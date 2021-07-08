package me._4o4.vplanbot.models;

import java.lang.reflect.Field;

public class Environment {
    private static String VPLAN_ANNOUNCE = "vertretungsplan";
    private static String VPLAN_BOT = "bot";
    private static String VPLAN_HOST = "";
    private static String VPLAN_PASSWORD = "";
    private static String VPLAN_TIME = "16";
    private static String VPLAN_CLASS = "7b";

    public static void parseEnvironments() throws IllegalAccessException {
        for(Field field : Environment.class.getDeclaredFields()){
            field.setAccessible(true);
            if(System.getenv(field.getName()) != null){
                field.set(String.class, System.getenv(field.getName()));
            }
            if(field.get("") == ""){
                System.out.println("[ERROR] Environment variable '" + field.getName() + "' is not set!");
                System.exit(-1);
            }
            field.setAccessible(false);
        }
        try{
            if(!(Integer.parseInt(VPLAN_TIME) >= 0) || !(Integer.parseInt(VPLAN_TIME) <= 24)){
                System.out.println("[ERROR] VPLAN_TIME is a incorrect time");
                System.exit(-1);
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("[ERROR] VPLAN_TIME is a incorrect time");
            System.exit(-1);
        }
    }

    public static String getVplanClass() {
        return VPLAN_CLASS;
    }

    public static String getVplanAnnounce() {
        return VPLAN_ANNOUNCE;
    }

    public static String getVplanBot() {
        return VPLAN_BOT;
    }

    public static String getVplanHost() {
        return VPLAN_HOST;
    }

    public static String getVplanPassword() {
        return VPLAN_PASSWORD;
    }

    public static String getVplanTime() {
        return VPLAN_TIME;
    }
}
