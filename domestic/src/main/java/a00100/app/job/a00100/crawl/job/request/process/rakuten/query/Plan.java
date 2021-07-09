package a00100.app.job.a00100.crawl.job.request.process.rakuten.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import a00100.app.job.a00100.crawl.Connection;
import a00100.app.job.a00100.crawl.job.webBrowser.WebClient;
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

				break;
				}
			}
		};
	}

	static class _00000 extends WebClient {
		WebElement m_element;

		String getText(final By by) {
			for (val e : m_element.findElements(by)) {
				return e.getText();
			}

			return null;
		}

		String getHotelName() {
			val by = By.xpath("//a[contains(@class,'rtconds')]");
			return getText(by);
		}

		String getPlanName() {
			val by = By.xpath(".//child::*[1]");
			return getText(by);
		}

		public WebClient submit() throws Exception {
			insert();
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
							+ "?::VARCHAR AS room_name,\n"
							+ "?::VARCHAR AS room_info,\n"
							+ "?::VARCHAR AS room_remark,\n"
							+ "?::VARCHAR AS room_option_meal,\n"
							+ "?::NUMERIC AS original_price,\n"
							+ "?::NUMERIC AS discounted_price\n"
					+ ")\n"
					+ "INSERT INTO t_price_rakuten\n"
					+ "(\n"
						+ "hotel_code,\n"
						+ "hotel_name,\n"
						+ "plan_code,\n"
						+ "plan_name,\n"
						+ "room_code,\n"
						+ "room_name,\n"
						+ "room_info,\n"
						+ "room_remark,\n"
						+ "room_option_meal,\n"
						+ "original_price,\n"
						+ "discounted_price\n"
					+ ")\n"
					+ "SELECT t10.hotel_code,\n"
						+ "t10.hotel_name,\n"
						+ "t10.plan_code,\n"
						+ "t10.plan_name,\n"
						+ "t10.room_code,\n"
						+ "t10.room_name,\n"
						+ "t10.room_info,\n"
						+ "t10.room_remark,\n"
						+ "t10.room_option_meal,\n"
						+ "t10.original_price,\n"
						+ "t10.discounted_price\n"
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
					stmt.setString(colNum++, r.getRoomInfo());
					stmt.setString(colNum++, r.getRoomRemark());
					stmt.setString(colNum++, r.getRoomOptionMeal());
					stmt.setString(colNum++, r.getOriginalPrice());
					stmt.setString(colNum++, r.getDiscountedPrice());
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

		String getText(final WebElement element, final By by) {
			String v=null;
			for (val e : element.findElements(by)) {
				v=e.getText();
			}

			return v;
		}

		String getValue(final WebElement element, final By by) {
			for (val e : element.findElements(by)) {
				return e.getAttribute("value");
			}

			return null;
		}

		String getHotelCode() throws Exception {
			val by = By.xpath(".//input[@type='hidden' and @name='f_no']");
			return getValue(getFormElement(), by);
		}

		String getPlanCode() throws Exception {
			val by = By.xpath(".//input[@type='hidden' and @name='f_camp_id']");
			return getValue(getFormElement(), by);
		}

		String getReserveUrl() throws Exception {
			val params = new ArrayList<String>() {
				{
					add("f_dhr_rsv_pgm=ry_kensaku");

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
						val value = getValue(getFormElement(), by);
						add(String.format("%s=%s", StringUtils.defaultString(entry.getValue(), entry.getKey()), StringUtils.defaultString(value)));
					}
				}
			};

			return StringUtils.join(params, "&");
		}

		String getRoomCode() throws Exception {
			val by = By.xpath(".//input[@type='hidden' and @name='f_syu']");
			return getValue(getFormElement(), by);
		}

		String getRoomName() throws Exception {
			val by = By.xpath(".//*[@data-locate='roomType-name']");
			return getText(getRootElement(), by);
		}

		String getRoomInfo() throws Exception {
			val by = By.xpath(".//*[@data-locate='roomType-Info']");
			return getText(getRootElement(), by);
		}

		String getRoomRemark() throws Exception {
			val by = By.xpath(".//*[@data-locate='roomType-Remark']");
			return getText(getRootElement(), by);
		}

		String getRoomOptionMeal() throws Exception {
			// --------------------------------------------------
			// 食事以降を取得
			// --------------------------------------------------
			return StringUtils.join(new ArrayList<String>() {
				{
					val by = By.xpath(".//*[@data-locate='roomType-option-meal']");
					val values = StringUtils.split(getText(getRootElement(), by), StringUtils.SPACE);
					for (int i = 1; i < values.length; i++) {
						add(values[i]);
					}
				}
			}, StringUtils.SPACE);
		}

		String getRoomOptionPeople() throws Exception {
			val by = By.xpath(".//*[@data-locate='roomType-option-people']");
			return getText(getRootElement(), by);
		}

		String getRoomOptionPayment() throws Exception {
			val by = By.xpath(".//*[@data-locate='roomType-option-payment']");
			return getText(getRootElement(), by);
		}

		String getOriginalPrice() throws Exception {
			val by = By.xpath(".//*[contains(@class,'originalPrice')]");
			val value = getText(getRootElement(), by);
			val p = Pattern.compile("[\\d,]+");
			val m = p.matcher(value);

			if (m.find() == true) {
				return StringUtils.remove(m.group(), ',');
			}

			return null;
		}

		String getDiscountedPrice() throws Exception {
			val by = By.xpath(".//*[contains(@class,'discountedPrice')]");
			val value = getText(getRootElement(), by);
			val p = Pattern.compile("[\\d,]+");
			val m = p.matcher(value);

			if (m.find() == true) {
				return StringUtils.remove(m.group(), ',');
			}

			return null;
		}
	}
}
