package a00100.app.job.a00100.crawl.job.rakuten.request;

public class Request {
	static final ThreadLocal<Request> m_instances = new ThreadLocal<Request>() {
		@Override
		protected Request initialValue() {
			return new Request();
		}
	};

	Request() {
	}

	public static Request getInstance() {
		return m_instances.get();
	}

	public void execute() throws Exception {
		try {
		} finally {
			m_instances.remove();
		}
	}
}
