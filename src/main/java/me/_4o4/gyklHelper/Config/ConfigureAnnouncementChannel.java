package me._4o4.gyklHelper.Config;

import me._4o4.gyklHelper.GyKlHelper;
import me._4o4.gyklHelper.models.Server;
import me._4o4.gyklHelper.utils.Database;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.pmw.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;


public class ConfigureAnnouncementChannel implements Configurable{
    @Override
    public boolean isValidInput(Message message, Server server) {
        String[] args = message.getContentStripped().split(" ");
        if(args.length < 3) return false;
        switch (args[2]){
            case "add":
            case "delete":{
                if(args.length != 4) return false;
                return message.getMentionedChannels().size() == 1;
            }
            case "get":
                return args.length == 3;
        }
        return false;
    }

    @Override
    public String getName() {
        return "announcement";
    }

    @Override
    public void configure(Message message, Server server) {
        String[] args = message.getContentStripped().split(" ");
        switch(args[2]){
            case "add":{
                try{
                    TextChannel channel = message.getMentionedChannels().get(0);
                    if(server.getConfig().getAnnouncement_channel().contains(channel.getId())){
                        message.getChannel().sendMessage(
                                GyKlHelper
                                        .getLanguageManager()
                                        .getLang(server.getConfig().getLanguage())
                                        .getConfig_double_Object()
                        ).queue();
                        return;
                    }
                    List<String> old = new ArrayList<>();
                    server.getConfig().getAnnouncement_channel().forEach(
                            x -> {
                                old.add("#" + message.getGuild().getGuildChannelById(x).getName());
                            }
                    );
                    server.getConfig().getAnnouncement_channel().add(channel.getId());
                    new Database().updateServer(server.getServer_id(), server);
                    message.getChannel().sendMessage(String.format("[%s] -> [%s]",
                            "#" + channel.getName(),
                            String.join(", ", old))
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
                break;
            }
            case "delete":{
                try{
                    TextChannel channel = message.getMentionedChannels().get(0);
                    if(!server.getConfig().getAnnouncement_channel().contains(channel.getId())){
                        message.getChannel().sendMessage(
                                GyKlHelper
                                        .getLanguageManager()
                                        .getLang(server.getConfig().getLanguage())
                                        .getConfig_object_not_found()
                        ).queue();
                        return;
                    }
                    server.getConfig().getAnnouncement_channel().remove(channel.getId());
                    new Database().updateServer(server.getServer_id(), server);
                    message.getChannel().sendMessage(String.format("[%s] -> [:wastebasket:]",
                            "#" + channel.getName()
                    )).queue();
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
            }
            case "get":{
                List<String> channelNames = new ArrayList<>();
                server.getConfig().getAnnouncement_channel().forEach(
                        x ->{
                            channelNames.add("#" + message.getGuild().getGuildChannelById(x).getName());
                        }
                );
                message.getChannel().sendMessage(String.format("[:mega:] = [%s]", String.join(", ", channelNames))).queue();
                break;
            }
        }
    }

    @Override
    public String getUsage() {
        return "Usage: !config announcement [add|delete|get] [#channel]";
    }
}
