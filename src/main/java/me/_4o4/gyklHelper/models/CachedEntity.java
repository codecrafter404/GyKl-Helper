package me._4o4.gyklHelper.models;

public class CachedEntity {
    private String timestamp;
    private String date;

    public CachedEntity(String timestamp, String date) {
        this.timestamp = timestamp;
        this.date = date;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
