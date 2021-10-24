package sample.Service;

import Core.net.NetService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.Email.jobImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.quartz.TriggerBuilder.newTrigger;

public class EmailService {

    private final Logger log= LoggerFactory.getLogger(NetService.class);

    private Scheduler scheduler;

    private SimpleDateFormat dateFormat;


    //开启进程
    public void start(){
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }



    public void setDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public Scheduler getScheduler(){
        return scheduler;
    }

    private SimpleTrigger getTrigger(String date,String content,String email){
        SimpleTrigger trigger=null;
        try {
            trigger=(SimpleTrigger) newTrigger()
                    .startAt(dateFormat.parse(date))
                    .usingJobData("content",content)
                    .usingJobData("email",email)
                    .build();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return trigger;
    }

    private JobDetail getJobDetail(){
        return JobBuilder.newJob(jobImpl.class).build();
    }


    //添加任务
    public void addJob(String date,String content,String email){
        SimpleTrigger trigger=getTrigger(date,content,email);
        JobDetail jobDetail=getJobDetail();
        if(trigger==null){
            return;
        }
        try {
            scheduler.scheduleJob(jobDetail,trigger);
            log.info("add email job successfully");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}

