package com.navarambh.mltests.utils;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils {

    private static final Pattern SUREFIRE_FILENAME = Pattern.compile("TEST-(.+)\\.xml");

        public static File getFile(String dir, String userPath) {
            File file = new File(dir + userPath);
            if(file.exists()) {
                return file;
            }
            return null;
        }

    public static File getDataFile(String name) throws URISyntaxException {
        return new File(FileUtils.class.getResource(name).toURI());
    }

    public Set<File> getXMLFiles(String dir) {

        Pattern SUREFIRE_FILENAME = Pattern.compile("TEST-(.+)\\.xml");

        return Stream.of(new File(dir).listFiles())
                .filter(file -> !file.isDirectory() && SUREFIRE_FILENAME.matcher(file.getName()).matches())
                .collect(Collectors.toSet());
    }
}


