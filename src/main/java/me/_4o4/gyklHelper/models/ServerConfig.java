package me._4o4.gyklHelper.models;

import java.util.ArrayList;
import java.util.List;

public class ServerConfig {
    List<String> announcement_channel = new ArrayList<>();
    List<String> interact_channel = new ArrayList<>();
    String api_host;
    String api_password;
    List<Integer> announcement_time = new ArrayList<>();
    String default_class;
    String language = "eng-us";
    boolean debug = false;
    String prefix = "!";

    public ServerConfig(List<String> announcement_channel, List<String> interact_channel, String api_host, String api_password, List<Integer> announcement_time, String default_class, String language, boolean debug, String prefix) {
        this.announcement_channel = announcement_channel;
        this.interact_channel = interact_channel;
        this.api_host = api_host;
        this.api_password = api_password;
        this.announcement_time = announcement_time;
        this.default_class = default_class;
        this.language = language;
        this.debug = debug;
        this.prefix = prefix;
    }

    public List<String> getAnnouncement_channel() {
        return announcement_channel;
    }

    public List<String> getInteract_channel() {
        return interact_channel;
    }

    public String getApi_host() {
        return api_host;
    }

    public String getApi_password() {
        return api_password;
    }

    public List<Integer> getAnnouncement_time() {
        return announcement_time;
    }

    public String getDefault_class() {
        return default_class;
    }

    public String getLanguage() {
        return language;
    }

    public boolean isDebug() {
        return debug;
    }

    public String getPrefix() {
        return prefix;
    }
}
