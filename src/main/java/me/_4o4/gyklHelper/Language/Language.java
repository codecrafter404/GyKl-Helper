package me._4o4.gyklHelper.Language;

import java.util.List;
import java.util.Locale;

public interface Language {
    String getName();
    String getErr_Register();
    String getErr_Command_not_found();
    List<String> getDay_Triggers();
    String getDay_Description();
    String getDay_Usage();
    String getDay_Tomorrow();
    String getCredentialsNotSet();
    String getUsage_String();
    String getDay_Ready();
    String getDay_API_Not_reachable();
    String getAPI_unknown_error();
    String getAPI_class_not_found();
    String getAPI_teacher_not_found();
    String getAPI_wrong_password();
    String getAPI_Invalid_date();
    String getAPI_no_data();
    String getDay_Image_failed();
    String getPlan_title_time();
    String getPlan_title_subject();
    String getPlan_title_teacher();
    String getPlan_title_room();
    String getPlan_title_info();
    String getPlan_info_title();
    Locale getLocal();
    String getConfig_Description();
    String getConfig_Usage();
    List<String> getConfig_triggers();
    String getConfig_i_have_no_Permission();
    String getConfig_u_have_no_Permission();
    String getConfig_failed_save_Role();
    String getConfig_cant_update();
    String getConfig_double_Object();
    String getConfig_object_not_found();
    String getConfig_options_text();
    String getConfig_wrong_password();
    List<String> getHelp_triggers();
    String getHelp_desciption();
    String getHelp_usage();
    String getHelp_title();
}
