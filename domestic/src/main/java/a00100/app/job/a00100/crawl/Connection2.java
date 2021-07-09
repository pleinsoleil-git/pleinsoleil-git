package a00100.app.job.a00100.crawl;

import java.util.concurrent.ConcurrentHashMap;

import common.jdbc.JDBCConnection;

public class Connection2 implements AutoCloseable {
	static final ConcurrentHashMap<Long, _Current> m_currents = new ConcurrentHashMap<Long, _Current>();

	@Override
	public void close() throws Exception {
	}

	public static class _Current implements AutoCloseable {
		JDBCConnection m_connection;

		@Override
		public void close() throws Exception {
			if (m_connection != null) {
				m_connection.close();
			}
		}
	}

	public static class App {

	}
}
