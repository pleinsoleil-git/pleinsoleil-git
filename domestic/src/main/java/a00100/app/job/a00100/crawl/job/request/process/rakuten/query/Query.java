package a00100.app.job.a00100.crawl.job.request.process.rakuten.query;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.Select;

import a00100.app.job.a00100.crawl.job.request.process.Process;
import a00100.app.job.a00100.crawl.job.webBrowser.WebClient;
import lombok.val;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
public class Query extends WebClient {
	static final ThreadLocal<Query> m_instances = new ThreadLocal<Query>() {
		@Override
		protected Query initialValue() {
			return new Query();
		}
	};
	_Current m_current;

	Query() {
	}

	public static Query getInstance() {
		return m_instances.get();
	}

	public static _Current getCurrent() {
		return getInstance().m_current;
	}

	public WebClient execute() throws Exception {
		try {
			for (m_current = new _00000(); m_current != null;) {
				m_current = (_Current) m_current.execute();
			}

			return Plan.getInstance();
		} finally {
			m_instances.remove();
		}
	}

	public static class _Current extends WebClient {
	}

	static class _00000 extends _Current {
		@Override
		public void navigate() throws Exception {
			val driver = getWebDriver();
			val process = Process.getCurrent();

			driver.get(String.format("https://hotel.travel.rakuten.co.jp/hotelinfo/plan/%s", process.getHotelCode()));
		}

		@Override
		public WebClient submit() throws Exception {
			pushDate();
			setCheckin();
			setCheckout();
			setRoom();
			setAdult();
			//setChild();
			pushQuery();
			return null;
		}

		void pushDate() throws Exception {
			// --------------------------------------------------
			// 日付未定のチェックを外す
			// --------------------------------------------------
			val driver = getWebDriver();
			val by = By.id("dh-unspecified-date");
			val element = driver.findElement(by);

			if (element.isSelected() == true) {
				element.click();
			}
		}

		void setCheckin() throws Exception {
			// --------------------------------------------------
			// チェックイン
			// --------------------------------------------------
			val driver = getWebDriver();
			val by = By.id("dh-checkin");
			val element = driver.findElement(by);
			val process = Process.getCurrent();

			element.sendKeys(Keys.ESCAPE);
			element.sendKeys(Keys.CONTROL, "a");
			element.sendKeys(Keys.DELETE);
			element.sendKeys(process.getCheckInDate());
		}

		void setCheckout() throws Exception {
			// --------------------------------------------------
			// チェックアウト
			// --------------------------------------------------
			val driver = getWebDriver();
			val by = By.id("dh-checkout");
			val element = driver.findElement(by);
			val process = Process.getCurrent();

			element.sendKeys(Keys.ESCAPE);
			element.sendKeys(Keys.CONTROL, "a");
			element.sendKeys(Keys.DELETE);
			element.sendKeys(process.getCheckOutDate());
		}

		void setRoom() throws Exception {
			// --------------------------------------------------
			// ご利用部屋数
			// --------------------------------------------------
			val driver = getWebDriver();
			val by = By.id("dh-room");
			val element = new Select(driver.findElement(by));
			val process = Process.getCurrent();

			element.selectByValue(String.format("%d", process.getRoomNums()));
		}

		void setAdult() throws Exception {
			// --------------------------------------------------
			// 大人人数
			// --------------------------------------------------
			val driver = getWebDriver();
			val by = By.id("dhAdult1");
			val element = new Select(driver.findElement(by));
			val process = Process.getCurrent();

			element.selectByValue(String.format("%d", process.getAdultNums()));
		}

		void setChild() throws Exception {
			// --------------------------------------------------
			// 子供人数
			// --------------------------------------------------
			val driver = getWebDriver();
			val by = By.id("chldNum1");
			val element = driver.findElement(by);

			element.click();


			val b = By.id("dh-s1");
			val e = new Select(driver.findElement(b));
			e.selectByValue("1");

			element.click();
		}

		void pushQuery() throws Exception {
			// --------------------------------------------------
			// 【検索】押下
			// --------------------------------------------------
			val driver = getWebDriver();
			val by = By.id("dh-submit");
			val element = driver.findElement(by);

			element.click();
		}
	}
}
