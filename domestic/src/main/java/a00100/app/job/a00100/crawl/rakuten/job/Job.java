package a00100.app.job.a00100.crawl.rakuten.job;

import java.util.Collection;

import org.apache.commons.dbutils.handlers.BeanListHandler;

import a00100.app.job.a00100.crawl.rakuten.job.request.Request;
import common.jdbc.JDBCUtils;
import common.webBrowser.WebBrowser;
import lombok.Data;
import lombok.val;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Accessors(prefix = "m_", chain = false)
public class Job {
	static Job m_instance;
	_Current m_current;

	public static Job getInstance() {
		return (m_instance == null ? m_instance = new Job() : m_instance);
	}

	public static _Current getCurrent() {
		return getInstance().m_current;
	}

	public void execute() throws Exception {
		try {
			for (val r : query()) {
				(m_current = r).execute();
			}
		} finally {
			m_instance = null;
		}
	}

	Collection<_Current> query() throws Exception {
		String sql;
		sql = "WITH s_params AS\n"
			+ "(\n"
				+ "SELECT CURRENT_TIMESTAMP( 0 ) AS execution_time\n"
			+ ")\n"
			+ "SELECT j10.id,\n"
				+ "j10.job_type AS jobType,\n"
				+ "j10.job_name AS jobName\n"
			+ "FROM s_params AS t10\n"
			+ "INNER JOIN domestic_00101.j_crawl_job AS j10\n"
				+ "ON j10.execution_date <= t10.execution_time::DATE\n"
				+ "AND j10.execution_date + j10.execution_start_time <= t10.execution_time\n"
				+ "AND j10.auto_run = TRUE\n"
				+ "AND j10.deleted = FALSE\n"
			+ "WHERE NOT EXISTS\n"
			+ "(\n"
				+ "SELECT NULL\n"
				+ "FROM domestic_00101.j_crawl_job_status AS j900\n"
				+ "WHERE j900.foreign_id = j10.id\n"
			+ ")\n"
			+ "ORDER BY j10.execution_date + j10.execution_start_time,\n"
				+ "j10.priority NULLS LAST,\n"
				+ "j10.id\n";

		val rs = new BeanListHandler<_Current>(_Current.class);
		return JDBCUtils.query(sql, rs);
	}

	@Data
	public static class _Current {
		Long m_id;
		String m_jobType;
		String m_jobName;
		String m_webDriver;

		void execute() throws Exception {
			log.info(String.format("Job[id=%d type=%s name=%s]", getId(), getJobType(), getJobName()));

			try (val browser = WebBrowser.getInstance()) {
				//request();
			}
		}

		void request() throws Exception {
			Request.getInstance().execute();
		}
	}
}
