package me._4o4.gyklHelper.Commands;

import me._4o4.gyklHelper.GyKlHelper;
import me._4o4.gyklHelper.models.Server;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand implements Command{
    @Override
    public void run(String[] args, MessageReceivedEvent event, Server server) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(
                GyKlHelper
                        .getLanguageManager()
                        .getLang(server.getConfig().getLanguage())
                        .getHelp_title()

        );
        CommandManager.getAllCommands()
                .forEach(
                        x ->{
                            embedBuilder.addField(x.getName() + ":", x.getDescription(server), false);
                        }
                );
        event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
    }

    @Override
    public List<String> getTriggers() {
        List<String> triggers = new ArrayList<>();

        GyKlHelper.getLanguageManager().getAllLanguages().forEach(
                l ->{
                    triggers.addAll(l.getHelp_triggers());
                });
        return triggers;
    }

    @Override
    public String getName() {
        return "Help";
    }

    @Override
    public String getDescription(Server server) {
        return GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getHelp_desciption();
    }

    @Override
    public String getUsage(Server server) {
        return GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getHelp_usage();
    }
}
