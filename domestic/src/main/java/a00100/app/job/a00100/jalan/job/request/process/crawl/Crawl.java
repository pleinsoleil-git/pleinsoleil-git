package a00100.app.job.a00100.jalan.job.request.process.crawl;

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
/*
			for (WebClient client = Login.getInstance(); client != null;) {
				client = client.execute();
			}
*/
		} finally {
			m_instances.remove();
		}
	}
}
