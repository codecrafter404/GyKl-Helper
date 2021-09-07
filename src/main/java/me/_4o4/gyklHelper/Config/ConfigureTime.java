package me._4o4.gyklHelper.Config;

import me._4o4.gyklHelper.GyKlHelper;
import me._4o4.gyklHelper.models.Server;
import me._4o4.gyklHelper.utils.Database;
import me._4o4.gyklHelper.utils.DateAndTimeUtil;
import net.dv8tion.jda.api.entities.Message;
import org.pmw.tinylog.Logger;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ConfigureTime implements Configurable{
    @Override
    public boolean isValidInput(Message message, Server server) {
        String[] args = message.getContentStripped().split(" ");
        if(args.length < 3) return false;
        switch (args[2]){
            case "add":
            case "delete":{
                if(args.length != 4) return false;
                return DateAndTimeUtil.parseTime(args[3]) != null;
            }
            case "get":
                return args.length == 3;
        }
        return false;
    }

    @Override
    public String getName() {
        return "time";
    }

    @Override
    public void configure(Message message, Server server) {
        String[] args = message.getContentStripped().split(" ");
        switch (args[2]){
            case "add":
                try{
                    LocalTime date = DateAndTimeUtil.parseTime(args[3]);
                    if(date == null){
                        message.getChannel().sendMessage(GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getAPI_Invalid_date()).queue();
                        return;
                    }
                    String compiledTime = DateTimeFormatter.ofPattern("HH:mm").format(date);
                    if(server.getConfig().getAnnouncement_time().contains(compiledTime)){
                        message.getChannel().sendMessage(
                                GyKlHelper
                                        .getLanguageManager()
                                        .getLang(server.getConfig().getLanguage())
                                        .getConfig_double_Object()
                        ).queue();
                        return;
                    }
                    final List<String> old = server.getConfig().getAnnouncement_time();
                    server.getConfig().getAnnouncement_time().add(compiledTime);
                    new Database().updateServer(
                            server.getServer_id(),
                            server
                    );
                    message.getChannel().sendMessage(String.format("[%s] -> [%s]",
                            compiledTime,
                            String.join(", ", old))
                    ).queue();

                    //Schedule announcement
                    GyKlHelper.getAnnouncementScheduler().schedule(server, compiledTime);

                }catch(Exception e){
                    Logger.trace(e);
                    Logger.warn("Can't update DB Object!");
                    message.getChannel().sendMessage(GyKlHelper
                            .getLanguageManager()
                            .getLang(server.getConfig().getLanguage())
                            .getConfig_cant_update()
                    ).queue();
                }
                break;
            case "delete":
                try{
                    LocalTime date = DateAndTimeUtil.parseTime(args[3]);
                    if(date == null){
                        message.getChannel().sendMessage(GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getAPI_Invalid_date()).queue();
                        return;
                    }
                    String compiledTime = DateTimeFormatter.ofPattern("HH:mm").format(date);
                    if(!server.getConfig().getAnnouncement_time().contains(compiledTime)){
                        message.getChannel().sendMessage(
                                GyKlHelper
                                        .getLanguageManager()
                                        .getLang(server.getConfig().getLanguage())
                                        .getConfig_double_Object()
                        ).queue();
                        return;
                    }
                    server.getConfig().getAnnouncement_time().remove(compiledTime);
                    new Database().updateServer(
                            server.getServer_id(),
                            server
                    );
                    message.getChannel().sendMessage(String.format("[%s] -> [:wastebasket:]",
                                compiledTime
                            )
                    ).queue();

                    //Cancel announcement
                    GyKlHelper.getAnnouncementScheduler().cancel(server, compiledTime);

                }catch(Exception e){
                    Logger.trace(e);
                    Logger.warn("Can't update DB Object!");
                    message.getChannel().sendMessage(GyKlHelper
                            .getLanguageManager()
                            .getLang(server.getConfig().getLanguage())
                            .getConfig_cant_update()
                    ).queue();
                }
                break;
            case "get":
                message.getChannel().sendMessage(
                        String.format("[:clock:] = [%s]",
                                String.join(", ", server.getConfig().getAnnouncement_time())
                        )
                ).queue();
        }
    }

    @Override
    public String getUsage() {
        return "!config time [add|delete|get] [HH:MM]";
    }
}
