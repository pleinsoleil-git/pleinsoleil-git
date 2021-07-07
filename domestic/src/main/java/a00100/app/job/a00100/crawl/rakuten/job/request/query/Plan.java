package a00100.app.job.a00100.crawl.rakuten.job.request.query;

import a00100.app.job.a00100.crawl.rakuten.job.webBrowser.WebClient;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = true)
class Plan extends WebClient {
	static final ThreadLocal<Plan> m_instances = new ThreadLocal<Plan>() {
		@Override
		protected Plan initialValue() {
			return new Plan();
		}
	};
	_Current m_current;

	Plan() {
	}

	static Plan getInstance() {
		return m_instances.get();
	}

	static _Current getCurrent() {
		return getInstance().m_current;
	}

	public WebClient execute() throws Exception {
		try {
			for (m_current = new _00000(); m_current != null;) {
				m_current = (_Current) m_current.execute();
			}

			return null;
		} finally {
			m_instances.remove();
		}
	}

	static class _Current extends WebClient {
	}

	static class _00000 extends _Current {
	}
}
