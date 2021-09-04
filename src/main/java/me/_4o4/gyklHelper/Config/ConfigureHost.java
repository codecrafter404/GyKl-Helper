package me._4o4.gyklHelper.Config;

import me._4o4.gyklHelper.GyKlHelper;
import me._4o4.gyklHelper.models.Server;
import me._4o4.gyklHelper.utils.Database;
import net.dv8tion.jda.api.entities.Message;
import org.pmw.tinylog.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigureHost implements Configurable{
    @Override
    public boolean isValidInput(Message message, Server server) {
        String[] args = message.getContentStripped().split(" ");
        if(args.length != 2 && args.length != 3) return false;
        if(args.length == 3){
            Pattern pattern = Pattern.compile("^(((?!-))(xn--|_{1,1})?[a-z0-9-]{0,61}[a-z0-9]{1,1}\\.)*(xn--)?([a-z0-9][a-z0-9\\-]{0,60}|[a-z0-9-]{1,30}\\.[a-z]{2,})$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(args[2]);
            return matcher.find();
        }
        return true;
    }

    @Override
    public String getName() {
        return "host";
    }

    @Override
    public void configure(Message message, Server server) {
        String[] args = message.getContentStripped().split(" ");
        if(args.length == 2){
            message.getChannel().sendMessage(
                    String.format("[:desktop:] = '%s'", server.getConfig().getApi_host())
            ).queue();
            return;
        }
        try{
            server.getConfig().setApi_host(args[2]);
            new Database().updateServer(server.getServer_id(), server);
            message.getChannel().sendMessage(
                    String.format("[:desktop:] -> '%s'", args[2])
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
        return "!config host [host]";
    }
}
