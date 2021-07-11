package a00100.app.job.a00100.ikyu.job.request.process.crawl.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import a00100.app.job.a00100.ikyu.job.request.process.webBrowser.WebClient;
import a00100.app.job.a00100.ikyu.job.request.process.webBrowser.WebElementUtils;
import common.jdbc.JDBCUtils;
import common.lang.StringUtils;
import lombok.Getter;
import lombok.val;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
class Load extends WebClient {
	static final ThreadLocal<Load> m_instances = new ThreadLocal<Load>() {
		@Override
		protected Load initialValue() {
			return new Load();
		}
	};

	Load() {
	}

	static Load getInstance() {
		return m_instances.get();
	}

	public WebClient execute() throws Exception {
		try {
			delete();

			for (WebClient client = new _00000(); client != null;) {
				client = client.execute();
			}

/*
			for (val r : query()) {
				for (WebClient client = r; client != null;) {
					client = client.execute();
				}
			}
*/

			return next();
		} finally {
			m_instances.remove();
		}
	}

	WebClient next() {
		return null;
	}

	void delete() throws Exception {
		val conn = getConnection();
		JDBCUtils.truncateTable(conn, "temp_price_ikyu");
		JDBCUtils.commit(conn);
	}

	static class _00000 extends WebClient {
		@Override
		public WebClient submit() throws Exception {
			return new _00100();
		}

		void pushPlan() throws Exception {
			// --------------------------------------------------
			// 【部屋・プラン】押下
			// --------------------------------------------------
			val driver = getDriver();
			val by = By.xpath("//a[@href='#room-plan-list']");

			for (val element : driver.findElements(by)) {
				element.click();
			}
		}
	}

	static class _00100 extends WebClient {
		@Override
		public WebClient submit() throws Exception {
			pushMore();
			return new _00200();
		}

		void pushMore() throws Exception {
			// --------------------------------------------------
			// 【プランをもっとみる】押下
			// --------------------------------------------------
			val driver = getDriver();
			val actions = new Actions(driver);
			val by = By.xpath("//button[contains(@data-gaclickid,'PlanListDetailMore')]");

			for (val element : driver.findElements(by)) {
				actions.moveToElement(element);
				actions.perform();
				element.click();
			}
		}
	}

	static class _00200 extends WebClient {
		@Override
		public WebClient submit() throws Exception {
			for (val r : query()) {

			}

			return null;
		}

		Collection<_00300> query() throws Exception {
			return new ArrayList<_00300>() {
				{
					val driver = getDriver();
					val actions = new Actions(driver);
					val by = By.xpath("//div[contains(@class,'PlanList')]/ul/li");

					for (val element : driver.findElements(by)) {
						add(new _00300() {
							{
								actions.moveToElement(m_rootElement = element);
								actions.perform();
							}
						});
					}
				}
			};
		}
	}

	static class _00300 extends WebClient {
		@Getter
		WebElement m_rootElement;

	}

	Collection<_00800> query() throws Exception {
		val driver = getDriver();
		val by = By.xpath("//a[@href='#room-plan-list']");

		driver.findElement(by).click();

		return new ArrayList<_00800>() {
			{
				val actions = new Actions(driver);
				val by = By.xpath("//div[contains(@class,'PlanList')]/ul/li");


//				driver.findElement(By.xpath("//a[@href='#room-plan-list']")).click();

//				Thread.sleep(1000);

//				System.out.println(driver.findElements(by).size());

				for (val element : driver.findElements(by)) {
					add(new _00800() {
						{
							//actions.moveToElement(m_rootElement = element);
							//actions.perform();
						}
					});
				}
			}
		};
	}

	static class _00800 extends WebClient {
		@Getter
		WebElement m_rootElement;

		String getHotelCode() throws Exception {
			val driver = getDriver();
			val by = By.name("yadNo");
			return driver.findElement(by).getAttribute("value");
		}

		String getHotelName() throws Exception {
			val driver = getDriver();
			val by = By.xpath("//*[@id='yado_header_hotel_name']//child::*[1]");
			return driver.findElement(by).getText();
		}

		String getPlanCode() {
			return getRootElement().getAttribute("data-plancode");
		}

		String getPlanName() {
			val by = By.xpath(".//p[contains(@class,'p-searchResultItem__catchPhrase')]");
			return WebElementUtils.getText(getRootElement(), by);
		}

		public WebClient submit() throws Exception {
System.out.println(getRootElement().getAttribute("id"));
			return null;
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

		Collection<_00900> query() throws Exception {
			return new ArrayList<_00900>() {
				{
					val driver = getDriver();
					val actions = new Actions(driver);
					val by = By.xpath(".//ul[contains(@class,'htlPlnRmTypLst')]/li");

					for (val element : getRootElement().findElements(by)) {
						add(new _00900() {
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

	static class _00900 extends WebClient {
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
