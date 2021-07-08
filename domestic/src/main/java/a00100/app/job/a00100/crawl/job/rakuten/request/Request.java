package a00100.app.job.a00100.crawl.job.rakuten.request;

import java.util.concurrent.Callable;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Accessors(prefix = "m_", chain = false)
public class Request {
	static final ThreadLocal<Request> m_instances = new ThreadLocal<Request>() {
		@Override
		protected Request initialValue() {
			return new Request();
		}
	};
	static final ThreadLocal<_Current> m_currents = new ThreadLocal<_Current>();

	Request() {
	}

	public static Request getInstance() {
		return m_instances.get();
	}

	public static _Current getCurrent() {
		return m_currents.get();
	}

	public void execute() throws Exception {
		try {
		} finally {
			m_instances.remove();
		}
	}

	@Data
	public static class _Current {
		Long m_id;
		String m_requestType;
		String m_requestName;

		void execute() throws Exception {
		}
	}

	public static class _Task extends _Current implements Callable<_Task> {
		@Override
		public _Task call() {
			try {
				m_currents.set(this);
				_execute();
			} catch (Exception e) {
				log.error("", e);
			} finally {
				m_currents.remove();
			}

			return this;
		}

		void _execute() throws Exception {
			log.info(String.format("Request[id=%d type=%s name=%s]", getId(), getRequestType(), getRequestName()));
		}
	}
}
