package a00100.app.job.a00100.crawl.rakuten.job.webBrowser;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import a00100.app.job.a00100.crawl.rakuten.job.Job;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebBrowser implements AutoCloseable {
	static WebBrowser m_instance;
	ConcurrentHashMap<Long, _Current> m_currents;

	static {
		val job = Job.getCurrent();
		val driver = new File(new File(System.getProperty("catalina.home"), "lib"), job.getWebDriver());
		System.setProperty("webdriver.chrome.driver", driver.getPath());
	}

	WebBrowser() {
		m_currents = new ConcurrentHashMap<Long, _Current>();
	}

	public static WebBrowser getInstance() {
		return (m_instance == null ? m_instance = new WebBrowser() : m_instance);
	}

	public static _Current getCurrent() {
		val k = Thread.currentThread().getId();
		_Current v = m_instance.m_currents.get(k);

		if (v == null) {
			m_instance.m_currents.put(k, v = new _Current());
		}

		return v;
	}

	@Override
	public void close() throws Exception {
		try {
			for (val v : m_currents.values()) {
				v.close();
			}
		} finally {
			m_currents.clear();
			m_instance = null;
		}
	}

	public static class _Current implements AutoCloseable {
		File m_directory;
		WebDriver m_webDriver;

		public WebDriver getWebDriver() throws Exception {
			if (m_webDriver == null) {
				m_webDriver = new ChromeDriver(new ChromeOptions() {
					{
						addArguments("--incognito"); // シークレットモード
						setExperimentalOption("prefs", new HashMap<String, Object>() {
							{
								// ==================================================
								// ダウンロード先を設定
								// ==================================================
								put("download.default_directory", getDownloadDirectory().getPath());
							}
						});
					}
				});
			}

			return m_webDriver;
		}

		public File getDownloadDirectory() throws Exception {
			if (m_directory == null) {
				m_directory = Files.createTempDirectory(null).toFile();
				log.debug(String.format("download.default_directory[%s]", m_directory.getPath()));
			}

			return m_directory;
		}

		@Override
		public void close() throws Exception {
		}
	}
}
