package me._4o4.gyklHelper;

import me._4o4.gyklHelper.Listeners.MessageListener;
import me._4o4.gyklHelper.models.Config;
import me._4o4.gyklHelper.models.Environment;
import me._4o4.gyklHelper.schedule.AlertSchedule;
import me._4o4.gyklHelper.utils.ConfigParse;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GyKlHelper {
    private static JDA jda = null;
    private static AlertSchedule alert = null;
    public static void main(String[] args) throws IOException, LoginException, IllegalAccessException, InterruptedException {

        final String SECRET_DIRECTORY = "/config/secret.json";

        System.out.println("[INFO] We're on " + System.getProperty("os.arch"));
        Config conf= ConfigParse.parseConfig(SECRET_DIRECTORY);
        Environment.parseEnvironments();

        System.out.println(conf.getToken());
        System.out.println(Environment.getVplanHost());

        jda = JDABuilder.createDefault(conf.getToken()).build();
        jda.awaitReady();
        jda.getPresence().setPresence(Activity.playing("Minecraft ;)"), true);

        jda.addEventListener(new MessageListener());

        new GyKlHelper().schedule();

    }

    public static JDA getJda(){
        return jda;
    }

    public static AlertSchedule getAlert() {
        if(alert == null) alert =  new AlertSchedule();
        return alert;
    }

    public void schedule() {
        ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutor.scheduleAtFixedRate(getAlert(), millisToNextHour()
                , 60*60*1000 + 1000, TimeUnit.MILLISECONDS);
        System.out.println("[INFO] Scheduled Announcement in " + millisToNextHour() / 60000 + "m");
    }

    private long millisToNextHour() {
        LocalDateTime nextHour = LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.HOURS);
        return LocalDateTime.now().until(nextHour, ChronoUnit.MILLIS);
    }
}
