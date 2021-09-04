package me._4o4.gyklHelper.Language;

import java.util.List;
import java.util.Locale;

public interface Language {
    public String getName();
    public String getErr_Register();
    public String getErr_Command_not_found();
    public List<String> getDay_Triggers();
    public String getDay_Description();
    public String getDay_Usage();
    public String getDay_Tomorrow();
    public String getCredentialsNotSet();
    public String getUsage_String();
    public String getDay_Ready();
    public String getDay_API_Not_reachable();
    public String getAPI_unknown_error();
    public String getAPI_class_not_found();
    public String getAPI_teacher_not_found();
    public String getAPI_wrong_password();
    public String getAPI_Invalid_date();
    public String getAPI_no_data();
    public String getDay_Image_failed();
    public String getPlan_title_time();
    public String getPlan_title_subject();
    public String getPlan_title_teacher();
    public String getPlan_title_room();
    public String getPlan_title_info();
    public String getPlan_info_title();
    public Locale getLocal();
    public String getConfig_Description();
    public String getConfig_Usage();
    public List<String> getConfig_triggers();
    public String getConfig_i_have_no_Permission();
    public String getConfig_u_have_no_Permission();
    public String getConfig_failed_save_Role();
    public String getConfig_cant_update();
    public String getConfig_double_Object();
    public String getConfig_object_not_found();
    public String getConfig_options_text();
    public String getConfig_wrong_password();
    public List<String> getHelp_triggers();
    public String getHelp_desciption();
    public String getHelp_usage();
    public String getHelp_title();
}
