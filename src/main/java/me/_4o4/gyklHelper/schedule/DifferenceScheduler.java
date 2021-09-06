package me._4o4.gyklHelper.schedule;

import me._4o4.gyklHelper.GyKlHelper;
import me._4o4.gyklHelper.models.Server;
import me._4o4.gyklHelper.utils.Converter;
import me._4o4.gyklHelper.utils.Database;
import me._4o4.gyklHelper.utils.DifferenceDatabase;
import me._4o4.vplanwrapper.VPlanAPI;
import me._4o4.vplanwrapper.models.Subject;
import me._4o4.vplanwrapper.models.Week;
import net.dv8tion.jda.api.EmbedBuilder;
import org.pmw.tinylog.Logger;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class DifferenceScheduler implements Runnable{

    @Override
    public void run() {
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
                    //Check recently checked

                    if(
                            DifferenceDatabase.getDates().containsKey(server.getServer_id()) &&
                            DifferenceDatabase.getDates().get(server.getServer_id()).until(LocalDateTime.now(), ChronoUnit.MINUTES)
                            < 5

                    ) return;
                    Week currentWeek = getWeek(server);
                    if(currentWeek == null){
                        return;
                    }
                    List<List<Subject>> oldSubjects = DifferenceDatabase.getData().containsKey(server.getServer_id())
                            ?
                            DifferenceDatabase.getData().get(server.getServer_id())
                            :
                            new ArrayList<>();
                    if(compare(oldSubjects, currentWeek.getDays().get(0).getSubjects())){
                        return;
                    }else {
                        DifferenceDatabase.update(server.getServer_id(), currentWeek.getDays().get(0).getSubjects());
                    }

                    try{
                        Converter converter = new Converter(
                                currentWeek.getDays().get(0),
                                "table.ftl",
                                server
                        );

                        //Build Embed
                        EmbedBuilder embed = new EmbedBuilder();
                        embed.setTitle(new SimpleDateFormat("EEEEE, dd.MM.yyyy", GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getLocal()).format(java.sql.Date.valueOf(LocalDate.now().plusDays(1))));
                        embed.setImage("attachment://plan.png");

                        for(String channel : server.getConfig().getAnnouncement_channel()){
                            try{
                                GyKlHelper.getJda().getGuildById(server.getServer_id()).getTextChannelById(channel).sendMessageEmbeds(embed.build())
                                        .addFile(new ByteArrayInputStream(converter.image2ByteArray(currentWeek.getTimes())), "plan.png")
                                        .queue();
                            }catch (Exception e){
                                //Channel not exists, Guild has kicked or deleted, checked by announcements and CleanUpService
                                Logger.debug("Wrong Channel");
                                continue;
                            }
                        }

                    }catch(Exception e){
                        Logger.warn(String.format("Image generation failed... Server: %s", server.getServer_name()));
                    }
                }
        );
    }

    private Week getWeek(Server server){
        Week result = null;
        try{
            Week week = new VPlanAPI(server.getConfig().getApi_host(), server.getConfig().getApi_password())
                    .getWeek(List.of(new SimpleDateFormat("yyyy-MM-dd").format(java.sql.Date.valueOf(LocalDate.now().plusDays(1)))), server.getConfig().getDefault_class());
            if(week.getError().equals("") && week.getDays().get(0).getError().equals("")) result = week;
        }catch (Exception e){
            Logger.warn(String.format("Can't download Subjects for server '%s'", server.getServer_name()));
            Logger.trace(e);
        }
        return result;
    }

    private boolean compare(List<List<Subject>> old, List<List<Subject>> current){
        if(old.size() != current.size()) return false;
        int index = 0;
        for(List<Subject> subject : current){
            try{
                if(!subject.get(0).getName_full().equalsIgnoreCase(current.get(index).get(0).getName_full())) return false;
                if(!subject.get(0).getInfo().equalsIgnoreCase(current.get(index).get(0).getInfo())) return false;
                if(!subject.get(0).getRoom_name().equalsIgnoreCase(current.get(index).get(0).getRoom_name())) return false;
                if(!subject.get(0).getTeacher().get(0).getName().equalsIgnoreCase(current.get(index).get(0).getTeacher().get(0).getName())) return false;
                index++;
            }catch (Exception e){continue;}
        }
        return true;
    }
}
