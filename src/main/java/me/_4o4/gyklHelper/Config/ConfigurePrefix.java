package me._4o4.gyklHelper.Config;

import me._4o4.gyklHelper.GyKlHelper;
import me._4o4.gyklHelper.models.Server;
import me._4o4.gyklHelper.utils.Database;
import net.dv8tion.jda.api.entities.Message;
import org.pmw.tinylog.Logger;


public class ConfigurePrefix implements Configurable{
    @Override
    public boolean isValidInput(Message message, Server server) {
        String[] args = message.getContentStripped().split(" ");
        if(args.length != 2 && args.length != 3) return false;
        return true;
    }

    @Override
    public String getName() {
        return "prefix";
    }

    @Override
    public void configure(Message message, Server server) {
        String[] args = message.getContentStripped().split(" ");
        if(args.length == 2){
            message.getChannel().sendMessage(
                    String.format("[:regional_indicator_p:] = '%s'", server.getConfig().getPrefix())
            ).queue();
            return;
        }
        try{
            server.getConfig().setPrefix(args[2]);
            new Database().updateServer(server.getServer_id(), server);
            message.getChannel().sendMessage(
                    String.format("[:regional_indicator_p:] -> '%s'", args[2])
            ).queue();
        }catch(Exception e){
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
        return "!config prefix [prefix]";
    }
}
