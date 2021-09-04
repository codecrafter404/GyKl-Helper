package me._4o4.gyklHelper.Config;

import me._4o4.gyklHelper.GyKlHelper;
import me._4o4.gyklHelper.Language.Language;
import me._4o4.gyklHelper.models.Server;
import me._4o4.gyklHelper.utils.Database;
import net.dv8tion.jda.api.entities.Message;
import org.pmw.tinylog.Logger;

import java.util.List;

public class ConfigureDebug implements Configurable{
    @Override
    public boolean isValidInput(Message message, Server server) {
        String[] args = message.getContentStripped().split(" ");
        if(args.length != 2 && args.length != 3) return false;
        if(args.length == 3){
            return args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("false");
        }
        return true;
    }

    @Override
    public String getName() {
        return "debug";
    }

    @Override
    public void configure(Message message, Server server) {
        String[] args = message.getContentStripped().split(" ");

        if (args.length == 2) {
            List<String> state = List.of("true", "false");
            message.getChannel().sendMessage(
                    String.format("[:bug:] = '%s'\n**%s**: %s",
                            server.getConfig().isDebug(),
                            GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getConfig_options_text(),
                            String.join(", ", state)
                    )
            ).queue();
            return;
        }

        try {
            server.getConfig().setDebug(Boolean.parseBoolean(args[2].toLowerCase()));
            new Database().updateServer(server.getServer_id(), server);
            message.getChannel().sendMessage(
                    String.format("[:bug:] -> '%s'", args[2])
            ).queue();
        } catch (Exception e) {
            Logger.trace(e);
            Logger.warn("Can't update DB Object!");
            message.getChannel().sendMessage(GyKlHelper
                    .getLanguageManager()
                    .getLang(server.getConfig().getLanguage())
                    .getConfig_cant_update()
            ).queue();
        }

    }

    @Override
    public String getUsage() {
        return "!config debug [true|false]";
    }
}
