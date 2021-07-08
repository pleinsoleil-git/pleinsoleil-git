package a00100.app.job.a00100.crawl.job.request.process.rakuten.query;

import java.util.ArrayList;
import java.util.Collection;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import a00100.app.job.a00100.crawl.Connection;
import a00100.app.job.a00100.crawl.job.webBrowser.WebClient;
import common.jdbc.JDBCUtils;
import lombok.val;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
class Plan extends WebClient {
	static final ThreadLocal<Plan> m_instances = new ThreadLocal<Plan>() {
		@Override
		protected Plan initialValue() {
			return new Plan();
		}
	};

	Plan() {
	}

	static Plan getInstance() {
		return m_instances.get();
	}

	public WebClient execute() throws Exception {
		try {
			for (val r : query()) {
				for (WebClient client = r; client != null;) {
					client = client.execute();
				}
			}

			return null;
		} finally {
			m_instances.remove();
		}
	}

	void insert() throws Exception {
		val conn = Connection.getCurrent().getDefault();
		try (val stmt = JDBCUtils.createStatement(conn)) {
			String sql;
			sql = "WITH s_params AS\n"
				+ "(\n"
					+ "SELECT ?::VARCHAR AS hotel_code,\n"
						+ "?::VARCHAR AS hotel_name,\n"
						+ "?::VARCHAR AS plan_code,\n"
						+ "?::VARCHAR AS plan_name,\n"
						+ "?::VARCHAR AS room_code,\n"
						+ "?::VARCHAR AS room_name\n"
				+ ")\n"
				+ "INSERT INTO t_price_rakuten\n"
				+ "(\n"
					+ "hotel_code,\n"
					+ "hotel_name,\n"
					+ "plan_code,\n"
					+ "plan_name,\n"
					+ "room_code,\n"
					+ "room_name\n"
				+ ")\n"
				+ "SELECT t10.hotel_code,\n"
					+ "t10.hotel_name,\n"
					+ "t10.plan_code,\n"
					+ "t10.plan_name,\n"
					+ "t10.room_code,\n"
					+ "t10.room_name\n"
				+ "FROM s_params AS t10\n";

			stmt.parse(sql);

			for (val r : query()) {
				int colNum = 1;
/*
				stmt.setString(colNum++, r.getHotelCode());
				stmt.setString(colNum++, r.getHotelName());
				stmt.setString(colNum++, r.getPlanCode());
				stmt.setString(colNum++, r.getPlanName());
				stmt.setString(colNum++, r.getRoomCode());
				stmt.setString(colNum++, r.getRoomName());
				stmt.addBatch();
*/
			}

			stmt.executeBatchAndClear();
			JDBCUtils.commit(conn);
		}
	}

	Collection<_00000> query() throws Exception {
		return new ArrayList<_00000>() {
			{
				val driver = getWebDriver();
				val actions = new Actions(driver);
				val by = By.xpath("//div[@class='planList']/ul/li");

				for (val element : driver.findElements(by)) {
					add(new _00000() {
						{
							// -------------------------------------------------------
							// プランまでスクロールしないと金額が取得できない
							// -------------------------------------------------------
							actions.moveToElement(m_element = element);
							actions.perform();
						}
					});
				}
			}
		};
	}

	static class _00000 extends WebClient {
		WebElement m_element;

		String getHotelName() throws Exception {
			val by = By.xpath("//a[contains(@class,'rtconds')]");
			val element = m_element.findElement(by);
			return element.getText();
		}

		String getPlanName() throws Exception {
			val by = By.xpath(".//child::*[1]");
			val element = m_element.findElement(by);
			return element.getText();
		}

		public WebClient submit() throws Exception {
			for (val r : query()) {
				r.execute();
			}

			return null;
		}

		void insert() throws Exception {
			val conn = Connection.getCurrent().getDefault();
			try (val stmt = JDBCUtils.createStatement(conn)) {
				String sql;
				sql = "WITH s_params AS\n"
					+ "(\n"
						+ "SELECT ?::VARCHAR AS hotel_code,\n"
							+ "?::VARCHAR AS hotel_name,\n"
							+ "?::VARCHAR AS plan_code,\n"
							+ "?::VARCHAR AS plan_name,\n"
							+ "?::VARCHAR AS room_code,\n"
							+ "?::VARCHAR AS room_name\n"
					+ ")\n"
					+ "INSERT INTO t_price_rakuten\n"
					+ "(\n"
						+ "hotel_code,\n"
						+ "hotel_name,\n"
						+ "plan_code,\n"
						+ "plan_name,\n"
						+ "room_code,\n"
						+ "room_name\n"
					+ ")\n"
					+ "SELECT t10.hotel_code,\n"
						+ "t10.hotel_name,\n"
						+ "t10.plan_code,\n"
						+ "t10.plan_name,\n"
						+ "t10.room_code,\n"
						+ "t10.room_name\n"
					+ "FROM s_params AS t10\n";

				stmt.parse(sql);

				val hotelName = getHotelName();
				val planName = getPlanName();

				for (val r : query()) {
					int colNum = 1;
					stmt.setString(colNum++, r.getHotelCode());
					stmt.setString(colNum++, hotelName);
					stmt.setString(colNum++, r.getPlanCode());
					stmt.setString(colNum++, planName);
					stmt.setString(colNum++, r.getRoomCode());
					stmt.setString(colNum++, r.getRoomName());
					stmt.addBatch();
				}

				stmt.executeBatchAndClear();
				JDBCUtils.commit(conn);
			}
		}

		Collection<_00100> query() throws Exception {
			return new ArrayList<_00100>() {
				{
					val driver = getWebDriver();
					val actions = new Actions(driver);
					val by = By.xpath(".//ul[contains(@class,'htlPlnRmTypLst')]/li");

					for (val element : m_element.findElements(by)) {
						add(new _00100() {
							{
								// -------------------------------------------------------
								// プランまでスクロールしないと金額が取得できない
								// -------------------------------------------------------
								actions.moveToElement(m_element = element);
								actions.perform();
							}
						});
					}
				}
			};
		}
	}

	static class _00100 extends WebClient {
		WebElement m_element;

		String getHotelCode() throws Exception {
			val by = By.name("f_no");
			val element = m_element.findElement(by);
			return element.getAttribute("value");
		}

		String getPlanCode() throws Exception {
			val by = By.name("f_camp_id");
			val element = m_element.findElement(by);
			return element.getAttribute("value");
		}

		String getRoomCode() throws Exception {
			val by = By.name("f_syu");
			val element = m_element.findElement(by);
			return element.getAttribute("value");
		}

		String getRoomName() throws Exception {
			val by = By.xpath(".//*[@data-locate='roomType-name']");
			val element = m_element.findElement(by);
			return element.getText();
		}

		String getRoomInfo() throws Exception {
			val by = By.xpath(".//*[@data-locate='roomType-Info']");
			val element = m_element.findElement(by);
			return element.getText();
		}

		String getRoomRemark() throws Exception {
			val by = By.xpath(".//*[@data-locate='roomType-Remark']");
			val element = m_element.findElement(by);
			return element.getText();
		}

		String getRoomMeal() throws Exception {
			val by = By.xpath(".//*[@data-locate='roomType-option-meal']");
			val element = m_element.findElement(by);
			return element.getText();
		}

		String getRoomPeople() throws Exception {
			val by = By.xpath(".//*[@data-locate='roomType-option-people']");
			val element = m_element.findElement(by);
			return element.getText();
		}

		String getRoomPayment() throws Exception {
			val by = By.xpath(".//*[@data-locate='roomType-option-payment']");
			val element = m_element.findElement(by);
			return element.getText();
		}

		String getOriginalPrice() throws Exception {
			val by = By.xpath(".//*[contains(@class,'originalPrice')]");
			val element = m_element.findElement(by);
			return element.getText();
		}

		String getDiscountedPrice() throws Exception {
			val by = By.xpath(".//*[contains(@class,'discountedPrice')]");
			val element = m_element.findElement(by);
			return element.getText();
		}

		@Override
		public WebClient submit() throws Exception {
			System.out.println(getDiscountedPrice());
			return null;
		}
	}
}
