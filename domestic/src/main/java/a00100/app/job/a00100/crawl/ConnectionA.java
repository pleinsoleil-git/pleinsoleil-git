package a00100.app.job.a00100.crawl;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.dbutils.handlers.BeanListHandler;

import common.jdbc.JDBCConnection;
import common.jdbc.JDBCUtils;
import lombok.val;

public class ConnectionA implements AutoCloseable {
	static final ThreadLocal<ConnectionA> m_instances = new ThreadLocal<ConnectionA>() {
		@Override
		protected ConnectionA initialValue() {
			return new ConnectionA();
		}
	};
	static final ConcurrentHashMap<Long, _Current> m_currents = new ConcurrentHashMap<Long, _Current>();
	static final ConcurrentHashMap<Long, JDBCConnection> m_connections = new ConcurrentHashMap<Long, JDBCConnection>();

	ConnectionA() {
	}

	public static ConnectionA getInstance() {
		return m_instances.get();
	}

	public static _Current getCurrent() {
		val k = Thread.currentThread().getId();
		_Current v = m_currents.get(k);

		if (v == null) {
			m_currents.put(k, v = new _Current());
		}

		return v;
	}

	@Override
	public void close() throws Exception {
		try {
			for (val v : m_currents.values()) {
				v.close();
			}
		} finally {
			m_currents.clear();
			m_instances.remove();
		}
	}

	public static class _Current implements AutoCloseable {
		JDBCConnection m_default;

		public JDBCConnection getDefault() throws Exception {
			return (m_default == null ? m_default = new JDBCConnection(a00100.app.job.a00100.crawl.App.DEFAULT_DSN) : m_default);
		}

		@Override
		public void close() throws Exception {
			if (m_default != null) {
				m_default.close();
			}
		}
	}

	public static class App {
		static Boolean m_sync = true;
		static JDBCConnection m_connection;

		static JDBCConnection getConnection() {
			return (m_connection == null ? m_connection = new JDBCConnection(a00100.App.DEFAULT_DSN) : m_connection);
		}

		public static <T> List<T> query(final String sql, final BeanListHandler<T> rs) throws Exception {
			synchronized (m_sync) {
				return JDBCUtils.query(getConnection().getConnection(), sql, rs);
			}
		}

		public synchronized static int execute(final String sql, final Collection<Object> params) throws Exception {
			synchronized (m_sync) {
				return JDBCUtils.execute(getConnection().getConnection(), sql, params.toArray());
			}
		}

		public synchronized static void commit() throws Exception {
			JDBCUtils.commit(getConnection().getConnection());
		}
	}
}
