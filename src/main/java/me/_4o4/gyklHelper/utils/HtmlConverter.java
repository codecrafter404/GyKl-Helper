package me._4o4.gyklHelper.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import me._4o4.gyklHelper.GyKlHelper;
import me._4o4.gyklHelper.models.Server;
import me._4o4.gyklHelper.models.Subject;
import me._4o4.vplanwrapper.api.Day;
import me._4o4.vplanwrapper.api.Group;
import net.anthavio.phanbedder.Phanbedder;
import org.pmw.tinylog.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HtmlConverter {


    /**
     * This method generate html from a day object using freemarker and a template
     *
     * @param day the day object to create from
     * @param timeTable List containing the start times of every hour
     * @param templateFile path to the template file
     * @param server server object to get translations
     * @return String containing the generated HTML-Code
     * @throws IOException error
     * @throws TemplateException error
     */
    private static String day2Html(Day day, List<LocalTime> timeTable, String templateFile, Server server) throws IOException, TemplateException {
        if(
                timeTable == null ||
                day == null ||
                templateFile == null ||
                server == null
        ) throw new IllegalArgumentException();

        // Build configuration
        Configuration conf = new Configuration(Configuration.VERSION_2_3_31);
        conf.setDefaultEncoding("UTF-8");
        conf.setLocale(Locale.GERMAN);
        conf.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        conf.setClassForTemplateLoading(GyKlHelper.class, "templates");

        // Convert into hashmap
        String info = day.getInfo().equals("") ? "" : day.getInfo();
        List<Subject> subjects = new ArrayList<>();

        // Genrate custom subject objects -> needed for templating
        int index = 0;
        for(me._4o4.vplanwrapper.api.Subject subject : day.getSubjects()){

            if(subject == null){
                subjects.add(
                        new Subject(
                        timeTable.get(index).format(DateTimeFormatter.ofPattern("HH:mm")),
                        "-",
                        "-",
                        "-",
                        "-",
                        ""
                        ))
                ;
                index += 1;
                continue;
            }
            subjects.add(
                    new Subject(
                            timeTable.get(index).format(DateTimeFormatter.ofPattern("HH:mm")),
                            subject.getName(),
                            subject.isMultiGroup() ? "-" : subject.getGroups().get(0).getTeacher().getName(),
                            subject.isMultiGroup() ? "-" : subject.getGroups().get(0).getRoom(),
                            subject.isMultiGroup() ? "-" : subject.getInfo(),
                            getStatusOfSubject(subject)
                    )
            );
            index += 1;

        }

        // Fill map with inputs
        Map<String, Object> input = new HashMap<>();
        input.put("info_title", GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getPlan_info_title());
        input.put("info_text", info);
        input.put("title_time", GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getPlan_title_time());
        input.put("title_subject", GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getPlan_title_subject());
        input.put("title_teacher", GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getPlan_title_teacher());
        input.put("title_room", GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getPlan_title_room());
        input.put("title_info", GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getPlan_title_info());
        input.put("subjects", subjects);

        // Convert and process output
        Template template = conf.getTemplate(templateFile);
        Writer stringWriter = new StringWriter();
        template.process(input, stringWriter);
        return stringWriter.toString();
    }

    /**
     * this method creates a png image, from a day
     *
     * @param day the day object to create from
     * @param timeTable List containing the start times of every hour
     * @param templateFile path to the template file
     * @param server server object to get translations
     * @return a buffered image
     * @throws IOException error
     * @throws TemplateException error
     * @throws InterruptedException interrupt while read from console
     */
    public static BufferedImage day2Image(Day day, List<LocalTime> timeTable, String templateFile, Server server) throws TemplateException, IOException, InterruptedException {

        String html = day2Html(day, timeTable, templateFile, server);
        File phantomjs = (System.getProperty("os.arch").equals("amd64")) ? Phanbedder.unpack() : new File("/tmp/arm/phantomjs");
        Logger.info("Extracted PhantomJS to '" + phantomjs.getAbsolutePath() + "'");
        ResourceUtil.extractResource(
                "/convert.js",
                System.getProperty("user.dir") + File.separator + "convert.js"
        );


        //Start PhantomJS
        ProcessBuilder proc_builder = new ProcessBuilder(
                phantomjs.getAbsolutePath(),
                "convert.js"
        );
        proc_builder.directory(new File(System.getProperty("user.dir")));
        Process process = proc_builder.start();

        //Write HTML
        OutputStream os = process.getOutputStream();
        html = html.replace("\n", "");
        os.write((html + "\n").getBytes(StandardCharsets.UTF_8));
        os.flush();
        os.close();

        //Read Base64
        InputStreamReader isr = new InputStreamReader(process.getInputStream());
        BufferedReader rdr = new BufferedReader(isr);

        String base64IMG = "";

        String line = "";
        while((line = rdr.readLine()) != null) {
            base64IMG = line;
        }
        isr = new InputStreamReader(process.getErrorStream());
        rdr = new BufferedReader(isr);
        while((line = rdr.readLine()) != null) {
            System.out.println(line);
        }
        isr.close();
        rdr.close();
        process.waitFor();
        return ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(base64IMG)));
    }


    /**
     * this function returns a byte[] containing a png image generated by a Day object and a timetable
     *
     * @param day the day object to create from
     * @param timeTable List containing the start times of every hour
     * @param templateFile path to the template file
     * @param server server object to get translations
     * @return a byte array
     * @throws IOException error
     * @throws TemplateException error
     */
    public static byte[] image2ByteArray(Day day, List<LocalTime> timeTable, String templateFile, Server server) throws TemplateException, IOException, InterruptedException {
        BufferedImage image = day2Image(day, timeTable, templateFile, server);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);
        out.flush();

        return out.toByteArray();
    }

    /**
     * This method merge the groups status
     *
     * @param subject subject to get status from
     * @return the status possible: "failure", "change", ""
     */
    private static String getStatusOfSubject(me._4o4.vplanwrapper.api.Subject subject){
        boolean failure = false;
        boolean change = false;

        switch (subject.getGroups().size()){
            case 1:
                failure = subject.getGroups().get(0).getState().isFailure();
                change = subject.getGroups().get(0).getState().isChange();
                break;
            case 2:
                failure = subject.getGroups().get(0).getState().isFailure() && subject.getGroups().get(1).getState().isFailure();
                change = subject.getGroups().get(0).getState().isChange() || subject.getGroups().get(1).getState().isChange();
                break;
            default:
                for(Group group : subject.getGroups()){
                    if(group.getState().isChange() || group.getState().isFailure()) change = true;
                }
        }
        return failure ? "failure" : change ? "change" : "";
    }
}
