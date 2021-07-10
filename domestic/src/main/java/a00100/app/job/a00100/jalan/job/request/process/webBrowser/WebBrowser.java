package a00100.app.job.a00100.jalan.job.request.process.webBrowser;

import java.util.concurrent.ConcurrentHashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import a00100.app.job.a00100.jalan.job.request.process.connection.Connection;
import common.jdbc.JDBCUtils;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebBrowser extends common.webBrowser.WebBrowser implements AutoCloseable {
	static WebBrowser m_instance;
	static final ConcurrentHashMap<Long, _Current> m_currents = new ConcurrentHashMap<Long, _Current>();

	WebBrowser() {
	}

	public static WebBrowser getInstance() {
		return (m_instance == null ? m_instance = new WebBrowser() : m_instance);
	}

	public static _Current getCurrent() {
		val k = Thread.currentThread().getId();
		_Current v = m_currents.get(k);

		if (v == null) {
			m_currents.put(k, v = new _Current());
		}

		return v;
	}

	public static WebDriver getWebDriver() throws Exception {
		return getCurrent().getWebDriver();
	}

	@Override
	public void close() throws Exception {
		try {
			for (val v : m_currents.values()) {
				v.close();
			}
		} finally {
			m_currents.clear();
			m_instance = null;
		}
	}

	public static class _Current implements AutoCloseable {
		WebDriver m_webDriver;

		public WebDriver getWebDriver() throws Exception {
			if (m_webDriver == null) {
				log.debug("Browser open!!");
				m_webDriver = new ChromeDriver(new ChromeOptions() {
					{
						addArguments("--incognito"); // シークレットモード
					}
				});

				//createTempTable();
			}

			return m_webDriver;
		}

		@Override
		public void close() throws Exception {
			if (m_webDriver != null) {
				log.debug("Browser close!!");
				//m_webDriver.quit();
			}
		}

		void createTempTable() throws Exception {
			createPrice();
		}

		void createPrice() throws Exception {
			String sql;
			sql = "CREATE LOCAL TEMPORARY TABLE IF NOT EXISTS temp_price_rakuten\n"
				+ "(\n"
					+ "id					BIGSERIAL,\n"
					+ "hotel_code			VARCHAR,\n"
					+ "hotel_name			VARCHAR,\n"
					+ "plan_code			VARCHAR,\n"
					+ "plan_name			VARCHAR,\n"
					+ "plan_url				VARCHAR,\n"
					+ "room_code			VARCHAR,\n"
					+ "room_name			VARCHAR,\n"
					+ "room_info			VARCHAR,\n"
					+ "room_remark			VARCHAR,\n"
					+ "room_option_meal		VARCHAR,\n"
					+ "room_option_people	VARCHAR,\n"
					+ "room_option_payment	VARCHAR,\n"
					+ "point_rate			VARCHAR,\n"
					+ "price				VARCHAR,\n"
					+ "original_price		VARCHAR,\n"
					+ "discounted_price		VARCHAR,\n"
					+ "per_person_price		VARCHAR\n"
				+ ")\n"
				+ "WITH\n"
				+ "(\n"
					+ "FILLFACTOR = 100\n"
				+ ")\n";

			val conn = Connection.app();
			JDBCUtils.execute(conn, sql);
			JDBCUtils.commit(conn);
		}
	}
}
