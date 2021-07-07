package a00100.app.job.a00100.crawl.rakuten.job.request.hotel;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.Select;

import a00100.app.job.a00100.crawl.rakuten.job.webBrowser.WebClient;
import lombok.val;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = true)
public class Hotel extends WebClient {
	static final ThreadLocal<Hotel> m_instances = new ThreadLocal<Hotel>() {
		@Override
		protected Hotel initialValue() {
			return new Hotel();
		}
	};
	_Current m_current;

	Hotel() {
	}

	public static Hotel getInstance() {
		return m_instances.get();
	}

	public static _Current getCurrent() {
		return getInstance().m_current;
	}

	public WebClient execute() throws Exception {
		try {
			for (WebClient client = new _00000(); client != null;) {
				client = client.execute();
			}

			return null;
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
			driver.get("https://hotel.travel.rakuten.co.jp/hotelinfo/plan/138037");
		}

		@Override
		public WebClient submit() throws Exception {
			pushDate();
			setCheckin();
			setCheckout();
			setRoom();
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

			element.sendKeys(Keys.ESCAPE);
			element.sendKeys(Keys.CONTROL, "a");
			element.sendKeys(Keys.DELETE);
			element.sendKeys("2021/08/03");
		}

		void setCheckout() throws Exception {
			// --------------------------------------------------
			// チェックアウト
			// --------------------------------------------------
			val driver = getWebDriver();
			val by = By.id("dh-checkout");
			val element = driver.findElement(by);

			element.sendKeys(Keys.ESCAPE);
			element.sendKeys(Keys.CONTROL, "a");
			element.sendKeys(Keys.DELETE);
			element.sendKeys("2021/08/10");
		}

		void setRoom() throws Exception {
			// --------------------------------------------------
			// ご利用部屋数
			// --------------------------------------------------
			val driver = getWebDriver();
			val by = By.id("dh-room");
			val element = new Select(driver.findElement(by));

			element.selectByValue("2");
		}
	}

	static class _00100 extends _Current {
		@Override
		public WebClient submit() throws Exception {
			setUserId();
			setPassword();
			pushLogin();
			return null;
		}

		void setUserId() throws Exception {
			val driver = getWebDriver();
			val by = By.name("u");
			val element = driver.findElement(by);

			element.clear();
			element.sendKeys("money.hideki.nakayama@gmail.com");
		}

		void setPassword() throws Exception {
			val driver = getWebDriver();
			val by = By.name("p");
			val element = driver.findElement(by);

			element.clear();
			element.sendKeys("123Qwe@asd");
		}

		void pushLogin() throws Exception {
			val driver = getWebDriver();
			val by = By.xpath("//input[@type='submit' and contains(@value,'ログイン')]");
			val element = driver.findElement(by);

			element.click();
		}
	}
}
