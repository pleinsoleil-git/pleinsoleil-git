package a00100.app.job.a00100.rakuten.job;

import java.util.Collection;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.lang3.StringUtils;

import a00100.app.job.a00100.RequestType;
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
	_Current m_current;

	Job() {
	}

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

	void delete(final String tableName) throws Exception {
		log.info(String.format("Delete table %s", tableName));
		JDBCUtils.truncateTable(tableName, true);
		JDBCUtils.commit();
	}

	void delete() throws Exception {
		log.info(StringUtils.repeat("=", 50));
		log.info("リリース時には無効にすること！！");

		for (val t : new String[] {
				"j_crawl_process_result",
				"j_crawl_process_status",
				"j_crawl_request_status",
				"j_crawl_job_status",
				"t_price_rakuten",
		}) {
			delete(t);
		}
	}

	Collection<_Current> query() throws Exception {
		String sql;
		sql = "WITH s_params AS\n"
			+ "(\n"
				+ "SELECT CURRENT_TIMESTAMP( 0 ) AS execution_time,\n"
					+ "?::VARCHAR AS request_type\n"
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
			+ "WHERE EXISTS\n"
			+ "(\n"
				+ "SELECT NULL\n"
				+ "FROM j_crawl_request AS j900\n"
				+ "WHERE j900.foreign_id = j10.id\n"
				+ "AND j900.request_type = t10.request_type\n"
				+ "AND j900.deleted = FALSE\n"
			+ ")\n";

		val rs = new BeanListHandler<_Current>(_Current.class);
		return JDBCUtils.query(sql, rs, new JDBCParameterList() {
			{
				add(RequestType.RAKUTEN.name());
			}
		});
	}

	@Data
	public static class _Current {
		Long m_id;
		String m_jobType;
		String m_jobName;

		void execute() throws Exception {
			log.info(String.format("Job[id=%d type=%s name=%s]", getId(), getJobType(), getJobName()));
		}
	}
}
