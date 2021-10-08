package me._4o4.gyklHelper.utils;

import me._4o4.gyklHelper.GyKlHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ResourceUtil {

    /**
     * This method extracts a resource from the JAR
     *
     * @param resource path to the resource
     * @param saveIn file to save to
     * @throws IOException Extraction error
     */
    public static void extractResource(String resource, String saveIn) throws IOException {
        Files.copy(
                GyKlHelper.class.getResourceAsStream(resource),
                new File(saveIn).getAbsoluteFile().toPath(),
                StandardCopyOption.REPLACE_EXISTING
        );

    }
}
