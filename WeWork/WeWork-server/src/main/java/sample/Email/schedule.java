package sample.Email;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.concurrent.TimeUnit;

public class schedule {

    public static void main(String[] args) throws SchedulerException, InterruptedException {
        // 1、创建调度器Scheduler
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        // 2、创建JobDetail实例，并与PrintWordsJob类绑定(Job执行内容)
        JobDetail jobDetail = JobBuilder.newJob(jobTest.class).build();
        // 3、构建Trigger实例,每隔1s执行一次
        Trigger trigger = TriggerBuilder.newTrigger()
                .startNow()//立即生效
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(1)//每隔1s执行一次
                        .repeatForever())
                        .usingJobData("content","test").
                        build();//一直执行

        JobDetail jobDetail2 = JobBuilder.newJob(jobTest.class).build();
        //4、执行
        scheduler.scheduleJob(jobDetail, trigger);
        System.out.println("--------scheduler start ! ------------");
        scheduler.start();
        Trigger trigger2 = TriggerBuilder.newTrigger()
                .startNow()//立即生效
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(1)//每隔1s执行一次
                        .repeatForever())
                .usingJobData("content","test2222222").
                        build();//一直执行

        //睡眠
        TimeUnit.SECONDS.sleep(20);
        scheduler.scheduleJob(jobDetail2,trigger2);
        TimeUnit.SECONDS.sleep(10);
        System.out.println("--------scheduler shutdown ! ------------");
        scheduler.shutdown();


    }
}

