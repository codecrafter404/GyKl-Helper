package me._4o4.gyklHelper.schedule;

import freemarker.template.TemplateException;
import me._4o4.gyklHelper.GyKlHelper;
import me._4o4.gyklHelper.models.Environment;
import me._4o4.gyklHelper.utils.Converter;
import me._4o4.vplanwrapper.VPlanAPI;
import me._4o4.vplanwrapper.models.Day;
import me._4o4.vplanwrapper.models.StartTimes;
import me._4o4.vplanwrapper.models.Subject;
import me._4o4.vplanwrapper.models.Week;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class AlertSchedule implements Runnable {

    List<List<Subject>> lastUpdatedWeek = new ArrayList<>();

    MessageChannel channel = GyKlHelper.getJda().getTextChannelsByName(Environment.getVplanAnnounce(), true).get(0);

    @Override
    public void run() {
        try{
            Week week;

            //Hour == 16
            Date date = java.sql.Date.valueOf(LocalDate.now());
            DayOfWeek dayOfWeek = DayOfWeek.from(LocalDate.now());
            if(LocalTime.now().getHour() == Integer.parseInt(Environment.getVplanTime())){
                System.out.println("Its 16? " + LocalTime.now().getHour());
                //Ignore 5,6
                if(dayOfWeek.getValue() == 5 || dayOfWeek.getValue() == 6) return;
                //Announce
                week = new VPlanAPI(
                        Environment.getVplanHost(),
                        Environment.getVplanPassword()
                ).getWeek(
                        Arrays.asList(new SimpleDateFormat("yyyy-MM-dd").format(java.sql.Date.valueOf(LocalDate.now().plusDays(1)))),
                                Environment.getVplanClass()
                );
                if(week.getDays().get(0).getError() != ""){
                    System.out.println("[ERROR] while generate week: " + week.getDays().get(0).getError());
                    channel.sendMessage("An error occurred!:exploding_head:\nPlease check the plan manually!!!").queue();
                    return;
                }

                //Save for compare
                lastUpdatedWeek = week.getDays().get(0).getSubjects();
                //Send Embed
                sendDay(week.getTimes(), week.getDays().get(0), java.sql.Date.valueOf(LocalDate.now().plusDays(1)));

            }else {
                System.out.println("Checking Updated!");
                //Ignore 6,7
                //Check Updated
                if(dayOfWeek.getValue() == 6 || dayOfWeek.getValue() == 7) return;

                if(LocalTime.now().getHour() > Integer.parseInt(Environment.getVplanTime()) && !(dayOfWeek.getValue() == 5)){
                    date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
                }

                week = new VPlanAPI(
                        Environment.getVplanHost(),
                        Environment.getVplanPassword()
                ).getWeek(
                        Arrays.asList(new SimpleDateFormat("yyyy-MM-dd").format(date)),
                        Environment.getVplanClass()
                );
                if(week.getDays().get(0).getError() != ""){
                    System.out.println("[ERROR] while generate week: " + week.getDays().get(0).getError());
                    channel.sendMessage("An error occurred!:exploding_head:\nPlease check the plan manually!!!").queue();
                    return;
                }

                boolean isDiff = false;

                if(lastUpdatedWeek.size() == week.getDays().get(0).getSubjects().size()) {
                    for (int i = 0; i < lastUpdatedWeek.size(); i++) {
                        isDiff = !lastUpdatedWeek.get(i).get(0).getName_full().equals(week.getDays().get(0).getSubjects().get(i).get(0).getName_full());
                        if(!isDiff) isDiff = !lastUpdatedWeek.get(i).get(0).getRoom_name().equals(week.getDays().get(0).getSubjects().get(i).get(0).getRoom_name());
                        if(!isDiff) isDiff = !lastUpdatedWeek.get(i).get(0).getTeacher().get(0).getName().equals(week.getDays().get(0).getSubjects().get(i).get(0).getTeacher().get(0).getName());
                    }
                }else {
                    isDiff = true;
                }
                if(isDiff){
                    lastUpdatedWeek = week.getDays().get(0).getSubjects();
                    sendDay(week.getTimes(), week.getDays().get(0), date);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            channel.sendMessage("An error occurred!:exploding_head:\nPlease check the plan manually!!!").queue();
            return;
        }
    }


    public void sendDay(StartTimes times, Day day, Date dateToSend) throws TemplateException, IOException, InterruptedException {
        Converter converter = new Converter(
                day,
                "table.ftl"
        );

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(new SimpleDateFormat("EEEEE, dd.MM.yyyy").format(dateToSend));
        embed.setImage("attachment://plan.png");

        GyKlHelper.getJda().getTextChannelsByName(Environment.getVplanAnnounce(), true).get(0).sendMessage(embed.build())
                .addFile(new ByteArrayInputStream(converter.image2ByteArray(times)), "plan.png")
                .queue();
    }
}
