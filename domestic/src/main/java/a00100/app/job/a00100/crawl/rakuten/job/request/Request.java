package a00100.app.job.a00100.crawl.rakuten.job.request;

import java.util.ArrayList;
import java.util.Collection;

import a00100.app.job.a00100.crawl.rakuten.job.request.query.Query;
import common.webBrowser.WebClient;
import lombok.val;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
public class Request {
	static Request m_instance;
	_Current m_current;

	public static Request getInstance() {
		return (m_instance == null ? m_instance = new Request() : m_instance);
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
			for (WebClient client = Query.getInstance(); client != null;) {
				client = client.execute();
			}
		}
	}
}
