package me._4o4.gyklHelper.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import me._4o4.gyklHelper.GyKlHelper;
import me._4o4.gyklHelper.models.Server;
import me._4o4.gyklHelper.models.Subject;
import me._4o4.vplanwrapper.models.Day;
import me._4o4.vplanwrapper.models.StartTimes;
import net.anthavio.phanbedder.Phanbedder;
import org.pmw.tinylog.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Converter {

    private final Day DAY;
    private final String TEMPLATE;
    private final Server server;

    public Converter(Day day, String template, Server server) {
        this.DAY = day;
        this.TEMPLATE = template;
        this.server = server;
    }

    public String day2Html(StartTimes times) throws IOException, TemplateException {
        Configuration conf = new Configuration(Configuration.VERSION_2_3_31);
        conf.setDefaultEncoding("UTF-8");
        conf.setLocale(Locale.GERMAN);
        conf.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        conf.setClassForTemplateLoading(GyKlHelper.class, "templates");

        List<Subject> subjects = new ArrayList<>();
        String info = (DAY.getInfo() == "") ? "" : DAY.getInfo();

        AtomicInteger index = new AtomicInteger();
        DAY.getSubjects().forEach(
                subject -> {
                subjects.add(

                        new Subject(
                                times.getTime(index.get()),
                                subject.get(0).getName_full(),
                                subject.get(0).getTeacher().get(0).getName(),
                                subject.get(0).getRoom_name(),
                                (subject.get(0).getInfo() == "") ? "-" : subject.get(0).getInfo(),
                                (subject.get(0).isFailure()) ? "failure" : (subject.get(0).isChange()) ? "change" : ""
                        )
                );
                index.getAndIncrement();
                }
        );
        Map<String, Object> input = new HashMap<>();
        input.put("info_title", GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getPlan_info_title());
        input.put("info_text", info);
        input.put("title_time", GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getPlan_title_time());
        input.put("title_subject", GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getPlan_title_subject());
        input.put("title_teacher", GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getPlan_title_teacher());
        input.put("title_room", GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getPlan_title_room());
        input.put("title_info", GyKlHelper.getLanguageManager().getLang(server.getConfig().getLanguage()).getPlan_title_info());
        input.put("subjects", subjects);

        Template template = conf.getTemplate(TEMPLATE);
        Writer stringWriter = new StringWriter();
        template.process(input, stringWriter);
        return stringWriter.toString();
    }

    public BufferedImage day2Image(StartTimes times) throws TemplateException, IOException, InterruptedException {

        String html = day2Html(times);
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

    public byte[] image2ByteArray(StartTimes times) throws TemplateException, IOException, InterruptedException {
        BufferedImage image = day2Image(times);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);
        out.flush();

        return out.toByteArray();
    }
}
