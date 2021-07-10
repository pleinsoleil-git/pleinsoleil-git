package a00100.app.job.a00100.jalan.job.request.process.crawl;

import a00100.app.job.a00100.jalan.job.request.process.crawl.login.Login;
import a00100.app.job.a00100.jalan.job.request.process.webBrowser.WebClient;

public class Crawl {
	static final ThreadLocal<Crawl> m_instances = new ThreadLocal<Crawl>() {
		@Override
		protected Crawl initialValue() {
			return new Crawl();
		}
	};

	Crawl() {
	}

	public static Crawl getInstance() {
		return m_instances.get();
	}

	public void execute() throws Exception {
		try {
			for (WebClient client = Login.getInstance(); client != null;) {
				client = client.execute();
			}
		} finally {
			m_instances.remove();
		}
	}
}
