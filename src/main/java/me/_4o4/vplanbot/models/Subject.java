package me._4o4.vplanbot.models;

public class Subject {
    private final String time;
    private final String name;
    private final String teacher;
    private final String room;
    private final String info;
    private final String status;

    public Subject(String time, String name, String teacher, String room, String info, String status) {
        this.time = time;
        this.name = name;
        this.teacher = teacher;
        this.room = room;
        this.info = info;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getRoom() {
        return room;
    }

    public String getInfo() {
        return info;
    }
}
