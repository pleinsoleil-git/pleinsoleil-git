package a00100.app.job.a00100.crawl.rakuten.job.webBrowser;

import java.io.File;

import a00100.app.job.a00100.crawl.rakuten.job.Job;
import lombok.val;

public class WebBrowser implements AutoCloseable {
	static WebBrowser m_instance;

	static {
		val job = Job.getCurrent();
		val driver = new File(new File(System.getProperty("catalina.home"), "lib"), job.getWebDriver());
		System.setProperty("webdriver.chrome.driver", driver.getPath());
	}

	WebBrowser() {
	}

	public static WebBrowser getInstance() {
		return (m_instance == null ? m_instance = new WebBrowser() : m_instance);
	}

	@Override
	public void close() throws Exception {
	}
}
