package me._4o4.gyklHelper.models;

public class MutedUser {
    String id;
    int time;

    public MutedUser(String id, int time) {
        this.id = id;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public int getTime() {
        return time;
    }
}
