package me._4o4.gyklHelper.utils;

import me._4o4.gyklHelper.GyKlHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DateAndTimeUtil {
    /**
     * This method validates a specified string as a date
     *
     * @param dateStr date to validate
     * @return true if valid, else false
     */
    public static boolean isValid(String dateStr) {
        DateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    /**
     * This method parse a string to localtime
     *
     * @param time time to parse
     * @return null, if an error occurred, else the parsed time as localtime
     */
    public static LocalTime parseTime(String time){
        List<String> parseFormats = List.of(
                "HH:mm",
                "HH-mm",
                "HH.mm",
                "HH/mm"
        );
        for(String format : parseFormats){
            try{
                return LocalTime.parse(time, DateTimeFormatter.ofPattern(format));
            }catch (Exception e){}
        }
        return null;
    }

    /**
     * This method parse string to a date
     *
     * @param date date to parse from
     * @return null if error, else the parsed date
     */
    public static Date getDate(String date){
        if(date == null || date.equals("")) return null;
        DateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        sdf.setLenient(false);
        Date d = null;
        List<String> tomorrowStrings = new ArrayList<>();
        GyKlHelper.getLanguageManager().getAllLanguages().forEach(
                language ->{
                    tomorrowStrings.add(language.getDay_Tomorrow());
                }
        );
        if(!tomorrowStrings.contains(date.toLowerCase())){
            try {
                d = sdf.parse(date);
            } catch (ParseException e) {
                return null;
            }
        }else {
            Date dt = java.sql.Date.valueOf(LocalDate.now());
            Calendar c = Calendar.getInstance();
            c.setTime(dt);
            c.add(Calendar.DATE, 1);
            dt = c.getTime();
            return dt;
        }
        return d;
    }
}
