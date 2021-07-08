package a00100.app.job.a00100.crawl;

import java.util.concurrent.ConcurrentHashMap;

import lombok.val;

public class Connection implements AutoCloseable {
	static final ThreadLocal<Connection> m_instances = new ThreadLocal<Connection>() {
		@Override
		protected Connection initialValue() {
			return new Connection();
		}
	};
	ConcurrentHashMap<Long, _Current> m_currents;

	Connection() {
	}

	public static Connection getInstance() {
		return m_instances.get();
	}

	public static _Current getCurrent() {
		@SuppressWarnings("resource")
		val c = getInstance().m_currents;
		val k = Thread.currentThread().getId();
		_Current v = c.get(k);

		if (v == null) {
			c.put(k, v = new _Current());
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
			m_instances.remove();
		}
	}

	public static class _Current implements AutoCloseable {
		@Override
		public void close() throws Exception {
		}
	}
}
