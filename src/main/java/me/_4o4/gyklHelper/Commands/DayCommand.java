package me._4o4.gyklHelper.Commands;

import me._4o4.gyklHelper.GyKlHelper;
import me._4o4.gyklHelper.models.Server;
import me._4o4.gyklHelper.utils.HtmlConverter;
import me._4o4.gyklHelper.utils.DateAndTimeUtil;
import me._4o4.gyklHelper.utils.ErrorMessage;
import me._4o4.vplanwrapper.VPlanAPI;

import me._4o4.vplanwrapper.api.Week;
import me._4o4.vplanwrapper.models.RequestDate;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.pmw.tinylog.Logger;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class DayCommand implements Command{

    @Override
    public void run(String[] args, MessageReceivedEvent event, Server server) {
        //Check if server login dates are initialised
        boolean init = !"".equals(server.getConfig().getApi_host()) && !"".equals(server.getConfig().getApi_password());
        if(!init){
            event.getChannel().sendMessage(GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getCredentialsNotSet()).queue();
            return;
        }
        //Check arguments
        if(args.length > 2){
            event.getChannel().sendMessage(
                    GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getUsage_String()
                    + getUsage(server)
            ).queue();
            return;
        }
        Date date = args.length == 1 ? java.sql.Date.valueOf(LocalDate.now()) : DateAndTimeUtil.getDate(args[1]);
        if(date == null){
            event.getChannel().sendMessage(
                    GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getUsage_String()
                            + getUsage(server)
            ).queue();
            return;
        }
        Week week;
        try{

            week = new VPlanAPI(server.getConfig().getApi_host(), server.getConfig().getApi_password(), true).getWeek(
                    List.of(new RequestDate(new SimpleDateFormat("yyyy-MM-dd").format(date), 0)),
                    server.getConfig().getDefault_class()
            );

        }catch (Exception e){
            Logger.error(String.format("Error while fetch data;\n Server: %s\nDate: %s\nHost: %s", server.getServer_name(), new SimpleDateFormat(
                    "EEEEE, dd.MM.yyyy",
                    GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getLocal()
            ).format(date), server.getConfig().getApi_host()));
            Logger.trace(e);
            event.getChannel().sendMessage(GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getDay_API_Not_reachable()).queue();
            return;
        }


        if(!week.getError().equals("")){
            //API error, e.g. wrong password
            event.getChannel().sendMessage(ErrorMessage.getErrorMessageForError(server.getConfig().getLanguage(), week.getError())).queue();
            return;
        }

        if(!week.getDays().get(0).getError().equals("")){
            //Error in day, e.g. invalid date
            event.getChannel().sendMessage(ErrorMessage.getErrorMessageForError(server.getConfig().getLanguage(), week.getDays().get(0).getError())).queue();
            return;
        }

        //Message, because it can take some time
        event.getChannel().sendMessage(GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getDay_Ready()).queue();
        event.getChannel().sendTyping().queue();

        //Try to send Image, if fails send Fallback
        try{

            //Build Embed
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle(new SimpleDateFormat("EEEEE, dd.MM.yyyy",GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getLocal()).format(date));
            embed.setImage("attachment://plan.png");

            event.getChannel().sendMessageEmbeds(embed.build())
                    .addFile(new ByteArrayInputStream(HtmlConverter.image2ByteArray(week.getDays().get(0), week.getTimeTable(), "table.ftl", server)), "plan.png")
                    .queue();

        }catch(Exception e){
            Logger.warn("The image generation failed");
            Logger.trace(e);
            event.getChannel().sendMessage(GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getDay_Image_failed()).queue();
        }
    }

    @Override
    public List<String> getTriggers() {
        List<String> triggers = new ArrayList<>();

        GyKlHelper.getLanguageManager().getAllLanguages().forEach(
                l -> triggers.addAll(l.getDay_Triggers()));
        return triggers;
    }

    @Override
    public String getName() {
        return "Day";
    }

    @Override
    public String getDescription(Server server) {
        return GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getDay_Description();
    }

    @Override
    public String getUsage(Server server) {
        return GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getDay_Usage();
    }
}
