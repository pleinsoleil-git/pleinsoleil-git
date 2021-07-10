package a00100.app.job.a00100.rakuten.job.request.process.crawl.query;

import a00100.app.job.a00100.rakuten.job.request.process.Process;
import a00100.app.job.a00100.rakuten.job.request.process.webBrowser.WebClient;
import common.jdbc.JDBCParameterList;
import common.jdbc.JDBCUtils;
import lombok.val;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
class Entry extends WebClient {
	static final ThreadLocal<Entry> m_instances = new ThreadLocal<Entry>() {
		@Override
		protected Entry initialValue() {
			return new Entry();
		}
	};

	Entry() {
	}

	static Entry getInstance() {
		return m_instances.get();
	}

	public WebClient execute() throws Exception {
		try {
			insert();
			return null;
		} finally {
			m_instances.remove();
		}
	}

	void insert() throws Exception {
		String sql;
		sql = "WITH s_params AS\n"
			+ "(\n"
				+ "SELECT ?::DATE AS execution_date\n"
			+ ")\n"
			+ "INSERT INTO domestic_00101.t_price\n"
			+ "(\n"
				+ "execution_date,\n"
				+ "hotel_code,\n"
				+ "hotel_name,\n"
				+ "plan_code,\n"
				+ "plan_name,\n"
				+ "plan_url,\n"
				+ "room_code,\n"
				+ "room_name,\n"
				+ "room_info,\n"
				+ "room_remark,\n"
				+ "room_option_meal,\n"
				+ "room_option_people,\n"
				+ "room_option_payment,\n"
				+ "point_rate,\n"
				+ "price,\n"
				+ "original_price,\n"
				+ "discounted_price,\n"
				+ "per_person_price\n"
			+ ")\n"
			+ "SELECT t10.execution_date,\n"
				+ "t20.hotel_code,\n"
				+ "t20.hotel_name,\n"
				+ "t20.plan_code,\n"
				+ "t20.plan_name,\n"
				+ "t20.plan_url,\n"
				+ "t20.room_code,\n"
				+ "t20.room_name,\n"
				+ "t20.room_info,\n"
				+ "t20.room_remark,\n"
				+ "t20.room_option_meal,\n"
				+ "t20.room_option_people,\n"
				+ "t20.room_option_payment,\n"
				+ "t20.point_rate::NUMERIC,\n"
				+ "t20.price::NUMERIC,\n"
				+ "t20.original_price::NUMERIC,\n"
				+ "t20.discounted_price::NUMERIC,\n"
				+ "t20.per_person_price::NUMERIC\n"
			+ "FROM s_params AS t10\n"
			+ "CROSS JOIN temp_price_rakuten AS t20\n"
			+ "ORDER BY t20.id\n";

		val conn = getConnection();
		JDBCUtils.execute(conn, sql, new JDBCParameterList() {
			{
				val process = Process.getCurrent();
				add(process.getExecutionDate());
			}
		});
		JDBCUtils.commit(conn);
	}
}
