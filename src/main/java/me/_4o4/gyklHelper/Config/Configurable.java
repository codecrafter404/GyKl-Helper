package me._4o4.gyklHelper.Config;

import me._4o4.gyklHelper.models.Server;
import net.dv8tion.jda.api.entities.Message;

public interface Configurable {
    boolean isValidInput(Message message, Server server);
    String getName();
    void configure(Message message, Server server);
    String getUsage();
}
