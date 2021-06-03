package io.jenkins.plugins;

import com.navarambh.mltests.utils.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class MLFileTest {

    @Test
    public void testFile(){
        Path resourceDirectory = Paths.get("src","test","resources");
        String testResourcesAbsolutePath = resourceDirectory.toFile().getAbsolutePath();
        Set<File> xmlFiles = new FileUtils().getXMLFiles(testResourcesAbsolutePath);
        System.out.println(xmlFiles.size());
    }
}
