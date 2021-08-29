package me._4o4.gyklHelper.utils;

import com.google.gson.Gson;
import me._4o4.gyklHelper.models.Config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigParse {
    public static Config parseConfig(String configFile) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(configFile)));
        return new Gson().fromJson(json, Config.class);
    }
}
