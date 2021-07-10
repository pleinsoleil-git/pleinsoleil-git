package a00100.app.job.a00100.ikyu.job.request.process;

import common.app.job.JobStatus;
import common.jdbc.JDBCParameterList;
import common.jdbc.JDBCUtils;
import lombok.Data;
import lombok.val;
import lombok.experimental.Accessors;

@Data
@Accessors(prefix = "m_", chain = false)
public class Status implements AutoCloseable {
	JobStatus m_status;
	String m_errorCode;
	String m_errorMessage;

	@Override
	public void close() throws Exception {
		switch (getStatus()) {
		case SUCCESS:
			faild();
			break;
		default:
			faild();
			break;
		}

	}

	void success() throws Exception {
		String sql;
		sql = "WITH s_params AS\n"
			+ "(\n"
				+ "SELECT ?::BIGINT AS request_id\n"
			+ ")\n"
			+ "INSERT INTO j_crawl_process_status\n"
			+ "(\n"
				+ "foreign_id,\n"
				+ "status,\n"
				+ "error_code,\n"
				+ "error_message\n"
			+ ")\n"
			+ "SELECT j10.id,\n"
				+ "j30.status,\n"
				+ "j30.error_code,\n"
				+ "j30.error_message\n"
			+ "FROM s_params AS t10\n"
			+ "INNER JOIN j_crawl_request AS j10\n"
				+ "ON j10.id = t10.request_id\n"
			+ "INNER JOIN j_crawl_process AS j20\n"
				+ "ON j20.foreign_id = j10.id\n"
			+ "INNER JOIN j_crawl_process_status AS j30\n"
				+ "ON j30.foreign_id = j20.id\n"
			+ "ORDER BY j30.status, j30.id\n"
			+ "LIMIT 1\n";

		JDBCUtils.execute(sql, new JDBCParameterList() {
			{
				val process = Process.getCurrent();
				add(process.getId());
			}
		});
		JDBCUtils.commit();
	}

	void faild() throws Exception {
		String sql;
		sql = "WITH s_params AS\n"
			+ "(\n"
				+ "SELECT ?::BIGINT AS request_id,\n"
					+ "?::NUMERIC AS status,\n"
					+ "?::VARCHAR AS error_code,\n"
					+ "?::VARCHAR AS error_message\n"
			+ ")\n"
			+ "INSERT INTO j_crawl_process_status\n"
			+ "(\n"
				+ "foreign_id,\n"
				+ "status,\n"
				+ "error_code,\n"
				+ "error_message\n"
			+ ")\n"
			+ "SELECT t10.request_id,\n"
				+ "t10.status,\n"
				+ "t10.error_code,\n"
				+ "t10.error_message\n"
			+ "FROM s_params AS t10\n";

		JDBCUtils.execute(sql, new JDBCParameterList() {
			{
				val process = Process.getCurrent();
				add(process.getId());
				add(m_status.original());
				add(m_errorCode);
				add(m_errorMessage);
			}
		});
		JDBCUtils.commit();
	}
}
