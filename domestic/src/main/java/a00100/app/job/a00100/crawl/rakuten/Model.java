package a00100.app.job.a00100.crawl.rakuten;

import java.util.concurrent.atomic.AtomicBoolean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Model extends a00100.app.job.Model {
	static AtomicBoolean m_executing = new AtomicBoolean();

	public void execute() throws Exception {
		if (isExecutable() == true) {
			try {
				log.info("@@@@@@@@@@@@@");
			} finally {
				m_executing.set(false);
			}
		}
	}

	boolean isExecutable() {
		return m_executing.compareAndSet(false, true);
	}

	void job() throws Exception {
	}
}
