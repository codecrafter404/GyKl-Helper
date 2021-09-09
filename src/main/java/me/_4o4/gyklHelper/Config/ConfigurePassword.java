package me._4o4.gyklHelper.Config;

import me._4o4.gyklHelper.GyKlHelper;
import me._4o4.gyklHelper.models.Server;
import me._4o4.gyklHelper.utils.Database;
import me._4o4.vplanwrapper.VPlanAPI;
import net.dv8tion.jda.api.entities.Message;
import org.apache.commons.codec.digest.DigestUtils;
import org.pmw.tinylog.Logger;

public class ConfigurePassword implements Configurable{


    @Override
    public boolean isValidInput(Message message, Server server) {
        String[] args = message.getContentStripped().split(" ");
        return args.length == 2 || args.length == 3;
    }

    @Override
    public String getName() {
        return "password";
    }

    @Override
    public void configure(Message message, Server server) {
        String[] args = message.getContentStripped().split(" ");

        if(args.length == 2){
            message.getChannel().sendMessage(
                    String.format("[:lock:] = '%s'", "***SECRET***")
            ).queue();
            return;
        }
        if(!server.getConfig().getApi_host().equals("")){
            //Check password through API
            try{
                VPlanAPI api = new VPlanAPI(
                        server.getConfig().getApi_host(),
                        "Doesn't matter", false
                );

                if(!api.checkPassword(args[2])){
                    message.getChannel().sendMessage(
                            GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getConfig_wrong_password()
                    ).queue();
                    return;
                }
            }catch(Exception e){
                Logger.trace(e);
                Logger.warn("Can't validate password!");
            }
        }
        try{
            server.getConfig().setApi_password(DigestUtils.md5Hex(args[2]));
            new Database().updateServer(server.getServer_id(), server);
            message.getChannel().sendMessage(
                    String.format("[:lock:] -> '%s'", args[2])
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
        return "!config password [password]";
    }
}
