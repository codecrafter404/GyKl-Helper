package me._4o4.gyklHelper.Commands;

import me._4o4.gyklHelper.Config.*;
import me._4o4.gyklHelper.GyKlHelper;
import me._4o4.gyklHelper.models.Environment;
import me._4o4.gyklHelper.models.Server;
import me._4o4.gyklHelper.utils.Database;
import me._4o4.gyklHelper.utils.RoleUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.pmw.tinylog.Logger;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ConfigCommand implements Command{
    @Override
    public void run(String[] args, MessageReceivedEvent event, Server server) {
        Role botAdmin = null;
        if(server.getConfig().getRoleID().equals("")){
                // New Server

                // is there already a Role
                botAdmin = RoleUtil.getRole(Environment.getBotAdminRole(), event.getGuild());
                if(botAdmin == null){
                    try{
                        //Create new Role
                        botAdmin = event.getGuild().createRole()
                                .setColor(Color.getColor("red"))
                                .setHoisted(false)
                                .setMentionable(false)
                                .setName(Environment.getBotAdminRole())
                                .setPermissions(Permission.EMPTY_PERMISSIONS)
                                .submit().get();
                        Logger.info(String.format("Created Role '%s' on '%s'", Environment.getBotAdminRole(), event.getGuild().getName()));

                    }catch(Exception e){
                        Logger.trace(e);
                        event.getChannel().sendMessage(GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getConfig_i_have_no_Permission()).queue();
                        return;
                    }
                }

                try{
                    server.getConfig().setRoleID(botAdmin.getId());
                    //Save to DB
                    new Database().updateServer(server.getServer_id(), server);
                    Logger.info("Saved Role");
                }catch(Exception e){
                    Logger.trace(e);
                    event.getChannel().sendMessage(GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getConfig_failed_save_Role()).queue();
                    return;
                }
        }else{
            botAdmin = event.getGuild().getRoleById(server.getConfig().getRoleID());
        }
        if(!event.getMember().getRoles().contains(botAdmin)){
            event.getChannel().sendMessage(GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getConfig_u_have_no_Permission()).queue();
            return;
        }

        List<Configurable> configs = new ArrayList<>();
        configs.add(new ConfigureAnnouncementChannel());
        configs.add(new ConfigureInteractChannel());
        configs.add(new ConfigureHost());
        configs.add(new ConfigurePassword());
        configs.add(new ConfigureTime());
        configs.add(new ConfigureClass());
        configs.add(new ConfigureLanguage());
        configs.add(new ConfigureDebug());
        configs.add(new ConfigurePrefix());

        if(args.length == 1){
            event.getChannel().sendMessage(
                    getUsage(server)
            ).queue();
            sendHelp(event, server, configs);
            return;
        }

        for(Configurable conf : configs){
            if(conf.getName().equalsIgnoreCase(args[1])){
                if(conf.isValidInput(event.getMessage(), server)){
                    conf.configure(event.getMessage(), server);
                }else {
                    event.getChannel().sendMessage(conf.getUsage()).queue();
                }
                return;
            }
        }
        sendHelp(event, server, configs);
    }

    private void sendHelp(MessageReceivedEvent event, Server server,List<Configurable> configs){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getConfig_options_text() + ":");
        configs.forEach(
                conf -> {
                    builder.addField(conf.getName() + ":", conf.getUsage(), false);
                }
        );
        event.getChannel().sendMessageEmbeds(builder.build()).queue();

    }
    @Override
    public List<String> getTriggers() {
        List<String> triggers = new ArrayList<>();

        GyKlHelper.getLanguageManager().getAllLanguages().forEach(
                l ->{
                    triggers.addAll(l.getConfig_triggers());
                });
        return triggers;
    }

    @Override
    public String getName() {
        return "Config";
    }

    @Override
    public String getDescription(Server server) {
        return GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getConfig_Description();
    }

    @Override
    public String getUsage(Server server) {
        return GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getConfig_Usage();
    }
}
