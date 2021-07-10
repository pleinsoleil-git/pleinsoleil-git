package a00100.app.job.a00100.ikyu.job.request.process.connection;

import java.util.concurrent.ConcurrentHashMap;

import a00100.app.job.a00100.ikyu.App;
import common.jdbc.JDBCConnection;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Connection implements AutoCloseable {
	static Connection m_instance;
	static final ConcurrentHashMap<Long, _Current> m_currents = new ConcurrentHashMap<Long, _Current>();

	Connection() {
	}

	public static Connection getInstance() {
		return (m_instance == null ? m_instance = new Connection() : m_instance);
	}

	public static _Current getCurrent() {
		val k = Thread.currentThread().getId();
		_Current v = m_currents.get(k);

		if (v == null) {
			m_currents.put(k, v = new _Current());
		}

		return v;
	}

	public static JDBCConnection app() {
		return getCurrent().getApp();
	}

	@Override
	public void close() throws Exception {
		try {
			for (val v : m_currents.values()) {
				v.close();
			}
		} finally {
			m_currents.clear();
			m_instance = null;
		}
	}

	public static class _Current implements AutoCloseable {
		JDBCConnection m_app;

		public JDBCConnection getApp() {
			return (m_app == null ? m_app = new JDBCConnection(App.DEFAULT_DSN) {
				{
					log.debug("Connection open!!");
				}
			} : m_app);
		}

		@Override
		public void close() throws Exception {
			if (m_app != null) {
				log.debug("Connection close!!");
				m_app.close();
			}
		}
	}
}
