package me._4o4.gyklHelper.utils;

import me._4o4.gyklHelper.GyKlHelper;

public class ErrorMessage {
    /**
     * This method returns an error message from the error code
     *
     * @param language Language to get the result in
     * @param error the error string
     * @return an error message in the specified language
     */
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
            case "ERR_WEEKEND":
                return GyKlHelper.getLanguageManager().getLang(language).getIsWeekEnd();
            default:
                return GyKlHelper.getLanguageManager().getLang(language).getAPI_unknown_error();
        }

    }
}
