package a00100.app.job.a00100.crawl;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.dbutils.handlers.BeanListHandler;

import common.jdbc.JDBCConnection;
import common.jdbc.JDBCUtils;
import lombok.val;

public class Connection implements AutoCloseable {
	static final ThreadLocal<Connection> m_instances = new ThreadLocal<Connection>() {
		@Override
		protected Connection initialValue() {
			return new Connection();
		}
	};
	static final ConcurrentHashMap<Long, _Current> m_currents = new ConcurrentHashMap<Long, _Current>();
	static final ConcurrentHashMap<Long, JDBCConnection> m_connections = new ConcurrentHashMap<Long, JDBCConnection>();

	Connection() {
	}

	public static Connection getInstance() {
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

		public JDBCConnection getDefault() {
			return (m_default == null ? m_default = new JDBCConnection(a00100.App.DEFAULT_DSN) : m_default);
		}

		@Override
		public void close() throws Exception {
			if (m_default != null) {
				m_default.close();
			}
		}
	}

	public static class App {
		static JDBCConnection getConnection() {
			return Connection.getCurrent().getDefault();
		}

		public static <T> List<T> query(final String sql, final BeanListHandler<T> rs) throws Exception {
			return JDBCUtils.query(getConnection().getConnection(), sql, rs);
		}

		public static int execute(final String sql, final Collection<Object> params) throws Exception {
			return JDBCUtils.execute(getConnection().getConnection(), sql, params.toArray());
		}

		public static void commit() throws Exception {
			JDBCUtils.commit(getConnection().getConnection());
		}
	}
}
