package a00100.app.job.a00100.crawl.job.request.process;

import a00100.app.job.a00100.crawl.ConnectionA;
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
		String sql;
		sql = "WITH s_params AS\n"
			+ "(\n"
				+ "SELECT ?::BIGINT AS process_id,\n"
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
			+ "SELECT t10.process_id,\n"
				+ "t10.status,\n"
				+ "t10.error_code,\n"
				+ "t10.error_message\n"
			+ "FROM s_params AS t10\n";

		val conn = ConnectionA.getCurrent().getDefault();
		JDBCUtils.execute(conn, sql, new JDBCParameterList() {
			{
				val process = Process.getCurrent();
				add(process.getId());
				add(m_status.original());
				add(m_errorCode);
				add(m_errorMessage);
			}
		});
		JDBCUtils.commit(conn);
	}
}
