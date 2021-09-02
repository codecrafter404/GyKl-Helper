package me._4o4.gyklHelper.Commands;

import me._4o4.gyklHelper.models.Server;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public interface Command {
    public void run(String[] args, MessageReceivedEvent event, Server server);
    public List<String> getTriggers();
    public String getName();
    public String getDescription(Server server);
    public String getUsage(Server server);
}
