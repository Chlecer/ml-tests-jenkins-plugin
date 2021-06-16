package com.myown.mltests.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@XStreamAlias("testsuite")
public class MLTestResult implements Serializable {

    @XStreamImplicit
    protected List<Properties> properties;

    @XStreamImplicit
    protected List<MLTestCase> testcase = new ArrayList<MLTestCase>();

    protected String name;

    protected String time;

    protected String tests;

    protected String errors;

    protected String skipped;

    protected String failures;

    protected String group;

    public List<Properties> getProperties() {
        return properties;
    }

    public String getName() {
        return name;
    }

    public List<MLTestCase> getTestcase() {
        return testcase;
    }

    public String getTime() {
        return time;
    }

    public String getTests() {
        return tests;
    }

    public String getErrors() {
        return errors;
    }

    public String getSkipped() {
        return skipped;
    }

    public String getFailures() {
        return failures;
    }

    public String getGroup() {
        return group;
    }
}
