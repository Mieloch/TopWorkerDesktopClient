package topWorker.restClient;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import topWorker.restClient.utils.PostPeriodsJob;
import topWorker.restClient.utils.SerializationUtil;

public class App {

    static List<Message> oldMessages;
    static Scheduler scheduler = null;
    static JobDetail postPeriodsJob;
    static SerializationUtil serializationUtil;
    static Message todayMessage;

    public static void main(String[] args) {
       // setOutputStream();
        System.out.println(new Date());
        Properties prop = new Properties();

        try {
            InputStream propInput = new FileInputStream("config.properties");
            prop.load(propInput);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        serializationUtil = new SerializationUtil(prop.getProperty("messagePath"));
        todayMessage = new Message(new Date(), new Date());
        loadOldMessages();

        JobDataMap map = new JobDataMap();
        map.put("oldMessages", oldMessages);
        map.put("todayMessage", todayMessage);
        map.put("username", prop.getProperty("username"));
        map.put("password",  prop.getProperty("password"));
        map.put("target",  prop.getProperty("target"));
        map.put("serializationUtil", serializationUtil);
        postPeriodsJob = PostPeriodsJob.prepareJob(map);

        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("workPeriodPostTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0/5 * * * ?")).build();
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            scheduler.scheduleJob(postPeriodsJob, trigger);

        } catch (SchedulerException e1) {
            todayMessage.setEnd(new Date());
            serializationUtil.writeWorkPeriods(oldMessages);
            e1.printStackTrace();
        }

    }

    private static void loadOldMessages() {
        oldMessages = null;
        oldMessages = serializationUtil.readWorkPeriods();
        if (oldMessages == null) {
            oldMessages = new ArrayList<>();
        }
    }

    private static void setOutputStream() {
        try {
            System.setErr(new PrintStream(
                    new BufferedOutputStream(new FileOutputStream("log.txt", true)),
                    true));
            System.setOut(new PrintStream(
                    new BufferedOutputStream(new FileOutputStream("log.txt", true)),
                    true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
    }

}
