package me._4o4.gyklHelper.Language;

import java.util.List;
import java.util.Locale;

public class German implements Language{
    @Override
    public String getName() {
        return "german";
    }

    @Override
    public String getErr_Register() {
        return "Entschuldigung, ein problem bei dem registrieren des Servers ist ein problem aufgetreten. \nBitte versuchen sie es später erneut!";
    }

    @Override
    public String getErr_Command_not_found() {
        return "Dieses Command ist nicht definiert\n Geben sie !hilfe ein um sich alle unterstützten commands anzuzeigen";
    }

    @Override
    public List<String> getDay_Triggers() {
        return List.of("tag", "t");
    }

    @Override
    public String getDay_Description() {
        return "Lassen sie sich den Vertretungsplan für ein bestimmtes Datum anzeigen";
    }

    @Override
    public String getDay_Usage() {
        return "!tag [Datum mit vollem Jahr(z.b.: 06.09.2021)]";
    }

    @Override
    public String getDay_Tomorrow() {
        return "morgen";
    }

    @Override
    public String getCredentialsNotSet() {
        return "Die Zugangsdaten sind falsch oder nicht konfiguriert";
    }

    @Override
    public String getUsage_String() {
        return "Benutzung: ";
    }

    @Override
    public String getDay_Ready() {
        return "Ich bereite alle dinge für sie vor :thumbsup:";
    }

    @Override
    public String getDay_API_Not_reachable() {
        return "Ich kann die API nicht erreichen.\nBitte vergewissern sie sich das sie den richtigen Host konfiguriert haben.\nFalls das Problem besteht kontaktieren sie @Codecrafter_404#6203";
    }

    @Override
    public String getAPI_unknown_error() {
        return "Ein unbekannter API Fehler ist aufgetreten";
    }

    @Override
    public String getAPI_class_not_found() {
        return "Ich konnte die konfigurierte Klasse nicht finden.";
    }

    @Override
    public String getAPI_teacher_not_found() {
        return "Ich konnte den Lehrer nicht feststellen.";
    }

    @Override
    public String getAPI_wrong_password() {
        return "Sie haben ein falsches passwort konfiguriert,\nändern sie dieses mit !konfiguration password <Passwort>";
    }

    @Override
    public String getAPI_Invalid_date() {
        return "Sie haben ein falsches Datum definiert.";
    }

    @Override
    public String getAPI_no_data() {
        return "Es sind keine daten vorhanden.";
    }

    @Override
    public String getDay_Image_failed() {
        return "Die Bild-erzeugung ist gescheitert.\nBitte versuchen sie es später erneut.\nFalls das Problem besteht kontaktieren sie @Codecrafter_404#6203";
    }

    @Override
    public String getPlan_title_time() {
        return "Zeit";
    }

    @Override
    public String getPlan_title_subject() {
        return "Fach";
    }

    @Override
    public String getPlan_title_teacher() {
        return "Lehrer";
    }

    @Override
    public String getPlan_title_room() {
        return "Raum";
    }

    @Override
    public String getPlan_title_info() {
        return "Infos";
    }

    @Override
    public String getPlan_info_title() {
        return "Informationen:";
    }

    @Override
    public Locale getLocal() {
        return Locale.GERMAN;
    }

    @Override
    public String getConfig_Description() {
        return "Benutzen sie diesen Command um Server Einstellungen zu modifizieren.";
    }

    @Override
    public String getConfig_Usage() {
        return "!config [OPTION] [WERT] [WERT2] [...]";
    }

    @Override
    public List<String> getConfig_triggers() {
        return List.of("konfiguration", "k");
    }

    @Override
    public String getConfig_i_have_no_Permission() {
        return "Ich habe leider keine Berechtigung um meine Dinge zu machen.\nBitte erteilen sie mir vollzugriff damit ich sie Bedienen kann.";
    }

    @Override
    public String getConfig_u_have_no_Permission() {
        return "Sie haben leider keine Berechtigung diese operation auszuführen.";
    }

    @Override
    public String getConfig_failed_save_Role() {
        return "Fahler beim speichern der Rolle.\nBitte versuchen sie es später erneut.\nFalls das Problem besteht kontaktieren sie @Codecrafter_404#6203";
    }

    @Override
    public String getConfig_cant_update() {
        return "Ich kann die änderung nicht speichen.\nBitte versuchen sie es später erneut.\nFalls das Problem besteht kontaktieren sie @Codecrafter_404#6203";
    }

    @Override
    public String getConfig_double_Object() {
        return "Dieses Objekt existiert bereits.";
    }

    @Override
    public String getConfig_object_not_found() {
        return "Ich konnte das Objekt nicht finden.";
    }

    @Override
    public String getConfig_options_text() {
        return "Optionen";
    }

    @Override
    public String getConfig_wrong_password() {
        return "Ich haber erkannt dass sie versuchen ein falsches passwort zu konfigurieren.";
    }

    @Override
    public List<String> getHelp_triggers() {
        return List.of("hilfe");
    }

    @Override
    public String getHelp_desciption() {
        return "Hilfe!!!";
    }

    @Override
    public String getHelp_usage() {
        return "!hilfe";
    }

    @Override
    public String getHelp_title() {
        return "Hilfe!!!";
    }
}
