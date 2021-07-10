package a00100.app.job.a00100.jalan.job.request;

import java.util.Collection;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.BooleanUtils;

import a00100.app.job.a00100.jalan.job.Job;
import a00100.app.job.a00100.jalan.job.request.process.Process;
import common.app.job.JobStatus;
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
				+ "SELECT ?::BIGINT AS job_id,\n"
					+ "?::VARCHAR AS request_type\n"
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
				+ "AND j20.request_type = t10.request_type\n"
				+ "AND j20.aborted = FALSE\n"
				+ "AND j20.deleted = FALSE\n"
			+ "WHERE NOT EXISTS\n"
			+ "(\n"
				+ "SELECT NULL\n"
				+ "FROM j_crawl_request_status AS j900\n"
				+ "WHERE j900.foreign_id = j20.id\n"
			+ ")\n";

		val rs = new BeanListHandler<_Current>(_Current.class);
		return JDBCUtils.query(sql, rs, new JDBCParameterList() {
			{
				val job = Job.getCurrent();
				add(job.getId());
				add(job.getRequestType());
			}
		});
	}

	@Data
	public static class _Current {
		Long m_id;
		String m_requestType;
		String m_requestName;
		Status m_status;

		Status getStatus() {
			return (m_status == null ? m_status = new Status() : m_status);
		}

		void execute() throws Exception {
			log.info(String.format("Request[id=%d type=%s name=%s]", getId(), getRequestType(), getRequestName()));

			try (val status = getStatus()) {
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
			return BooleanUtils.isTrue(JDBCUtils.query(sql, rs, new JDBCParameterList() {
				{
					add(getId());
				}
			}));
		}
	}
}
