package me._4o4.gyklHelper.Listeners;

import me._4o4.gyklHelper.Commands.CommandManager;
import me._4o4.gyklHelper.GyKlHelper;
import me._4o4.gyklHelper.models.Environment;
import me._4o4.gyklHelper.models.Server;
import me._4o4.gyklHelper.utils.Database;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.pmw.tinylog.Logger;

import java.util.concurrent.TimeUnit;


public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        Database database = new Database();
        Server server = database.getServerFromID(event.getGuild().getId());
        if(server == null){
            //New Server
            Logger.info("Register on new server: " + event.getGuild().getName());
            server = Server.getDefault(event.getGuild().getId(), event.getGuild().getName());
            try{
                database.saveServer(server);
            }catch (Exception e){
                Logger.trace(e);
                Logger.error("Can't register " + event.getGuild().getName());
                event.getChannel().sendMessage(GyKlHelper.getLanguageManager().getLang(Environment.getDefaultLanguage()).getErr_Register()).queue();
                return;
            }
        }
        //Check is interact channel or its !config
        if(!server.getConfig().getInteract_channel().contains(event.getChannel().getId()) && !event.getMessage().getContentRaw().equals("!config")) return;
        //Check is defined Prefix or its !config
        if(!event.getMessage().getContentRaw().startsWith(server.getConfig().getPrefix()) && !event.getMessage().getContentRaw().equals("!config")) return;

        event.getChannel().sendTyping().queue();
        String message = event.getMessage().getContentRaw();
        String[] args = message.split(" ");
        String commmand = args[0].substring(server.getConfig().getPrefix().length()).toLowerCase();
        boolean succeed = CommandManager.runCommand(commmand, args, event, server);
        if(!succeed){
            event.getChannel().sendMessage(GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getErr_Command_not_found())
                    .delay(10, TimeUnit.SECONDS)
                    .flatMap(Message::delete)
                    .flatMap(m -> event.getMessage().delete())
                    .queue();
        }

    }

    /*
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(!event.getMessage().getChannel().getName().equalsIgnoreCase(Environment.getVplanBot())) return;
        if(!event.getMessage().getContentRaw().startsWith("!")) return;
        if(event.getAuthor().isBot()) return;

        String[] args = event.getMessage().getContentRaw().split(" ");
        switch (args[0]){

            case "!smile":
                event.getMessage().getChannel().sendMessage(
                        ":smile:"
                ).queue();
                break;
            case "!day":

                if(args.length != 2){
                    event.getMessage().getChannel().sendMessage("No date specified!\nUsage: !day <mm/dd/yyyy> or !day <today/heute/tomorrow/morgen>").queue();
                    break;
                }
                Date date = DateUtil.getDate(args[1]);
                if(date == null){
                    event.getMessage().getChannel().sendMessage("Invalid date!\nUsage: !day <mm/dd/yyyy> or !day <today/heute/tomorrow/morgen>").queue();
                    break;
                }

                event.getMessage().getChannel().sendMessage("I'll get things ready for u!:thumbsup:").queue();
                try{
                    Week week = new VPlanAPI(Environment.getVplanHost(), Environment.getVplanPassword()).getWeek(
                            Arrays.asList(new SimpleDateFormat("yyyy-MM-dd").format(date)),
                            Environment.getVplanClass()
                    );
                    if(week.getDays().get(0).getError() != ""){
                        event.getMessage().getChannel().sendMessage("Day not found, not available or its on weekend!:face_with_monocle:").queue();
                        break;
                    }
                    Converter converter = new Converter(
                            week.getDays().get(0),
                            "table.ftl"
                    );

                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setTitle(new SimpleDateFormat("EEEEE, dd.MM.yyyy").format(date));
                    embed.setImage("attachment://plan.png");

                    event.getMessage().getChannel().sendMessage(embed.build())
                            .addFile(new ByteArrayInputStream(converter.image2ByteArray(week.getTimes())), "plan.png")
                            .queue();
                }catch (Exception e){
                    e.printStackTrace();
                    event.getMessage().getChannel().sendMessage("An error occurred while getting plan ready for u!:exploding_head:").queue();
                }

                break;
            case "!test":
                if(Boolean.parseBoolean(Environment.getVplanDebug())){
                    GyKlHelper.getAlert().run();
                }else {
                    event.getMessage().getChannel().sendMessage("This command is currently disabled!").queue();
                }
                break;
            case "!help":
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Help!");
                eb.addField("Commands", "!smile\n!day <dd.mm.yyyy>", true);
                eb.addField("Function", "Let the bot smile!\nShow the plan of the given day", true);
                event.getChannel().sendMessage(eb.build()).queue();
                break;
            default:{
                event.getMessage().reply("This Command does not exist! :face_with_raised_eyebrow:")
                        .delay(10, TimeUnit.SECONDS)
                        .flatMap(m -> m.delete())
                        .flatMap(m -> event.getMessage().delete())
                        .queue();
            }
        }
    }
    */

}
