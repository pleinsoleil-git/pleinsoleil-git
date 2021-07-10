package a00100.app.job.a00100.rakuten.job.request.process;

import java.util.Collection;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.BooleanUtils;

import a00100.app.job.a00100.rakuten.job.request.Request;
import common.app.job.JobStatus;
import common.jdbc.JDBCParameterList;
import common.jdbc.JDBCUtils;
import lombok.Data;
import lombok.val;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Accessors(prefix = "m_", chain = false)
public class Process {
	static Process m_instance;
	_Current m_current;

	Process() {
	}

	public static Process getInstance() {
		return (m_instance == null ? m_instance = new Process() : m_instance);
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
				+ "SELECT ?::BIGINT AS request_id\n"
			+ ")\n"
			+ "SELECT j30.id,\n"
				+ "j20.job_type AS jobType,\n"
				+ "j10.request_type AS requestType,\n"
				+ "j10.user_id AS userId,\n"
				+ "j10.password,\n"
				+ "j20.check_in_date AS checkInDate,\n"
				+ "j20.check_out_date AS checkOutDate,\n"
				+ "j20.room_nums AS roomNums,\n"
				+ "j20.adult_nums AS adultNums,\n"
				+ "j20.upper_grade_nums AS upperGradeNums,\n"
				+ "j20.lower_grade_nums AS lowerGradeNums,\n"
				+ "j30.hotel_code AS hotelCode\n"
			+ "FROM s_params AS t10\n"
			+ "INNER JOIN j_crawl_request AS j10\n"
				+ "ON j10.id = t10.request_id\n"
				+ "AND j10.aborted = FALSE\n"
			+ "INNER JOIN j_crawl_job AS j20\n"
				+ "ON j20.id = j10.foreign_id\n"
				+ "AND j20.aborted = FALSE\n"
			+ "INNER JOIN j_crawl_process AS j30\n"
				+ "ON j30.foreign_id = j10.id\n"
				+ "AND j30.aborted = FALSE\n"
				+ "AND j30.deleted = FALSE\n"
			+ "WHERE NOT EXISTS\n"
			+ "(\n"
				+ "SELECT NULL\n"
				+ "FROM j_crawl_process_status AS j900\n"
				+ "WHERE j900.foreign_id = j30.id\n"
			+ ")\n";

		val rs = new BeanListHandler<_Current>(_Current.class);
		return JDBCUtils.query(sql, rs, new JDBCParameterList() {
			{
				val request = Request.getCurrent();
				add(request.getId());
			}
		});
	}

	@Data
	public static class _Current {
		Long m_id;
		Status m_status;

		Status getStatus() {
			return (m_status == null ? m_status = new Status() : m_status);
		}

		void execute() throws Exception {
			try (val status = getStatus()) {
				try {
					if (aborted() == true) {
						status.setStatus(JobStatus.ABORT);
					} else {
						status.setStatus(JobStatus.SUCCESS);
					}
				} catch (Exception e) {
					status.setStatus(JobStatus.FAILD);
					status.setErrorMessage(e.getMessage());
					log.error("", e);
				}
			}
		}

		boolean aborted() throws Exception {
			String sql;
			sql = "WITH s_params AS\n"
				+ "(\n"
					+ "SELECT ?::BIGINT AS process_id\n"
				+ ")\n"
				+ "SELECT j10.aborted\n"
				+ "FROM s_params AS t10\n"
				+ "INNER JOIN j_crawl_process AS j10\n"
					+ "ON j10.id = t10.process_id\n"
				+ "INNER JOIN j_crawl_request AS j20\n"
					+ "ON j20.id = j10.foreign_id\n"
					+ "AND j20.aborted = FALSE\n"
				+ "INNER JOIN j_crawl_job AS j30\n"
					+ "ON j30.id = j20.foreign_id\n"
					+ "AND j30.aborted = FALSE\n";

			val rs = new ScalarHandler<Boolean>();
			return BooleanUtils.isTrue(JDBCUtils.query(sql, rs, new JDBCParameterList() {
				{
					add(getId());
				}
			}));
		}
	}
}
