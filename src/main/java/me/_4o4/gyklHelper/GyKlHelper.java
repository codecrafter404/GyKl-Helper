package me._4o4.gyklHelper;

import com.google.gson.Gson;
import me._4o4.gyklHelper.Language.LanguageManager;
import me._4o4.gyklHelper.Listeners.MessageListener;
import me._4o4.gyklHelper.models.Environment;
import me._4o4.gyklHelper.models.Server;
import me._4o4.gyklHelper.models.ServerConfig;
import me._4o4.gyklHelper.models.ServerData;
import me._4o4.gyklHelper.schedule.AlertSchedule;
import me._4o4.gyklHelper.utils.Database;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.Logger;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GyKlHelper {
    private static JDA jda = null;
    private static AlertSchedule alert = null;
    private static LanguageManager languageManager = null;
    public static void main(String[] args) throws IOException, LoginException, IllegalAccessException, InterruptedException {

        Configurator.defaultConfig()
                .level(Level.TRACE)
                .formatPattern("{date:yyyy-MM-dd HH:mm:ss} [{thread}] {class}.{method} | {level} |    {message}")
                .activate();


        Logger.debug("We're on [" + System.getProperty("os.arch") + "]");

        Logger.info("Read environments");
        Environment.parseEnvironments();

        Logger.info("Load languages");
        languageManager = new LanguageManager();

        Logger.info("Start JDA");
        jda = JDABuilder.createDefault(Environment.getDiscordToken()).build();
        Logger.info("Wait for Bot");
        jda.awaitReady();
        jda.getPresence().setPresence(OnlineStatus.DO_NOT_DISTURB,Activity.watching("Sword Art Online"), false);
        Logger.info("Successfully logged in");
        Logger.info("Setup MessageListener");
        jda.addEventListener(new MessageListener());
        Logger.info("Finish!");

    }

    public static JDA getJda(){
        return jda;
    }

    public static LanguageManager getLanguageManager() {
        return languageManager;
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
