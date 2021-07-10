package a00100.app.job.a00100.rakuten;

import java.util.concurrent.atomic.AtomicBoolean;

import a00100.app.job.a00100.crawl.job.Job;

public class Model extends a00100.app.job.Model {
	static AtomicBoolean m_running = new AtomicBoolean();

	public void execute() throws Exception {
		if (isExecutable() == true) {
			try {
			} finally {
				m_running.set(false);
			}
		}
	}

	boolean isExecutable() {
		return m_running.compareAndSet(false, true);
	}

	void job() throws Exception {
		Job.getInstance().execute();
	}
}
