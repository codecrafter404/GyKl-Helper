package me._4o4.gyklHelper.Commands;

import me._4o4.gyklHelper.models.Server;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class CommandManager {
    private static final List<Command> commands = Arrays.asList(
            new TestCommand(),
            new DayCommand()
    );

    public static boolean runCommand(String name, String[] args, MessageReceivedEvent event, Server server){
        Command cmd = null;
        for(Command command : commands){
            if(command.getTriggers().contains(name.toLowerCase())) cmd = command;
        }
        if(cmd != null) cmd.run(args, event, server);
        return cmd != null;
    }
}
