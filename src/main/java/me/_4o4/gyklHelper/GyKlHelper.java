package me._4o4.gyklHelper;

import me._4o4.gyklHelper.Language.LanguageManager;
import me._4o4.gyklHelper.Listeners.MessageListener;
import me._4o4.gyklHelper.models.Environment;
import me._4o4.gyklHelper.schedule.AlertSchedule;
import me._4o4.gyklHelper.schedule.AnnouncementScheduler;
import me._4o4.gyklHelper.schedule.CleanUpDatabaseSchedule;
import me._4o4.gyklHelper.schedule.DifferenceScheduler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.Logger;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GyKlHelper {
    private static JDA jda = null;
    private static AlertSchedule alert = null;
    private static LanguageManager languageManager = null;
    private static AnnouncementScheduler announcementScheduler = null;
    public static void main(String[] args) throws IOException, LoginException, IllegalAccessException, InterruptedException {
        try{
            TimeZone.setDefault(TimeZone.getTimeZone(Environment.getTIMEZONE()));
        }catch (Exception e){
            Logger.trace(e);
            Logger.error("Can't set configured TimeZone | This can occur wired errors!");
        }

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

        //All here Depends on JDA
        Logger.info("Init announcements");
        announcementScheduler = new AnnouncementScheduler();
        announcementScheduler.scheduleAllDefinedAnnouncements();

        Logger.info("Setup CleanUpService");
        scheduleCleanUp();

        Logger.info("Setup Difference Checker");
        scheduleDifferenceChecker();

        Logger.info("Finish!");

    }

    public static JDA getJda(){
        return jda;
    }

    public static LanguageManager getLanguageManager() {
        return languageManager;
    }

    public static AnnouncementScheduler getAnnouncementScheduler() {
        return announcementScheduler;
    }

    private static void scheduleCleanUp(){
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new CleanUpDatabaseSchedule(), 0, 20, TimeUnit.MINUTES);
    }
    private static void scheduleDifferenceChecker(){
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new DifferenceScheduler(), 0, 2, TimeUnit.MINUTES);
    }
}
