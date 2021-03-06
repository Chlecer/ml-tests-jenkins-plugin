package io.jenkins.plugins;

import com.myown.mltests.model.MLFailure;
import com.myown.mltests.model.MLTestCase;
import com.myown.mltests.model.MLTestResult;
import com.thoughtworks.xstream.XStream;
import hudson.XmlFile;
import hudson.util.HeapSpaceStringConverter;
import hudson.util.XStream2;
import org.junit.Assert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

public class MLTestResultRecorderTest {

    @Test
    @DisplayName("Test first test")
    void test() throws URISyntaxException, IOException {

        XmlFile xmlHudsonFile = null;

        xmlHudsonFile = new XmlFile(XSTREAM, getDataFile("/test.xml"));
        MLTestResult result = (MLTestResult) xmlHudsonFile.read();
        System.out.println("name->>" + result.getName() + result.getTestcase().toString() +
                " | " + result.getProperties().get(0).getProperty("java.class.version"));
        Assert.assertEquals("mluser", result.getName());
    }

    @Test
    @Disabled
    @DisplayName("Test first test2")
    void test2() {
        fail("Not yet implemented");
    }

    private File getDataFile(String name) throws URISyntaxException {
        return new File(this.getClass().getResource(name).toURI());
    }

    private static final XStream XSTREAM = new XStream2();

    static {
        XSTREAM.ignoreUnknownElements();
        XSTREAM.processAnnotations(new Class[] { MLTestResult.class, MLTestCase.class, MLFailure.class});
        XSTREAM.registerConverter(new HeapSpaceStringConverter(), 100);
    }

}
