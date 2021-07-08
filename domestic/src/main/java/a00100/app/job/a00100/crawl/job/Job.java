package a00100.app.job.a00100.crawl.job;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;

import org.apache.commons.dbutils.handlers.BeanListHandler;

import a00100.app.job.a00100.crawl.job.rakuten.Rakuten;
import common.jdbc.JDBCUtils;
import common.lang.NotSupportedException;
import common.webBrowser.WebBrowser;
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

	Collection<_Task> query() throws Exception {
		String sql;
		sql = "WITH s_params AS\n"
			+ "(\n"
				+ "SELECT CURRENT_TIMESTAMP( 0 ) AS execution_time\n"
			+ ")\n"
			+ "SELECT j10.id,\n"
				+ "j10.job_type AS jobType,\n"
				+ "j10.job_name AS jobName\n"
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
		return JDBCUtils.query(sql, rs);
	}

	@Data
	public static class _Current {
		Long m_id;
		String m_jobType;
		String m_jobName;

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
			log.info(String.format("Job[id=%d type=%s name=%s]", getId(), getJobType(), getJobName()));

			switch (JobType.valueOf(getJobType())) {
			case RAKUTEN:
				rakuten();
				break;
			default:
				throw new NotSupportedException();
			}
		}

		void rakuten() throws Exception {
			Rakuten.getInstance().execute();
		}
	}
}
