package a00100.app.job.a00100.rakuten.job.request.process.crawl.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import a00100.app.job.a00100.rakuten.job.request.process.webBrowser.WebClient;
import a00100.app.job.a00100.rakuten.job.request.process.webBrowser.WebElementUtils;
import common.jdbc.JDBCUtils;
import common.lang.StringUtils;
import lombok.Getter;
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

	Collection<_00000> query() throws Exception {
		return new ArrayList<_00000>() {
			{
				val driver = getDriver();
				val actions = new Actions(driver);
				val by = By.xpath("//div[@class='planList']/ul/li");

				for (val element : driver.findElements(by)) {
					add(new _00000() {
						{
							// -------------------------------------------------------
							// プランまでスクロールしないと金額が取得できない
							// -------------------------------------------------------
							actions.moveToElement(m_rootElement = element);
							actions.perform();
						}
					});

				break;
				}
			}
		};
	}

	static class _00000 extends WebClient {
		@Getter
		WebElement m_rootElement;

		String getHotelName() {
			val by = By.xpath("//a[contains(@class,'rtconds')]");
			return WebElementUtils.getText(getRootElement(), by);
		}

		String getPlanName() {
			val by = By.xpath(".//child::*[1]");
			return WebElementUtils.getText(getRootElement(), by);
		}

		public WebClient submit() throws Exception {
			delete();
			insert();
			return null;
		}

		void delete() throws Exception {
			val conn = getConnection();
			JDBCUtils.truncateTable(conn, "temp_price_rakuten");
			JDBCUtils.commit(conn);
		}

		void insert() throws Exception {
			val conn = getConnection();

			try (val stmt = JDBCUtils.createStatement(conn)) {
				String sql;
				sql = "WITH s_params AS\n"
					+ "(\n"
						+ "SELECT ?::VARCHAR AS hotel_code,\n"
							+ "?::VARCHAR AS hotel_name,\n"
							+ "?::VARCHAR AS plan_code,\n"
							+ "?::VARCHAR AS plan_name,\n"
							+ "?::VARCHAR AS plan_url,\n"
							+ "?::VARCHAR AS room_code,\n"
							+ "?::VARCHAR AS room_name,\n"
							+ "?::VARCHAR AS room_info,\n"
							+ "?::VARCHAR AS room_remark,\n"
							+ "?::VARCHAR AS room_option_meal,\n"
							+ "?::VARCHAR AS room_option_people,\n"
							+ "?::VARCHAR AS room_option_payment,\n"
							+ "?::VARCHAR AS point_rate,\n"
							+ "?::VARCHAR AS price,\n"
							+ "?::VARCHAR AS original_price,\n"
							+ "?::VARCHAR AS discounted_price,\n"
							+ "?::VARCHAR AS per_person_price\n"
					+ ")\n"
					+ "INSERT INTO temp_price_rakuten\n"
					+ "(\n"
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
					+ "SELECT t10.hotel_code,\n"
						+ "t10.hotel_name,\n"
						+ "t10.plan_code,\n"
						+ "t10.plan_name,\n"
						+ "t10.plan_url,\n"
						+ "t10.room_code,\n"
						+ "t10.room_name,\n"
						+ "t10.room_remark,\n"
						+ "t10.room_info,\n"
						+ "t10.room_option_meal,\n"
						+ "t10.room_option_people,\n"
						+ "t10.room_option_payment,\n"
						+ "t10.point_rate,\n"
						+ "t10.price,\n"
						+ "t10.original_price,\n"
						+ "t10.discounted_price,\n"
						+ "t10.per_person_price\n"
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
					stmt.setString(colNum++, r.getPlanUrl());
					stmt.setString(colNum++, r.getRoomCode());
					stmt.setString(colNum++, r.getRoomName());
					stmt.setString(colNum++, r.getRoomInfo());
					stmt.setString(colNum++, r.getRoomRemark());
					stmt.setString(colNum++, r.getRoomOptionMeal());
					stmt.setString(colNum++, r.getRoomOptionPeople());
					stmt.setString(colNum++, r.getRoomOptionPayment());
					stmt.setString(colNum++, r.getPointRate());
					stmt.setString(colNum++, r.getPrice());
					stmt.setString(colNum++, r.getOriginalPrice());
					stmt.setString(colNum++, r.getDiscountedPrice());
					stmt.setString(colNum++, r.getPerPersonPrice());
					stmt.addBatch();
				}

				stmt.executeBatchAndClear();
			} finally {
				JDBCUtils.commit(conn);
			}
		}

		Collection<_00100> query() throws Exception {
			return new ArrayList<_00100>() {
				{
					val driver = getDriver();
					val actions = new Actions(driver);
					val by = By.xpath(".//ul[contains(@class,'htlPlnRmTypLst')]/li");

					for (val element : getRootElement().findElements(by)) {
						add(new _00100() {
							{
								// -------------------------------------------------------
								// プランまでスクロールしないと金額が取得できない
								// -------------------------------------------------------
								actions.moveToElement(m_rootElement = element);
								actions.perform();
							}
						});
					}
				}
			};
		}
	}

	static class _00100 extends WebClient {
		@Getter
		WebElement m_rootElement;

		WebElement m_formElement;

		WebElement getFormElement() {
			if (m_formElement == null) {
				val by = By.xpath(".//form");
				m_formElement = getRootElement().findElement(by);
			}

			return m_formElement;
		}

		String getHotelCode() throws Exception {
			val by = By.xpath(".//input[@type='hidden' and @name='f_no']");
			return WebElementUtils.getValue(getFormElement(), by);
		}

		String getPlanCode() throws Exception {
			val by = By.xpath(".//input[@type='hidden' and @name='f_camp_id']");
			return WebElementUtils.getValue(getFormElement(), by);
		}

		String getPlanUrl() throws Exception {
			val params = new ArrayList<String>() {
				{
					for (val entry : new LinkedHashMap<String, String>() {
						{
							for (val v : new String[][] {
									{ "f_dhr_rsv_pgm", "ry_kensaku" },
									{ "f_adult_su", "1" },
							}) {
								put(v[0], v[1]);
							}
						}
					}.entrySet()) {
						add(String.format("%s=%s", entry.getKey(), entry.getValue()));
					}

					for (val entry : new LinkedHashMap<String, String>() {
						{
							for (val v : new String[][] {
									{ "f_no", null },
									{ "f_syu", null },
									{ "f_hi1", null },
									{ "f_hi2", null },
									{ "f_s1", null },
									{ "f_s2", null },
									{ "f_y1", null },
									{ "f_y2", null },
									{ "f_y3", null },
									{ "f_y4", null },
									{ "f_heya_su", null },
									{ "f_ninzu", null },
									{ "f_teikei", null },
									{ "f_camp_id", null },
									{ "f_otona_su", null },
									{ "f_id_yami", null },
									{ "f_service", null },
							}) {
								put(v[0], v[1]);
							}
						}
					}.entrySet()) {
						val by = By.xpath(String.format(".//input[@type='hidden' and @name='%s']", entry.getKey()));
						val value = WebElementUtils.getValue(getFormElement(), by);
						add(String.format("%s=%s", StringUtils.defaultString(entry.getValue(), entry.getKey()), StringUtils.defaultString(value)));
					}
				}
			};

			return "https://rsvh.travel.rakuten.co.jp/rsv/RsvLogin.do?" + StringUtils.join(params, "&");
		}

		String getRoomCode() throws Exception {
			val by = By.xpath(".//input[@type='hidden' and @name='f_syu']");
			return WebElementUtils.getValue(getFormElement(), by);
		}

		String getRoomName() throws Exception {
			val by = By.xpath(".//*[@data-locate='roomType-name']");
			return WebElementUtils.getText(getRootElement(), by);
		}

		String getRoomInfo() throws Exception {
			val by = By.xpath(".//*[@data-locate='roomType-Info']");
			return WebElementUtils.getText(getRootElement(), by);
		}

		String getRoomRemark() throws Exception {
			val by = By.xpath(".//*[@data-locate='roomType-Remark']");
			return WebElementUtils.getText(getRootElement(), by);
		}

		String getRoomOptionMeal() throws Exception {
			// --------------------------------------------------
			// 食事以降を取得
			// --------------------------------------------------
			return StringUtils.join(new ArrayList<String>() {
				{
					val by = By.xpath(".//*[@data-locate='roomType-option-meal']");
					val values = StringUtils.split(WebElementUtils.getText(getRootElement(), by), StringUtils.SPACE);

					for (int i = 1; i < values.length; i++) {
						add(values[i]);
					}
				}
			}, StringUtils.SPACE);
		}

		String getRoomOptionPeople() throws Exception {
			// --------------------------------------------------
			// 人数以降を取得
			// --------------------------------------------------
			return StringUtils.join(new ArrayList<String>() {
				{
					val by = By.xpath(".//*[@data-locate='roomType-option-people']");
					val values = StringUtils.split(WebElementUtils.getText(getRootElement(), by), StringUtils.SPACE);

					for (int i = 1; i < values.length; i++) {
						add(values[i]);
					}
				}
			}, StringUtils.SPACE);
		}

		String getRoomOptionPayment() throws Exception {
			// --------------------------------------------------
			// 決済以降を取得
			// --------------------------------------------------
			return StringUtils.join(new ArrayList<String>() {
				{
					val by = By.xpath(".//*[@data-locate='roomType-option-payment']");
					val values = StringUtils.split(WebElementUtils.getText(getRootElement(), by), StringUtils.SPACE);

					for (int i = 1; i < values.length; i++) {
						for (val v : StringUtils.split(values[i], "／")) {
							add(v);
						}
					}
				}
			}, StringUtils.SPACE);
		}

		String getPointRate() throws Exception {
			val by = By.xpath(".//*[@data-locate='plan-point-info']");
			val value = WebElementUtils.getText(getRootElement(), by);

			if (StringUtils.isNotEmpty(value) == true) {
				val p = Pattern.compile("[^\\d]+");
				val m = p.matcher(value);
				return m.replaceAll("");
			}

			return null;
		}

		String getPrice() throws Exception {
			val by = By.xpath(".//*[contains(@class,'ndPrice')]");
			val value = WebElementUtils.getText(getRootElement(), by);

			if (StringUtils.isNotEmpty(value) == true) {
				val p = Pattern.compile("[^\\d]+");
				val m = p.matcher(value);
				return m.replaceAll("");
			}

			return null;
		}

		String getOriginalPrice() throws Exception {
			val by = By.xpath(".//*[contains(@class,'originalPrice')]");
			val value = WebElementUtils.getText(getRootElement(), by);

			if (StringUtils.isNotEmpty(value) == true) {
				val p = Pattern.compile("[^\\d]+");
				val m = p.matcher(value);
				return m.replaceAll("");
			}

			return null;
		}

		String getDiscountedPrice() throws Exception {
			val by = By.xpath(".//*[contains(@class,'discountedPrice')]");
			val value = WebElementUtils.getText(getRootElement(), by);

			if (StringUtils.isNotEmpty(value) == true) {
				val p = Pattern.compile("[^\\d]+");
				val m = p.matcher(value);
				return m.replaceAll("");
			}

			return null;
		}

		String getPerPersonPrice() throws Exception {
			val by = By.xpath(".//*[contains(@class,'prcPerPerson')]");
			val values = StringUtils.split(WebElementUtils.getText(getRootElement(), by), "人");

			for (int i = 1; i < values.length;) {
				val p = Pattern.compile("[^\\d]+");
				val m = p.matcher(values[i]);
				return m.replaceAll("");
			}

			return null;
		}
	}
}
