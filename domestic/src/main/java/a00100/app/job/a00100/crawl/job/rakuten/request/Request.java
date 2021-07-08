package a00100.app.job.a00100.crawl.job.rakuten.request;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;

import org.apache.commons.dbutils.handlers.BeanListHandler;

import a00100.app.job.a00100.crawl.Connection;
import a00100.app.job.a00100.crawl.job.Job;
import common.jdbc.JDBCParameterList;
import common.jdbc.JDBCUtils;
import lombok.Data;
import lombok.val;
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
			val executor = Executors.newFixedThreadPool(5);
			val completion = new ExecutorCompletionService<_Task>(executor);

			try {
				int taskNums = 0;

				do {
					for (val r : query()) {
						taskNums++;
						completion.submit(r);
					}

					if (taskNums > 0) {
						m_currents.set(completion.take().get());
						m_currents.get().execute();
					}
				} while (--taskNums > 0);

				executor.shutdown();
			} catch (Exception e) {
				executor.shutdownNow();
				throw e;
			}
		} finally {
			m_instances.remove();
		}
	}

	Collection<_Task> query() throws Exception {
		String sql;
		sql = "WITH s_params AS\n"
			+ "(\n"
				+ "SELECT ?::BIGINT AS job_id\n"
			+ ")\n"
			+ "SELECT j20.id,\n"
				+ "j20.request_type AS requestType,\n"
				+ "j20.request_name AS requestName\n"
			+ "FROM s_params AS t10\n"
			+ "INNER JOIN j_crawl_job AS j10\n"
				+ "ON j10.id = t10.job_id\n"
				+ "AND j10.aborted = FALSE\n"
			+ "INNER JOIN j_crawl_request AS j20\n"
				+ "ON j20.foreign_id = j10.id\n"
				+ "AND j20.aborted = FALSE\n"
				+ "AND j20.deleted = FALSE\n"
			+ "WHERE NOT EXISTS\n"
			+ "(\n"
				+ "SELECT NULL\n"
				+ "FROM j_crawl_request_status AS j900\n"
				+ "WHERE j900.foreign_id = j20.id\n"
			+ ")\n"
			+ "ORDER BY j20.priority NULLS LAST,\n"
				+ "j20.id\n";

		val conn = Connection.getCurrent().getDefault();
		val rs = new BeanListHandler<_Task>(_Task.class);
		return JDBCUtils.query(conn, sql, rs, new JDBCParameterList() {
			{
				val job = Job.getCurrent();
				add(job.getId());
			}
		});
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
