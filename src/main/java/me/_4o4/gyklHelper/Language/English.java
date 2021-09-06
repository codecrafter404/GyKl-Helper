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

    @Override
    public String getConfig_Description() {
        return "Use this command to configure Server Options";
    }

    @Override
    public String getConfig_Usage() {
        return "!config [OPTION] [VALUE] [VALUE2] [...]";
    }

    @Override
    public List<String> getConfig_triggers() {
        return Arrays.asList("config", "conf", "c");
    }

    @Override
    public String getConfig_i_have_no_Permission() {
        return "I don't have the permission to do my things.\nPlease give me admin permissions!";
    }

    @Override
    public String getConfig_u_have_no_Permission() {
        return "Sorry,\nyou don't have the permission to execute this command.";
    }

    @Override
    public String getConfig_failed_save_Role() {
        return "Failed to save Role.\nPlease try again later.\nIf the problem persists contact @Codecrafter_404#6203";
    }

    @Override
    public String getConfig_cant_update() {
        return "Can't update config.\nPlease try again later.\nIf the problem persists contact @Codecrafter_404#6203";
    }

    @Override
    public String getConfig_double_Object() {
        return "This channel is already in the list!";
    }

    @Override
    public String getConfig_object_not_found() {
        return "The object isn't in the List";
    }

    @Override
    public String getConfig_options_text() {
        return "Options";
    }

    @Override
    public String getConfig_wrong_password() {
        return "I've detected you try to configure a wrong password.\nPlease try again.";
    }

    @Override
    public List<String> getHelp_triggers() {
        return List.of("help", "h");
    }

    @Override
    public String getHelp_desciption() {
        return "Help!!!";
    }

    @Override
    public String getHelp_usage() {
        return "!help";
    }

    @Override
    public String getHelp_title() {
        return "Help!";
    }



}
