package me._4o4.gyklHelper.utils;



import me._4o4.vplanwrapper.models.Subject;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

public class DifferenceDatabase {
    private static HashMap<String, List<List<Subject>>> data = new HashMap<>();
    private static HashMap<String, LocalDateTime> dates = new HashMap<>();

    public static void update(String serverID, List<List<Subject>> subjects){
        data.put(serverID, subjects);
    }

    public static HashMap<String, List<List<Subject>>> getData() {
        return data;
    }

    public static void updateDate(String server_id, LocalDateTime data){
        dates.put(server_id, data);
    }

    public static HashMap<String, LocalDateTime> getDates() {
        return dates;
    }
}
