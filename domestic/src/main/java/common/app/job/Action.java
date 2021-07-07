package common.app.job;

import java.sql.BatchUpdateException;
import java.sql.SQLException;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import common.app.App;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Action implements org.quartz.Job {
	final App m_app;

	public Action(final App app) {
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
					printError(e);
				} finally {
					m_app.exitInstance();
				}
			}
		}.start();
	}

	void printError(final Exception e) {
		if (e instanceof BatchUpdateException) {
			printError((BatchUpdateException) e);
		} else {
			log.error("", e);
		}
	}

	void printError(final BatchUpdateException e) {
		SQLException ex = e.getNextException();

		while (ex != null) {
			log.error("", e);
			ex = ex.getNextException();
		}
	}
}
