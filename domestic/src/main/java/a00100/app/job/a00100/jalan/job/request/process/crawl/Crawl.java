package a00100.app.job.a00100.jalan.job.request.process.crawl;

import java.util.Collection;
import java.util.Date;

import org.apache.commons.dbutils.handlers.BeanListHandler;

import a00100.app.job.a00100.jalan.job.request.process.Process;
import a00100.app.job.a00100.jalan.job.request.process.crawl.login.Login;
import a00100.app.job.a00100.jalan.job.request.process.webBrowser.WebClient;
import common.jdbc.JDBCParameterList;
import common.jdbc.JDBCUtils;
import lombok.Data;
import lombok.val;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
public class Crawl extends WebClient {
	static final ThreadLocal<Crawl> m_instances = new ThreadLocal<Crawl>() {
		@Override
		protected Crawl initialValue() {
			return new Crawl();
		}
	};
	static final ThreadLocal<_Current> m_currents = new ThreadLocal<_Current>();

	Crawl() {
	}

	public static Crawl getInstance() {
		return m_instances.get();
	}

	public static _Current getCurrent() {
		return m_currents.get();
	}

	@Override
	public WebClient execute() throws Exception {
		try {
			for (val r : query()) {
				m_currents.set(r);
				r.execute();
			}

			return null;
		} finally {
			m_currents.remove();
			m_instances.remove();
		}
	}

	Collection<_Current> query() throws Exception {
		String sql;
		sql = "WITH s_params AS\n"
			+ "(\n"
				+ "SELECT ?::BIGINT AS process_id\n"
			+ ")\n"
			+ "SELECT j30.execution_date AS executionDate,\n"
				+ "j20.user_id AS userId,\n"
				+ "j20.password,\n"
				+ "TO_CHAR( j30.check_in_date, 'YYYY' ) AS yearFrom,\n"
				+ "TO_CHAR( j30.check_in_date, 'MM' ) AS monthFrom,\n"
				+ "TO_CHAR( j30.check_in_date, 'DD' ) AS dayFrom,\n"
				+ "( j30.check_out_date - j30.check_in_date )::VARCHAR stayNums,\n"
				+ "j30.check_out_date AS checkOutDate,\n"
				+ "j30.room_nums AS roomNums,\n"
				+ "j30.adult_nums AS adultNums,\n"
				+ "j30.upper_grade_nums AS upperGradeNums,\n"
				+ "j30.lower_grade_nums AS lowerGradeNums,\n"
				+ "j10.hotel_code AS hotelCode\n"
			+ "FROM s_params AS t10\n"
			+ "INNER JOIN j_crawl_process AS j10\n"
				+ "ON j10.id = t10.process_id\n"
			+ "INNER JOIN j_crawl_request AS j20\n"
				+ "ON j20.id = j10.foreign_id\n"
			+ "INNER JOIN j_crawl_job AS j30\n"
				+ "ON j30.id = j20.foreign_id\n";

		val rs = new BeanListHandler<_Current>(_Current.class);
		return JDBCUtils.query(getConnection(), sql, rs, new JDBCParameterList() {
			{
				val process = Process.getCurrent();
				add(process.getId());
			}
		});
	}

	@Data
	public static class _Current {
		Long m_id;
		Date m_executionDate;
		String m_userId;
		String m_password;
		String m_yearFrom;
		String m_monthFrom;
		String m_dayFrom;
		String m_stayNums;
		Long m_roomNums;
		Long m_adultNums;
		Long m_upperGradeNums;
		Long m_lowerGradeNums;
		String m_hotelCode;

		void execute() throws Exception {
			for (WebClient client = Login.getInstance(); client != null;) {
				client = client.execute();
			}
		}
	}
}
