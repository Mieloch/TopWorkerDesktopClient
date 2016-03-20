package topWorker.restClient.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

import topWorker.restClient.Message;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;

public class PostPeriodsJob implements Job {

    public PostPeriodsJob() {

    }

    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        System.out.println("EXECUTING JOB");
        JobDataMap map = arg0.getJobDetail().getJobDataMap();
        Message todayMessage = (Message) map.get("todayMessage");

        List<Message> messages = (List<Message>) map.get("oldMessages");
        SerializationUtil serializationUtil = (SerializationUtil) map.get("serializationUtil");
        todayMessage.setEnd(new Date());
        messages.add(todayMessage);
        WorkTimeClient workTimeClient = new WorkTimeClient(map.getString("target"), map.getString("username"), map.getString("password"));

        Iterator it = messages.iterator();
        while(it.hasNext()) {
            Message msg = (Message) it.next();
            if (workTimeClient.postMessage(msg) == true) {
                it.remove();
            }
        }
        serializationUtil.writeWorkPeriods(messages);

    }

    public static JobDetail prepareJob(JobDataMap map) {

        JobKey jobKey = new JobKey("postPeriodsJob");
        JobDetail postPeriodsJob = JobBuilder.newJob(PostPeriodsJob.class).withIdentity(jobKey).usingJobData(map)
                .build();
        return postPeriodsJob;
    }

}
