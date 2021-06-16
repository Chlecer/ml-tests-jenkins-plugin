package com.myown.mltests.postbuildsteps;

import com.google.common.collect.ImmutableList;
import com.myown.mltests.model.MLTestCase;
import com.myown.mltests.utils.FileUtils;
import com.myown.mltests.model.MLFailure;
import com.myown.mltests.model.MLTestResult;
import com.myown.mltests.utils.MLCloudClient;
import com.thoughtworks.xstream.XStream;
import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.*;
import hudson.model.*;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import hudson.util.FormValidation;
import hudson.util.HeapSpaceStringConverter;
import hudson.util.XStream2;
import jenkins.model.Jenkins;
import jenkins.tasks.SimpleBuildStep;
import org.apache.commons.lang.StringUtils;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class MLTestsResultRecorder extends Recorder implements SimpleBuildStep {

    private String frameworkType;
    private String resultsFilePath;
    private String mlServerAddress;
    private Boolean failBuildOnFailure = false;
    private Boolean hideDetails = false;
    private Entry entry;

    private static final XStream XSTREAM = new XStream2();

    static {
        XSTREAM.ignoreUnknownElements();
        XSTREAM.processAnnotations(new Class[] { MLTestResult.class, MLTestCase.class, MLFailure.class});
        XSTREAM.registerConverter(new HeapSpaceStringConverter(), 100);
    }

    @DataBoundConstructor
    public MLTestsResultRecorder(String frameworkType, String resultsFilePath, String mlServerAddress) {
        this.frameworkType =frameworkType;
        this.resultsFilePath = resultsFilePath;
        this.mlServerAddress = mlServerAddress;
    }

    public String getFrameworkType() { return frameworkType; }
    public String getResultsFilePath() { return resultsFilePath; }
    public String getMlServerAddress() {
        return mlServerAddress;
    }

    public Boolean getFailBuildOnFailure() {
        return failBuildOnFailure;
    }
    @DataBoundSetter
    public void setFailBuildOnFailure(boolean failBuildOnFailure) {
        this.failBuildOnFailure = failBuildOnFailure;
    }

    public Boolean getHideDetails() { return hideDetails; }
    @DataBoundSetter
    public void setHideDetails(boolean hideDetails) { this.hideDetails = hideDetails; }

    @DataBoundSetter
    public void setEntry(Entry entry) { this.entry = entry;}
    public Entry getEntry() {
        return entry;
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    @Override
    public void perform(@NonNull Run<?, ?> run, @NonNull FilePath filePath, @NonNull Launcher launcher,
                        @NonNull TaskListener taskListener) throws InterruptedException, IOException {

        logStartEnd(true, taskListener);

        //Fetch file
        Set<File> xmlFiles = new FileUtils().getXMLFiles(filePath.getRemote() + this.resultsFilePath);
        for (File xmlFile : xmlFiles) {
            taskListener.getLogger().println("File found -> " + xmlFile.getName());
            XmlFile xmlHudsonFile = new XmlFile(XSTREAM, xmlFile);
            MLTestResult result = (MLTestResult) xmlHudsonFile.read();
            taskListener.getLogger().println("name->>" + result.getName() + result.getTestcase().toString() +
                    " | " + result.getProperties().get(0).getProperty("java.class.version") + "| ML Server Address: " + mlServerAddress);
            try {
                MLCloudClient aioClient = new MLCloudClient(mlServerAddress, "fakeProjectID");
                aioClient.importResults( this.frameworkType, this.hideDetails, run, taskListener.getLogger());
            } catch (Exception e) {
                e.printStackTrace();
                taskListener.getLogger().println("Publishing results failed : " + e.getMessage());
                this.setResultStatus(run, taskListener);
            }
        }

        logStartEnd(false, taskListener);

    }

    private  void setResultStatus(Run run, TaskListener taskListener) {
        if(failBuildOnFailure) {
            taskListener.getLogger().println("Publish results to ML Tests : Marking build as failed.");
            run.setResult(Result.FAILURE);
        }
    }

    private static void logStartEnd(boolean start, TaskListener taskListener) {
        String startLog = "ML Tests : Publishing results";
        taskListener.getLogger().println(StringUtils.leftPad("",110,"*"));
        taskListener.getLogger().println(StringUtils.leftPad(start? startLog : startLog + " end.",start? 70 : 74));
        taskListener.getLogger().println(StringUtils.leftPad("",110,"*"));
    }

    public static abstract class Entry extends AbstractDescribableImpl<Entry> {}

    @Symbol("mlImport")
    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        public DescriptorImpl() {
            super(MLTestsResultRecorder.class);
            load();
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Publish results to ML Tests - ML Server";
        }


        public List<Descriptor> getEntryDescriptors() {
            Jenkins jenkins = Jenkins.get();
            try {
                return ImmutableList.of(jenkins.getDescriptor(NewCycle.class), jenkins.getDescriptor(ExistingCycle.class));
            } catch (Exception e){
                throw new NullPointerException("Error initializing entry options");
            }
        }

        public FormValidation doCheckFilePath(@QueryParameter String filePath) {
            if (StringUtils.isEmpty(filePath)) {
                return FormValidation.error("Please specify the file path of results file ");
            }
            return FormValidation.ok();
        }

    }

    public static final class ExistingCycle extends Entry {

        private final String cycleKey;

        @DataBoundConstructor public ExistingCycle(String cycleKey) {
            this.cycleKey = cycleKey;
        }

        public String getCycleKey() {
            return cycleKey;
        }

        @Extension public static class DescriptorImpl extends Descriptor<Entry> {

            @Override public String getDisplayName() {
                return "Use an existing cycle";
            }

            public FormValidation doCheckCycleKey(@QueryParameter String cycleKey)  {
                if (StringUtils.isEmpty(cycleKey)) {
                    return FormValidation.error("Cycle Key cannot be empty.");
                }
                return FormValidation.ok();
            }
        }
    }

    public static final class NewCycle extends Entry {

        private final String cyclePrefix;

        @DataBoundConstructor public NewCycle(String cyclePrefix) {
            this.cyclePrefix = cyclePrefix;
        }

        public String getCyclePrefix() {
            return cyclePrefix;
        }

        @Extension public static class DescriptorImpl extends Descriptor<Entry> {
            @Override public String getDisplayName() {
                return "Create new cycle for each job run";
            }

            public FormValidation doCheckCyclePrefix(@QueryParameter String cyclePrefix)  {
                if (StringUtils.isEmpty(cyclePrefix)) {
                    return FormValidation.error("Cycle Prefix cannot be empty.");
                }
                return FormValidation.ok();
            }
        }

    }

}
