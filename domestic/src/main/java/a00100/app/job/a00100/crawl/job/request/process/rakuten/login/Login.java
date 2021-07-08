package a00100.app.job.a00100.crawl.job.request.process.rakuten.login;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

import a00100.app.job.a00100.crawl.job.request.process.Process;
import a00100.app.job.a00100.crawl.job.request.process.rakuten.query.Query;
import a00100.app.job.a00100.crawl.job.webBrowser.WebClient;
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

	Login() {
	}

	public static Login getInstance() {
		return m_instances.get();
	}

	@Override
	public WebClient execute() throws Exception {
		try {
			for (WebClient client = new _00000(); client != null;) {
				client = client.execute();
			}

			return next();
		} finally {
			m_instances.remove();
		}
	}

	WebClient next() {
		return Query.getInstance();
	}

	static class _00000 extends WebClient {
		@Override
		public void navigate() throws Exception {
			val driver = getWebDriver();
			driver.get("https://travel.rakuten.co.jp/");
		}

		@Override
		public WebClient submit() throws Exception {
			return new _00100();
		}
	}

	static class _00100 extends WebClient {
		@Override
		public WebClient submit() throws Exception {
			pushLogout();
			return new _00200();
		}

		void pushLogout() throws Exception {
			// --------------------------------------------------
			// 【ログアウト】押下
			// --------------------------------------------------
			val driver = getWebDriver();
			val by = By.xpath("//a[contains(text(),'ログアウト')]");

			for (val element : driver.findElements(by)) {
				element.click();
				break;
			}
		}
	}

	static class _00200 extends WebClient {
		@Override
		public WebClient submit() throws Exception {
			val process = Process.getCurrent();
			if (StringUtils.isEmpty(process.getUserId()) == true
					&& StringUtils.isEmpty(process.getPassword()) == true) {
				// --------------------------------------------------
				// ユーザID、パスワードが指定されていないのでログインの必要なし
				// --------------------------------------------------
				return null;
			}

			pushLogin();
			return new _00300();
		}

		void pushLogin() throws Exception {
			// --------------------------------------------------
			// 【ログイン】押下
			// --------------------------------------------------
			val driver = getWebDriver();
			val by = By.xpath("//a[contains(text(),'ログイン')]");

			for (val element : driver.findElements(by)) {
				element.click();
				break;
			}
		}
	}

	static class _00300 extends WebClient {
		@Override
		public WebClient submit() throws Exception {
			setUserId();
			setPassword();
			pushLogin();
			return null;
		}

		void setUserId() throws Exception {
			// --------------------------------------------------
			// ユーザID
			// --------------------------------------------------
			val process = Process.getCurrent();
			val driver = getWebDriver();
			val by = By.name("u");
			val element = driver.findElement(by);

			element.clear();
			element.sendKeys(process.getUserId());
		}

		void setPassword() throws Exception {
			// --------------------------------------------------
			// パスワード
			// --------------------------------------------------
			val process = Process.getCurrent();
			val driver = getWebDriver();
			val by = By.name("p");
			val element = driver.findElement(by);

			element.clear();
			element.sendKeys(process.getPassword());
		}

		void pushLogin() throws Exception {
			// --------------------------------------------------
			// 【ログイン】押下
			// --------------------------------------------------
			val driver = getWebDriver();
			val by = By.xpath("//input[@type='submit' and contains(@value,'ログイン')]");
			val element = driver.findElement(by);

			element.click();
		}
	}
}
