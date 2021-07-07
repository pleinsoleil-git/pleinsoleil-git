package a00100.app.job.a00100.crawl.rakuten.job.request.login;

import a00100.app.job.a00100.crawl.rakuten.job.webBrowser.WebClient;
import lombok.val;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = true)
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
			driver.get("https://travel.rakuten.co.jp/");
		}
	}
}
