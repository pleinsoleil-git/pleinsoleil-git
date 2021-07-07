package common.app;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class Job implements org.quartz.Job {
	final App m_app;

	public Job(final App app) {
		m_app = app;
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		new Thread() {
			@Override
			public void run() {
				try {
					m_app.initInstance();
				} catch (Exception e) {
				} finally {
					m_app.exitInstance();
				}
			}
		}.start();
	}
}
