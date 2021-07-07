package a00100.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class Job implements org.quartz.Job {
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("@@@@@@@@@@@@@@@@@@");
	}
}
