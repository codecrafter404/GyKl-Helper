package me._4o4.vplanbot.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import me._4o4.vplanbot.VPlanBot;
import me._4o4.vplanbot.models.Environment;
import me._4o4.vplanbot.models.Subject;
import me._4o4.vplanwrapper.models.Day;
import me._4o4.vplanwrapper.models.StartTimes;
import net.anthavio.phanbedder.Phanbedder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Converter {

    private final Day DAY;
    private final String TEMPLATE;

    public Converter(Day day, String template) {
        this.DAY = day;
        this.TEMPLATE = template;
    }

    public String day2Html(StartTimes times) throws IOException, TemplateException {
        Configuration conf = new Configuration(Configuration.VERSION_2_3_31);
        conf.setDefaultEncoding("UTF-8");
        conf.setLocale(Locale.GERMAN);
        conf.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        conf.setClassForTemplateLoading(VPlanBot.class, "templates");

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
        input.put("info", info);
        input.put("subjects", subjects);

        Template template = conf.getTemplate(TEMPLATE);
        Writer stringWriter = new StringWriter();
        template.process(input, stringWriter);
        return stringWriter.toString();
    }

    public BufferedImage day2Image(StartTimes times) throws TemplateException, IOException, InterruptedException {

        String html = day2Html(times);
        File phantomjs = (Environment.getVplanPath().equals("")) ? Phanbedder.unpack() : new File(Environment.getVplanPath());
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
