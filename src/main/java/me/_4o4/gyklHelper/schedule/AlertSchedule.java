package me._4o4.gyklHelper.schedule;

import me._4o4.gyklHelper.GyKlHelper;
import me._4o4.gyklHelper.models.CachedEntity;
import me._4o4.gyklHelper.models.Server;
import me._4o4.gyklHelper.utils.*;
import me._4o4.vplanwrapper.VPlanAPI;
import me._4o4.vplanwrapper.api.Week;
import me._4o4.vplanwrapper.models.RequestDate;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.pmw.tinylog.Logger;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * This alert sends the plan for the next day when its triggered
 */
public class AlertSchedule implements Runnable {

    private Server server;

    public AlertSchedule(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        //Check network connection
        if(!NetworkUtil.isNetworkAvailable()){
            Logger.error("Can't run alert: Network unavailable!");
            return;
        }

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
        Week week;
        try{
            week = new VPlanAPI(server.getConfig().getApi_host(), server.getConfig().getApi_password(), true).getWeek(
                    List.of(new RequestDate(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), 0)),
                    server.getConfig().getDefault_class()
            );
        }catch (Exception e){
            Logger.error(String.format("Error while fetch data;\n Server: %s\nDate: %s\nHost: %s", server.getServer_name(), new SimpleDateFormat(
                    "EEEEE, dd.MM.yyyy",
                    GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getLocal()
            ).format(java.sql.Date.valueOf(date)), server.getConfig().getApi_host()));
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
        //Add to Cache
        server.getData().setCache(new CachedEntity(week.getDays().get(0).getTimestamp(), new SimpleDateFormat("yyyy-MM-dd").format(java.sql.Date.valueOf(date))));
        try{
            new Database().updateServer(server.getServer_id(), server);
        }catch (Exception e){
            Logger.error("Can't update cache!");
            Logger.trace(e);
        }

        //Try to send Image,
        try{
            //Build Embed
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle(new SimpleDateFormat("EEEEE, dd.MM.yyyy",GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getLocal()).format(java.sql.Date.valueOf(date)));
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
                        .addFile(new ByteArrayInputStream(HtmlConverter.image2ByteArray(week.getDays().get(0), week.getTimeTable(), "table.ftl", server)), "plan.png")
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
