package a00100.app.job.a00100.crawl.connection;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.dbutils.handlers.BeanListHandler;

import common.jdbc.JDBCConnection;
import common.jdbc.JDBCUtils;
import lombok.val;

public class Connection implements AutoCloseable {
	static Connection m_instance;
	static final ConcurrentHashMap<Long, JDBCConnection> m_connections = new ConcurrentHashMap<Long, JDBCConnection>();

	Connection() {
	}

	public static Connection getInstance() {
		return (m_instance == null ? m_instance = new Connection() : m_instance);
	}

	@Override
	public void close() throws Exception {
		try {
			for (val v : m_connections.values()) {
				v.close();
			}
		} finally {
			m_connections.clear();
			m_instance = null;
		}
	}

	static JDBCConnection getConnection(final String dsn) {
		val k = Thread.currentThread().getId();
		JDBCConnection v = m_connections.get(k);

		if (v == null) {
			m_connections.put(k, v = new JDBCConnection(dsn));
		}

		return v;
	}

	public static class App {
		static JDBCConnection getConnection() {
			return Connection.getConnection(a00100.App.DEFAULT_DSN);
		}

		public static <T> List<T> query(final String sql, final BeanListHandler<T> rs) throws Exception {
			return JDBCUtils.query(getConnection().getConnection(), sql, rs);
		}

		public synchronized static int execute(final String sql, final Collection<Object> params) throws Exception {
			return JDBCUtils.execute(getConnection().getConnection(), sql, params.toArray());
		}

		public synchronized static void commit() throws Exception {
			JDBCUtils.commit(getConnection().getConnection());
		}
	}
}
