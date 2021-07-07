package me._4o4.vplanbot.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
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
    public static Date getDate(String date){
        DateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        sdf.setLenient(false);
        Date d = null;
        if(!date.equalsIgnoreCase("morgen") && !date.equalsIgnoreCase("tomorrow") && !date.equalsIgnoreCase("today") && !date.equalsIgnoreCase("heute")){
            try {
                d = sdf.parse(date);
            } catch (ParseException e) {
                return d;
            }
        }else {
            if(date.equalsIgnoreCase("morgen") || date.equalsIgnoreCase("tomorrow")){
                Date dt = java.sql.Date.valueOf(LocalDate.now());
                Calendar c = Calendar.getInstance();
                c.setTime(dt);
                c.add(Calendar.DATE, 1);
                dt = c.getTime();
                return dt;
            }else {
                return java.sql.Date.valueOf(LocalDate.now());
            }
        }
        return d;
    }
}
