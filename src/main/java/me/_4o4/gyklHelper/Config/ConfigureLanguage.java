package me._4o4.gyklHelper.Config;

import me._4o4.gyklHelper.GyKlHelper;
import me._4o4.gyklHelper.Language.Language;
import me._4o4.gyklHelper.models.Server;
import me._4o4.gyklHelper.utils.Database;
import net.dv8tion.jda.api.entities.Message;
import org.pmw.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;

public class ConfigureLanguage implements Configurable{
    @Override
    public boolean isValidInput(Message message, Server server) {
        String[] args = message.getContentStripped().split(" ");
        if(args.length != 2 && args.length != 3) return false;
        if(args.length == 3){
            return GyKlHelper.getLanguageManager().getLang(args[2]) != null;
        }
        return true;
    }

    @Override
    public String getName() {
        return "language";
    }

    @Override
    public void configure(Message message, Server server) {
        String[] args = message.getContentStripped().split(" ");

        if (args.length == 2) {
            List<String> languages = new ArrayList<>();
            for (Language lang : GyKlHelper.getLanguageManager().getAllLanguages()) {
                languages.add(lang.getName());
            }
            message.getChannel().sendMessage(
                    String.format("[:flag_white:] = '%s'\n**%s**: %s",
                            server.getConfig().getLanguage(),
                            GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getConfig_options_text(),
                            String.join(", ", languages)
                    )
            ).queue();
            return;
        }

        try {
            server.getConfig().setLanguage(args[2]);
            new Database().updateServer(server.getServer_id(), server);
            message.getChannel().sendMessage(
                    String.format("[:flag_white:] -> '%s'", args[2])
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
        return "!config language [language]";
    }
}
