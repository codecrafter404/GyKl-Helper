package me._4o4.gyklHelper.models;

import java.util.concurrent.ScheduledFuture;

public class AnnouncementTask {
    private String serverID;
    private ScheduledFuture service;
    private String time;

    public AnnouncementTask(String serverID, ScheduledFuture service, String time) {
        this.serverID = serverID;
        this.service = service;
        this.time = time;
    }

    public String getServerID() {
        return serverID;
    }

    public ScheduledFuture getService() {
        return service;
    }

    public String getTime() {
        return time;
    }
}
