package me._4o4.gyklHelper.utils;

import me._4o4.gyklHelper.GyKlHelper;
import net.dv8tion.jda.api.JDA;

public class NetworkUtil {
    public static boolean isNetworkAvailable(){
        return GyKlHelper.getJda().getStatus() == JDA.Status.CONNECTED;
    }
}
