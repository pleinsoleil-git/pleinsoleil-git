package a00100.app.job.a00100.ikyu.job.request.process.crawl.login;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

import a00100.app.job.a00100.ikyu.job.request.process.crawl.Crawl;
import a00100.app.job.a00100.ikyu.job.request.process.webBrowser.WebClient;
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

			return null;
		} finally {
			m_instances.remove();
		}
	}

	static class _00000 extends WebClient {
		@Override
		public void navigate() throws Exception {
			val driver = getDriver();
			driver.get("https://www.ikyu.com");
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
			val driver = getDriver();
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
			val crawl = Crawl.getCurrent();

			if (StringUtils.isEmpty(crawl.getUserId()) == true
					&& StringUtils.isEmpty(crawl.getPassword()) == true) {
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
			val driver = getDriver();
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
			val driver = getDriver();
			val handle = driver.getWindowHandle();

			try {
				// --------------------------------------------------
				// 【ログイン】ポップアップに切替
				// --------------------------------------------------
				for (val h : driver.getWindowHandles()) {
					driver.switchTo().window(h);

					val by = By.id("text_member_id");
					if (driver.findElements(by).isEmpty() == false) {
						break;
					}
				}

				setUserId();
				setPassword();
				pushKeepLoggedIn();
				pushLogin();
			} finally {
				// 元ウィンドウに戻す
				driver.switchTo().window(handle);
			}

			return null;
		}

		void setUserId() throws Exception {
			// --------------------------------------------------
			// ユーザID
			// --------------------------------------------------
			val crawl = Crawl.getCurrent();
			val driver = getDriver();
			val by = By.id("text_member_id");
			val element = driver.findElement(by);

			element.clear();
			element.sendKeys(crawl.getUserId());
		}

		void setPassword() throws Exception {
			// --------------------------------------------------
			// パスワード
			// --------------------------------------------------
			val crawl = Crawl.getCurrent();
			val driver = getDriver();
			val by = By.id("password_member_password");
			val element = driver.findElement(by);

			element.clear();
			element.sendKeys(crawl.getPassword());
		}

		void pushKeepLoggedIn() throws Exception {
			// --------------------------------------------------
			// 【ログイン状態を保持】チェックを外す
			// --------------------------------------------------
			val driver = getDriver();
			val by = By.xpath("//input[@type='checkbox' and contains(@class,'checkbox_IwesW')]");
			val element = driver.findElement(by);

			if (BooleanUtils.toBoolean(element.getAttribute("value")) == true) {
				element.click();
			}
		}

		void pushLogin() throws Exception {
			// --------------------------------------------------
			// 【ログイン】押下
			// --------------------------------------------------
			val driver = getDriver();
			val by = By.id("button_auth_member");
			val element = driver.findElement(by);

			element.click();
		}
	}
}
