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
	static final ConcurrentHashMap<Long, _Current> m_currents = new ConcurrentHashMap<Long, _Current>();

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
		@Override
		public void close() throws Exception {
		}
	}
}
