package me._4o4.gyklHelper.models;

import java.util.ArrayList;
import java.util.List;

public class ServerConfig {
    List<String> announcement_channel = new ArrayList<>();
    List<String> interact_channel = new ArrayList<>();
    String api_host;
    String api_password;
    List<String> announcement_time = new ArrayList<>();
    String default_class;
    String language = "eng-us";
    boolean debug = false;
    String roleID;
    String prefix = "!";

    public ServerConfig(List<String> announcement_channel, List<String> interact_channel, String api_host, String api_password, List<String> announcement_time, String default_class, String language, boolean debug, String roleID, String prefix) {
        this.announcement_channel = announcement_channel;
        this.interact_channel = interact_channel;
        this.api_host = api_host;
        this.api_password = api_password;
        this.announcement_time = announcement_time;
        this.default_class = default_class;
        this.language = language;
        this.debug = debug;
        this.roleID = roleID;
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

    public List<String> getAnnouncement_time() {
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

    public String getRoleID() {
        return roleID;
    }

    public void setAnnouncement_channel(List<String> announcement_channel) {
        this.announcement_channel = announcement_channel;
    }

    public void setInteract_channel(List<String> interact_channel) {
        this.interact_channel = interact_channel;
    }

    public void setApi_host(String api_host) {
        this.api_host = api_host;
    }

    public void setApi_password(String api_password) {
        this.api_password = api_password;
    }

    public void setAnnouncement_time(List<String> announcement_time) {
        this.announcement_time = announcement_time;
    }

    public void setDefault_class(String default_class) {
        this.default_class = default_class;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
