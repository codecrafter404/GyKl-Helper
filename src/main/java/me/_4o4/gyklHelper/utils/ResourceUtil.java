package me._4o4.gyklHelper.utils;

import me._4o4.gyklHelper.GyKlHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ResourceUtil {

    public static void extractResource(String resource, String saveIn) throws IOException {
        Files.copy(
                GyKlHelper.class.getResourceAsStream(resource),
                new File(saveIn).getAbsoluteFile().toPath(),
                StandardCopyOption.REPLACE_EXISTING
        );

    }
}
