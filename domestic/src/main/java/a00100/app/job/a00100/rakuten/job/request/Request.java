package a00100.app.job.a00100.rakuten.job.request;

import java.util.Collection;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.BooleanUtils;

import a00100.app.job.a00100.RequestType;
import common.jdbc.JDBCParameterList;
import common.jdbc.JDBCUtils;
import lombok.Data;
import lombok.val;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Accessors(prefix = "m_", chain = false)
public class Request {
	static Request m_instance;
	_Current m_current;

	Request() {
	}

	public static Request getInstance() {
		return (m_instance == null ? m_instance = new Request() : m_instance);
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

			val rs = new ScalarHandler<Boolean>();
			return BooleanUtils.isTrue(JDBCUtils.query(sql, rs, new JDBCParameterList() {
				{
					add(getId());
				}
			}));
		}
	}
}
