package me._4o4.gyklHelper.Language;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class English implements Language{
    @Override
    public String getName() {
        return "english";
    }

    @Override
    public String getErr_Register() {
        return "Sorry, can't register.\nPlease try again later.";
    }

    @Override
    public String getErr_Command_not_found() {
        return "Can't find this command.\nType <prefix>help to see available commands.";
    }

    @Override
    public List<String> getDay_Triggers() {
        return Arrays.asList("day", "d");
    }


    @Override
    public String getDay_Description() {
        return "Show the VPlan of the day";
    }

    @Override
    public String getDay_Usage() {
        return "!day <[dd.mm.yyyy]/tomorrow>";
    }

    @Override
    public String getDay_Tomorrow() {
        return "tomorrow";
    }

    @Override
    public String getCredentialsNotSet() {
        return "Credentials aren't set.\nSet them with !config";
    }

    @Override
    public String getUsage_String() {
        return "Usage: ";
    }

    @Override
    public String getDay_Ready() {
        return "I'll get things ready for u!:thumbsup:";
    }

    @Override
    public String getDay_API_Not_reachable() {
        return "The API isn't reachable\nPlease make sure you've configured the right API-Host.\nIf the problem persists contact @Codecrafter_404#6203";
    }

    @Override
    public String getAPI_unknown_error() {
        return "Unknown API Error";
    }

    @Override
    public String getAPI_class_not_found() {
        return "Couldn't find data for configured class.";
    }

    @Override
    public String getAPI_teacher_not_found() {
        return "Couldn't find the Teacher.";
    }

    @Override
    public String getAPI_wrong_password() {
        return "You're configured a wrong password";
    }

    @Override
    public String getAPI_Invalid_date() {
        return "You've entered a invalid date";
    }

    @Override
    public String getAPI_no_data() {
        return "No data available!";
    }

    @Override
    public String getDay_Image_failed() {
        return "The image generation failed.\nPlease try again later.\nIf the problem persists contact @Codecrafter_404#6203";
    }

    @Override
    public String getPlan_title_time() {
        return "Time";
    }

    @Override
    public String getPlan_title_subject() {
        return "Subject";
    }

    @Override
    public String getPlan_title_teacher() {
        return "Teacher";
    }

    @Override
    public String getPlan_title_room() {
        return "Room";
    }

    @Override
    public String getPlan_title_info() {
        return "Info";
    }

    @Override
    public String getPlan_info_title() {
        return "Information:";
    }

    @Override
    public Locale getLocal() {
        return Locale.ENGLISH;
    }

}
