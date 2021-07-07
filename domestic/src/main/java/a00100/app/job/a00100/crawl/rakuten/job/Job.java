package a00100.app.job.a00100.crawl.rakuten.job;

import java.util.ArrayList;
import java.util.Collection;

import a00100.app.job.a00100.crawl.rakuten.job.request.Request;
import a00100.app.job.a00100.crawl.rakuten.job.webBrowser.WebBrowser;
import lombok.Data;
import lombok.val;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = true)
public class Job {
	static Job m_instance;
	_Current m_current;

	public static Job getInstance() {
		return (m_instance == null ? m_instance = new Job() : m_instance);
	}

	public static _Current getCurrent() {
		return getInstance().m_current;
	}

	public void execute() throws Exception {
		try (val browser = WebBrowser.getInstance()) {
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
				add(new _Current() {
					{
						m_webDriver = "chromedriver.91.exe";
					}
				});
			}
		};
	}

	@Data
	public static class _Current {
		Long m_id;
		String m_webDriver;

		void execute() throws Exception {
			request();
		}

		void request() throws Exception {
			Request.getInstance().execute();
		}
	}
}
