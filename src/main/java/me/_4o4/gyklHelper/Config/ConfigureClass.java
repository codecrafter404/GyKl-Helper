package me._4o4.gyklHelper.Config;

import me._4o4.gyklHelper.GyKlHelper;
import me._4o4.gyklHelper.models.Server;
import me._4o4.gyklHelper.utils.Database;
import net.dv8tion.jda.api.entities.Message;
import org.pmw.tinylog.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigureClass implements Configurable{
    @Override
    public boolean isValidInput(Message message, Server server) {
        String[] args = message.getContentStripped().split(" ");
        if(args.length != 2 && args.length != 3) return false;
        if(args.length == 3){
            Pattern pattern = Pattern.compile("^(([5-9])|(1[0-2]))([a-z])$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(args[2]);
            return matcher.find();
        }
        return true;
    }

    @Override
    public String getName() {
        return "class";
    }

    @Override
    public void configure(Message message, Server server) {
        String[] args = message.getContentStripped().split(" ");
        if(args.length == 2){
            message.getChannel().sendMessage(
                    String.format("[:school:] = '%s'", server.getConfig().getDefault_class())
            ).queue();
            return;
        }
        try{
            server.getConfig().setDefault_class(args[2]);
            new Database().updateServer(server.getServer_id(), server);
            message.getChannel().sendMessage(
                    String.format("[:school:] -> '%s'", args[2])
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
        return "!config class [CLASS]";
    }
}
