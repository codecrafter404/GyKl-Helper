package me._4o4.gyklHelper.schedule;

import me._4o4.gyklHelper.GyKlHelper;
import me._4o4.gyklHelper.utils.Database;
import me._4o4.gyklHelper.utils.NetworkUtil;
import net.dv8tion.jda.api.entities.Guild;
import org.pmw.tinylog.Logger;

public class CleanUpDatabaseSchedule implements Runnable{

    @Override
    public void run() {
        //Check network connection
        if(!NetworkUtil.isNetworkAvailable()){
            Logger.error("Can't run CleanUpServer: Network unavailable!");
            return;
        }

        new Database().getAllServers().forEach(
                server -> {
                    Guild guild = GyKlHelper.getJda().getGuildById(server.getServer_id());
                    if(guild == null){
                        //Server deleted / kicked from server
                        Logger.info(String.format("Server '%s' got deleted or kicked me, delete database object for the server.", server.getServer_name()));
                        try{
                            new Database().deleteServer(server.getServer_id());
                            GyKlHelper.getAnnouncementScheduler().cancelAllFromServer(server);
                        }catch (Exception e){
                            Logger.trace(e);
                            Logger.error("Can't delete database object!");
                        }
                        return;
                    }
                    if(!server.getServer_name().equalsIgnoreCase(guild.getName())){
                        //Update Name
                        Logger.info(String.format("Server '%s' changed the name to '%s', update DB object.", server.getServer_name(), guild.getName()));
                        try{
                            server.setServer_name(guild.getName());
                            new Database().updateServer(server.getServer_id(), server);
                        }catch (Exception e){
                            Logger.trace(e);
                            Logger.error("Can't update database object!");
                        }
                        return;
                    }
                }
        );
    }
}
