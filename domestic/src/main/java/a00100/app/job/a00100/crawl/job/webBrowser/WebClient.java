package a00100.app.job.a00100.crawl.job.webBrowser;

import org.openqa.selenium.WebDriver;

public class WebClient {
	public WebDriver getWebDriver() throws Exception {
		return WebBrowser.getCurrent().getWebDriver();
	}

	public WebClient execute() throws Exception {
		navigate();
		return submit();
	}

	public void navigate() throws Exception {
	}

	public WebClient submit() throws Exception {
		return null;
	}
}
