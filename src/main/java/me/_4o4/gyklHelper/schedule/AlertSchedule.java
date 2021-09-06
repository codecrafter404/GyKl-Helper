package me._4o4.gyklHelper.schedule;

import me._4o4.gyklHelper.GyKlHelper;
import me._4o4.gyklHelper.models.Server;
import me._4o4.gyklHelper.utils.Converter;
import me._4o4.gyklHelper.utils.Database;
import me._4o4.gyklHelper.utils.DifferenceDatabase;
import me._4o4.gyklHelper.utils.ErrorMessage;
import me._4o4.vplanwrapper.VPlanAPI;
import me._4o4.vplanwrapper.models.Week;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.pmw.tinylog.Logger;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class AlertSchedule implements Runnable {

    private Server server;

    public AlertSchedule(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        Server currentServer = new Database().getServerFromID(server.getServer_id());
        if(currentServer == null) return;
        server = currentServer;
        //Credentials
        if(server.getConfig().getApi_host().equals("") || server.getConfig().getApi_password().equals("")) return;

        //Day of week
        List<DayOfWeek> blacklistedDays = List.of(
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY
        );
        if(blacklistedDays.contains(LocalDate.now().getDayOfWeek())){
            Logger.debug(String.format("Skip Task: %s(%s)",
                        server.getServer_name(),
                    LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
                    ));
            return;
        }

        LocalDate date = LocalDate.now().plusDays(1);

        //Send Plan
        Week week =  null;
        try{
            week = new VPlanAPI(server.getConfig().getApi_host(), server.getConfig().getApi_password()).getWeek(
                    Arrays.asList(new SimpleDateFormat("yyyy-MM-dd").format(date)),
                    server.getConfig().getDefault_class()
            );
        }catch (Exception e){
            Logger.error(String.format("Error while fetch data;\n Server: %s\nDate: %s\nHost: %s", server.getServer_name(), new SimpleDateFormat(
                    "EEEEE, dd.MM.yyyy",
                    GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getLocal()
            ).format(date), server.getConfig().getApi_host()));
            Logger.trace(e);
           sendMessageToAnnouncement(GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getDay_API_Not_reachable());
            return;
        }


        if(!week.getError().equals("")){
            //API error, e.g. wrong password
            sendMessageToAnnouncement(ErrorMessage.getErrorMessageForError(server.getConfig().getLanguage(), week.getError()));
            return;
        }

        if(!week.getDays().get(0).getError().equals("")){
            //Error in day, e.g. invalid date
            sendMessageToAnnouncement(ErrorMessage.getErrorMessageForError(server.getConfig().getLanguage(), week.getDays().get(0).getError()));
            return;
        }
        //Add to Difference Cache
        DifferenceDatabase.update(server.getServer_id(), week.getDays().get(0).getSubjects());
        DifferenceDatabase.updateDate(server.getServer_id(), LocalDateTime.now());

        //Try to send Image,
        try{
            Converter converter = new Converter(
                    week.getDays().get(0),
                    "table.ftl",
                    server
            );

            //Build Embed
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle(new SimpleDateFormat("EEEEE, dd.MM.yyyy",GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getLocal()).format(date));
            embed.setImage("attachment://plan.png");

            for(String channelID : server.getConfig().getAnnouncement_channel()){
                Guild guild = GyKlHelper.getJda().
                        getGuildById(server.getServer_id());
                if(guild == null) return;
                TextChannel channel = guild.getTextChannelById(channelID);
                if(channel == null){
                    server.getConfig().getAnnouncement_channel().remove(channelID);
                    return;
                }
                channel.sendMessageEmbeds(embed.build())
                        .addFile(new ByteArrayInputStream(converter.image2ByteArray(week.getTimes())), "plan.png")
                        .queue();
            }


        }catch(Exception e){
            Logger.warn("The image generation failed");
            Logger.trace(e);
            sendMessageToAnnouncement(GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getDay_Image_failed());
        }
    }

    private void sendMessageToAnnouncement(String message){
        server.getConfig().getAnnouncement_channel().forEach(
                channelID -> {
                    Guild guild = GyKlHelper.getJda().
                            getGuildById(server.getServer_id());
                    if(guild == null) return;
                    TextChannel channel = guild.getTextChannelById(channelID);
                    if(channel == null){
                        server.getConfig().getAnnouncement_channel().remove(channelID);
                        return;
                    }
                    channel.sendMessage(message).queue();
                }
            );

    }
}
