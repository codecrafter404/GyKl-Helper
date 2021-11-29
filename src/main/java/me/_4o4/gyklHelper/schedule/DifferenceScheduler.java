package me._4o4.gyklHelper.schedule;

import me._4o4.gyklHelper.GyKlHelper;
import me._4o4.gyklHelper.models.Server;
import me._4o4.gyklHelper.utils.DateAndTimeUtil;
import me._4o4.gyklHelper.utils.HtmlConverter;
import me._4o4.gyklHelper.utils.Database;
import me._4o4.gyklHelper.utils.NetworkUtil;
import me._4o4.vplanwrapper.VPlanAPI;
import me._4o4.vplanwrapper.api.Week;
import me._4o4.vplanwrapper.models.RequestDate;
import net.dv8tion.jda.api.EmbedBuilder;
import org.pmw.tinylog.Logger;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

/**
 * This scheduler checks if the local version of the
 */
public class DifferenceScheduler implements Runnable{

    @Override
    public void run() {
        //Check network connection
        if(!NetworkUtil.isNetworkAvailable()){
            Logger.error("Can't run difference check: Network unavailable!");
            return;
        }

        //Day of week
        List<DayOfWeek> blacklistedDays = List.of(
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY
        );
        if(blacklistedDays.contains(LocalDate.now().getDayOfWeek())) {
            Logger.debug("Skip update");
            return;
        }

        new Database().getAllServers().forEach(
                server -> {
                    if(server.getConfig().getAnnouncement_channel().size() == 0) return;
                    if(server.getConfig().getApi_host().equals("") || server.getConfig().getApi_password().equals("")) return;
                    if(server.getData().getCache() == null) return;

                    //Check if data has changed
                    Week week = getWeek(server);
                    if(week == null) return;
                    if(!week.getDays().get(0).isRequestChanged()) return;


                    try{

                        //Build Embed
                        EmbedBuilder embed = new EmbedBuilder();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        embed.setTitle(new SimpleDateFormat("EEEEE, dd.MM.yyyy", GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getLocal()).format(format.parse(server.getData().getCache().getDate())));
                        embed.setImage("attachment://plan.png");

                        for(String channel : server.getConfig().getAnnouncement_channel()){
                            try{
                                GyKlHelper.getJda().getGuildById(server.getServer_id()).getTextChannelById(channel).sendMessageEmbeds(embed.build())
                                        .addFile(new ByteArrayInputStream(HtmlConverter.image2ByteArray(week.getDays().get(0), week.getTimeTable(), "table.ftl", server)), "plan.png")
                                        .queue();
                            }catch (NullPointerException e){
                                //Channel not exists, Guild has kicked or deleted, checked by announcements and CleanUpService
                                Logger.info("Channel not exists, Guild has kicked or deleted, checked by announcements and CleanUpService");
                            }
                        }

                    }catch(Exception e){
                        Logger.warn(String.format("Image generation failed... Server: %s", server.getServer_name()));
                    }
                    server.getData().getCache().setTimestamp(week.getDays().get(0).getTimestamp());
                    // Update Database here
                    new Database().updateServer(server.getServer_id(), server);
                }
        );
    }

    private Week getWeek(Server server){
        Week result = null;
        try{
            Week week = new VPlanAPI(server.getConfig().getApi_host(), server.getConfig().getApi_password(), true)
                    .getWeek(List.of(new RequestDate(server.getData().getCache().getDate(), Integer.parseInt(server.getData().getCache().getTimestamp()))), server.getConfig().getDefault_class());
            if(week.getError().equals("") && week.getDays().get(0).getError().equals("")) result = week;
        }catch (Exception e){
            Logger.warn(String.format("Can't download Subjects for server '%s'", server.getServer_name()));
            Logger.trace(e);
        }
        return result;
    }

}
