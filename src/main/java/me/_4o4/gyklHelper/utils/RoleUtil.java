package me._4o4.gyklHelper.utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;

public class RoleUtil{
    /**
     * This method searches for a specified role
     *
     * @param name name of the role
     * @param guild guild to search in
     * @return null if not found, else the founded Role
     */
    public static Role getRole(String name, Guild guild){
        Role result = null;
        for(Role role : guild.getRolesByName(name, true)){
            result = role;
        }
        return result;
    }
}
