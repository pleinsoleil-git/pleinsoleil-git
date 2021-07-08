package a00100.app.job.a00100.crawl.job.request.process.rakuten.login;

import org.openqa.selenium.By;

import a00100.app.job.a00100.crawl.job.request.process.rakuten.query.Query;
import common.webBrowser.WebClient;
import lombok.val;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
public class Login extends WebClient {
	static final ThreadLocal<Login> m_instances = new ThreadLocal<Login>() {
		@Override
		protected Login initialValue() {
			return new Login();
		}
	};
	_Current m_current;

	Login() {
	}

	public static Login getInstance() {
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

			return Query.getInstance();
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
			driver.get("https://travel.rakuten.co.jp/");
		}

		@Override
		public WebClient submit() throws Exception {
			pushLogin();
			return new _00100();
		}

		void pushLogin() throws Exception {
			val driver = getWebDriver();
			val by = By.xpath("//a[contains(text(),'ログイン')]");

			for (val element : driver.findElements(by)) {
				element.click();
				break;
			}
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
