package me._4o4.gyklHelper.models;

import org.pmw.tinylog.Logger;

import java.lang.reflect.Field;

public class Environment {


    static String DISCORD_TOKEN;
    static String MONGO_CONTAINER;
    static String MONGO_USERNAME;
    static String MONGO_PASSWORD;
    static String DEFAULT_LANGUAGE = "english";
    static String BOT_ADMIN_ROLE = "bot-admin";
    static String TIMEZONE = "Europe/Berlin";

    public static void parseEnvironments() throws IllegalAccessException {
        for(Field field : Environment.class.getDeclaredFields()){
            field.setAccessible(true);
            if(System.getenv(field.getName()) != null && System.getenv(field.getName()) != ""){
                field.set(String.class, System.getenv(field.getName()));
            }
            if(field.get("") == null){
                Logger.error("Environment variable '" + field.getName() + "' is not set!");
                System.exit(-1);
            }
            field.setAccessible(false);
        }
    }

    public static String getDiscordToken() {
        return DISCORD_TOKEN;
    }

    public static String getMongoContainer() {
        return MONGO_CONTAINER;
    }

    public static String getMongoUsername() {
        return MONGO_USERNAME;
    }

    public static String getMongoPassword() {
        return MONGO_PASSWORD;
    }

    public static String getDefaultLanguage() {
        return DEFAULT_LANGUAGE;
    }

    public static String getBotAdminRole() {
        return BOT_ADMIN_ROLE;
    }

    public static String getTIMEZONE() {
        return TIMEZONE;
    }
}
