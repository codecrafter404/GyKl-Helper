package me._4o4.gyklHelper.Commands;

import me._4o4.gyklHelper.models.Server;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.pmw.tinylog.Logger;

import java.util.Arrays;
import java.util.List;

public class TestCommand implements Command{
    @Override
    public void run(String[] args, MessageReceivedEvent event, Server server) {
        Logger.debug("TEST");
        event.getChannel().sendMessage("Test").queue();
    }

    @Override
    public List<String> getTriggers() {
        return Arrays.asList("test");
    }

    @Override
    public String getName() {
        return "Test";
    }

    @Override
    public String getDescription(Server server) {
        return "nope:smile:";
    }

    @Override
    public String getUsage(Server server) {
        return  "simply type test";
    }
}
