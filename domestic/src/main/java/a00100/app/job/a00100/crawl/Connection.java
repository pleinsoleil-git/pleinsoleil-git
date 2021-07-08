package a00100.app.job.a00100.crawl;

public class Connection implements AutoCloseable {
	static final ThreadLocal<Connection> m_instances = new ThreadLocal<Connection>() {
		@Override
		protected Connection initialValue() {
			return new Connection();
		}
	};

	Connection() {
	}

	public static Connection getInstance() {
		return m_instances.get();
	}

	@Override
	public void close() throws Exception {
		m_instances.remove();
	}
}
