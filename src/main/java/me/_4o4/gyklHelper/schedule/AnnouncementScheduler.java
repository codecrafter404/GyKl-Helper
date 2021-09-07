package me._4o4.gyklHelper.schedule;

import me._4o4.gyklHelper.models.AnnouncementTask;
import me._4o4.gyklHelper.models.Server;
import me._4o4.gyklHelper.utils.Database;
import me._4o4.gyklHelper.utils.DateAndTimeUtil;
import org.pmw.tinylog.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class AnnouncementScheduler {

    List<AnnouncementTask> tasks = new ArrayList<>();

    public boolean schedule(Server server, String time){
        if(getTask(server, time) != null){
            Logger.warn(String.format("Can't schedule announcement at %s for %s: The task already exists.",
                        time,
                        server.getServer_name()
                    ));
            return false;
        }
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        ScheduledFuture<?> handle = service.scheduleAtFixedRate(
            new AlertSchedule(server),
            millisToTime(time),
            1000 * 60 * 60 * 24,
                TimeUnit.MILLISECONDS
        );
        tasks.add(
                new AnnouncementTask(
                        server.getServer_id(),
                        handle,
                        time
                )
        );
        return true;
    }

    public boolean cancel(Server server, String time){
        if(getTask(server, time) == null){
            Logger.warn(String.format("Can't delete announcement at %s for %s: The task doesn't exists!",
                    time,
                    server.getServer_name()
                    ));
            return false;
        }
        AnnouncementTask task = getTask(server, time);
        task.getService().cancel(false);
        tasks.remove(task);
        return true;
    }

    public void cancelAllFromServer(Server server){
        tasks.forEach(
                x ->{
                    if(x.getServerID().equalsIgnoreCase(server.getServer_id())){
                        tasks.remove(x);
                    }
                }
        );
    }

    public void scheduleAllDefinedAnnouncements(){
        new Database().getAllServers().forEach(
                server -> server.getConfig().getAnnouncement_time().forEach(
                        time -> schedule(server, time)
                )
        );
    }

    private AnnouncementTask getTask(Server server, String time){
        AnnouncementTask task = null;
        for(AnnouncementTask t : tasks){
            if(t.getServerID().equalsIgnoreCase(server.getServer_id())){
                if(t.getTime().equalsIgnoreCase(time)) task = t;
            }
        }
        return task;
    }

    private long millisToTime(String time){
        LocalTime t = DateAndTimeUtil.parseTime(time);
        if(t == null){
            return 0L;
        }
        LocalDateTime date;
        if(LocalTime.now().until(t, ChronoUnit.MILLIS) > 1000){
            //This day
            date = t.atDate(LocalDate.now());
        }else {
            // Next day
            date = t.atDate(LocalDate.now().plusDays(1));
        }
        return LocalDateTime.now().until(date, ChronoUnit.MILLIS);
    }
}
