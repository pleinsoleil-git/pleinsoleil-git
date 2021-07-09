package a00100.app.job.a00100.crawl.job;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import a00100.app.job.a00100.crawl.ConnectionA;
import a00100.app.job.a00100.crawl.job.request.Request;
import a00100.app.job.a00100.crawl.job.webBrowser.WebBrowser;
import common.app.job.JobStatus;
import common.jdbc.JDBCParameterList;
import common.jdbc.JDBCUtils;
import lombok.Data;
import lombok.val;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Accessors(prefix = "m_", chain = false)
public class Job {
	static Job m_instance;
	static final ThreadLocal<_Current> m_currents = new ThreadLocal<_Current>();

	Job() {
	}

	public static Job getInstance() {
		return (m_instance == null ? m_instance = new Job() : m_instance);
	}

	public static _Current getCurrent() {
		return m_currents.get();
	}

	public void execute() throws Exception {
		try (val browser = WebBrowser.getInstance()) {
			// --------------------------------------------------
			// delete();
			// --------------------------------------------------
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
			m_currents.remove();
			m_instance = null;
		}
	}

	void delete(final String tableName) throws Exception {
		log.info(String.format("Delete table %s", tableName));
		JDBCUtils.truncateTable(tableName, true);
		JDBCUtils.commit();
	}

	void delete() throws Exception {
		log.info(StringUtils.repeat("=", 50));
		log.info("リリース時には無効にすること！！");

		for (val t : new String[] {
				"j_crawl_process_status",
				"j_crawl_request_status",
				"j_crawl_job_status",
				"t_price_rakuten",
		}) {
			delete(t);
		}
	}

	Collection<_Task> query() throws Exception {
		String sql;
		sql = "WITH s_params AS\n"
			+ "(\n"
				+ "SELECT CURRENT_TIMESTAMP( 0 ) AS execution_time\n"
			+ ")\n"
			+ "SELECT j10.id,\n"
				+ "j10.job_type AS jobType,\n"
				+ "j10.job_name AS jobName,\n"
				+ "j10.execution_nums AS executionNums\n"
			+ "FROM s_params AS t10\n"
			+ "INNER JOIN j_crawl_job AS j10\n"
				+ "ON j10.execution_date <= t10.execution_time::DATE\n"
				+ "AND j10.execution_date + j10.execution_start_time <= t10.execution_time\n"
				+ "AND j10.auto_run = TRUE\n"
				+ "AND j10.aborted = FALSE\n"
				+ "AND j10.deleted = FALSE\n"
			+ "WHERE NOT EXISTS\n"
			+ "(\n"
				+ "SELECT NULL\n"
				+ "FROM j_crawl_job_status AS j900\n"
				+ "WHERE j900.foreign_id = j10.id\n"
			+ ")\n"
			+ "ORDER BY j10.execution_date + j10.execution_start_time,\n"
				+ "j10.priority NULLS LAST,\n"
				+ "j10.id\n";

		val rs = new BeanListHandler<_Task>(_Task.class);
		return ConnectionA.App.query(sql, rs);
	}

	@Data
	public static class _Current {
		Long m_id;
		String m_jobType;
		String m_jobName;
		Long m_executionNums;
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
			log.info(String.format("Job[id=%d type=%s name=%s]", getId(), getJobType(), getJobName()));

			try {
				m_currents.set(this);
				//_execute();
			} catch (Exception e) {
				log.error("", e);
			} finally {
				m_currents.remove();
			}

			return this;
		}

		void _execute() {
			val status = getStatus();

			try {
				if (aborted() == true) {
					status.setStatus(JobStatus.ABORT);
				} else {
					request();
					status.setStatus(JobStatus.SUCCESS);
				}
			} catch (Exception e) {
				status.setStatus(JobStatus.FAILD);
				status.setErrorMessage(e.getMessage());
				log.error("", e);
			}
		}

		void request() throws Exception {
			Request.getInstance().execute();
		}

		boolean aborted() throws Exception {
			String sql;
			sql = "WITH s_params AS\n"
				+ "(\n"
					+ "SELECT ?::BIGINT AS job_id\n"
				+ ")\n"
				+ "SELECT j10.aborted\n"
				+ "FROM s_params AS t10\n"
				+ "INNER JOIN j_crawl_job AS j10\n"
					+ "ON j10.id = t10.job_id\n"
					+ "AND j10.aborted = FALSE\n";

			val conn = ConnectionA.getCurrent().getDefault();
			val rs = new ScalarHandler<Boolean>();
			return BooleanUtils.isTrue(JDBCUtils.query(conn, sql, rs, new JDBCParameterList() {
				{
					add(getId());
				}
			}));
		}
	}
}
