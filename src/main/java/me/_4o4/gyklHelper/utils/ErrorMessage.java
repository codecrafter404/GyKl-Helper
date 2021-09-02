package me._4o4.gyklHelper.utils;

import me._4o4.gyklHelper.GyKlHelper;

public class ErrorMessage {
    public static String getErrorMessageForError(String language, String error){
        switch (error){
            case "ERR_LEHRER_NOT_FOUND":
                return GyKlHelper.getLanguageManager().getLang(language).getAPI_teacher_not_found();
            case "ERR_KLASSE_NOT_FOUND":
                return GyKlHelper.getLanguageManager().getLang(language).getAPI_class_not_found();
            case "ERR_WRONG_PASSWORD":
                return GyKlHelper.getLanguageManager().getLang(language).getAPI_wrong_password();
            case "ERR_NO_DATA_FOUND":
                return GyKlHelper.getLanguageManager().getLang(language).getAPI_no_data();
            case "ERR_INVALID_DATE":
                return GyKlHelper.getLanguageManager().getLang(language).getAPI_Invalid_date();
            default:
                return GyKlHelper.getLanguageManager().getLang(language).getAPI_unknown_error();
        }

    }
}
