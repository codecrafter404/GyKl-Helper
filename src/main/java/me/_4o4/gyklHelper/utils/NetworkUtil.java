package me._4o4.gyklHelper.utils;

import me._4o4.gyklHelper.GyKlHelper;
import net.dv8tion.jda.api.JDA;

public class NetworkUtil {
    /**
     * This method checks if the network is available using JDAs api
     *
     * @return true if network is available, else false
     */
    public static boolean isNetworkAvailable(){
        return GyKlHelper.getJda().getStatus() == JDA.Status.CONNECTED;
    }
}
