package sample.Email;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import sample.Utils.emailUtils;

public class jobImpl implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String content=(String) context.getTrigger().getJobDataMap().get("content");
        String recipient=(String) context.getTrigger().getJobDataMap().get("email");
        emailUtils.sendEmail(content,recipient);
    }
}
