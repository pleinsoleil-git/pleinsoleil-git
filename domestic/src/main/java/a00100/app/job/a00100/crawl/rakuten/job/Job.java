package a00100.app.job.a00100.crawl.rakuten.job;

import java.util.ArrayList;
import java.util.Collection;

import a00100.app.job.a00100.crawl.rakuten.job.request.Request;
import lombok.val;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = true)
public class Job {
	static Job m_instance;
	_Current m_current;

	public static Job getInstance() {
		return (m_instance == null ? m_instance = new Job() : m_instance);
	}

	public void execute() throws Exception {
		try {
			for (val r : query()) {
				(m_current = r).execute();
			}
		} finally {
			m_instance = null;
		}
	}

	Collection<_Current> query() throws Exception {
		return new ArrayList<_Current>() {
			{
				add(new _Current());
			}
		};
	}

	public static class _Current {
		void execute() throws Exception {
			request();
		}

		void request() throws Exception {
			Request.getInstance().execute();
		}
	}
}
