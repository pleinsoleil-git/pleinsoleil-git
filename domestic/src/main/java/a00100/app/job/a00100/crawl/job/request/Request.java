package a00100.app.job.a00100.crawl.job.request;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import a00100.app.job.a00100.crawl.Connection;
import a00100.app.job.a00100.crawl.job.Job;
import a00100.app.job.a00100.crawl.job.request.process.Process;
import common.app.job.JobStatus;
import common.jdbc.JDBCParameterList;
import lombok.Data;
import lombok.val;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Accessors(prefix = "m_", chain = false)
public class Request {
	static int MAX_THREAD_NUM = 5;
	static final ThreadLocal<Request> m_instances = new ThreadLocal<Request>() {
		@Override
		protected Request initialValue() {
			return new Request();
		}
	};
	static final ThreadLocal<_Current> m_currents = new ThreadLocal<_Current>();
	Set<Long> m_runningIds;

	Request() {
	}

	public static Request getInstance() {
		return m_instances.get();
	}

	public static _Current getCurrent() {
		return m_currents.get();
	}

	Set<Long> getRunningIds() {
		return (m_runningIds == null ? m_runningIds = new HashSet<>() : m_runningIds);
	}

	public void execute() throws Exception {
		try {
			val executor = Executors.newFixedThreadPool(MAX_THREAD_NUM);
			val completion = new ExecutorCompletionService<_Task>(executor);

			try {
				val ids = getRunningIds();

				do {
					for (val r : query()) {
						ids.add(r.getId());
						completion.submit(r);
					}

					if (ids.size() > 0) {
						val request = completion.take().get();
						m_currents.set(request);
						request.execute();
						ids.remove(request.getId());
					}
				} while (ids.size() > 0);

				executor.shutdown();
			} catch (Exception e) {
				executor.shutdownNow();
				throw e;
			}
		} finally {
			m_currents.remove();
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
			+ ")\n";

		val ids = getRunningIds();
		if (ids.isEmpty() == false) {
			sql += "AND j20.id NOT IN\n"
				+ "(\n"
					+ StringUtils.repeat("?::BIGINT", ",\n", ids.size())
				+ ")\n";
		}

		sql += "ORDER BY j20.priority NULLS LAST,\n"
				+ "j20.id\n";

		val rs = new BeanListHandler<_Task>(_Task.class);
		return Connection.App.query(sql, rs, new JDBCParameterList() {
			{
				val job = Job.getCurrent();
				add(job.getId());

				for (val id : ids) {
					add(id);
				}
			}
		});
	}

	@Data
	public static class _Current {
		Long m_id;
		String m_requestType;
		String m_requestName;
		Status m_status;

		public Status getStatus() {
			return (m_status == null ? m_status = new Status() : m_status);
		}

		void execute() throws Exception {
			try (val status = getStatus()) {
			}
		}
	}

	public static class _Task extends _Current implements Callable<_Task> {
		@Override
		public _Task call() {
			log.info(String.format("Request[id=%d type=%s name=%s]", getId(), getRequestType(), getRequestName()));

			try {
				m_currents.set(this);
				run();
			} catch (Exception e) {
				log.error("", e);
			} finally {
				m_currents.remove();
			}

			return this;
		}

		void run() {
			val status = getStatus();

			try {
				if (aborted() == true) {
					status.setStatus(JobStatus.ABORT);
				} else {
					process();
					status.setStatus(JobStatus.SUCCESS);
				}
			} catch (Exception e) {
				status.setStatus(JobStatus.FAILD);
				status.setErrorMessage(e.getMessage());
				log.error("", e);
			}
		}

		void process() throws Exception {
			Process.getInstance().execute();
		}

		boolean aborted() throws Exception {
			String sql;
			sql = "WITH s_params AS\n"
				+ "(\n"
					+ "SELECT ?::BIGINT AS request_id\n"
				+ ")\n"
				+ "SELECT j10.aborted\n"
				+ "FROM s_params AS t10\n"
				+ "INNER JOIN j_crawl_request AS j10\n"
					+ "ON j10.id = t10.request_id\n"
				+ "INNER JOIN j_crawl_job AS j20\n"
					+ "ON j20.id = j10.foreign_id\n"
					+ "AND j20.aborted = FALSE\n";

			val rs = new ScalarHandler<Boolean>();
			return BooleanUtils.isTrue(Connection.App.query(sql, rs, new JDBCParameterList() {
				{
					add(getId());
				}
			}));
		}
	}
}
