package io.jenkins.plugins;

import com.navarambh.mltests.model.MLTestResult;
import com.thoughtworks.xstream.XStream;
import hudson.XmlFile;
import hudson.util.HeapSpaceStringConverter;
import hudson.util.XStream2;
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
    void test() {
        /*assertThrows(IOException.class, () -> {
            XmlFile xmlHudsonFile = new XmlFile(XSTREAM, getDataFile("/test.xml"));
            MLTestResult result = (MLTestResult) xmlHudsonFile.read();
        });*/

        XmlFile xmlHudsonFile = null;
        try {
            xmlHudsonFile = new XmlFile(XSTREAM, getDataFile("/test.xml"));
            MLTestResult result = (MLTestResult) xmlHudsonFile.read();
            System.out.println("namee->>>" + result.getName());
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            System.out.println("deu coco");
        }

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
        XSTREAM.alias("testsuite", MLTestResult.class);
        XSTREAM.ignoreUnknownElements();
        XSTREAM.processAnnotations(MLTestResult.class);
        XSTREAM.registerConverter(new HeapSpaceStringConverter(), 100);
    }

}
