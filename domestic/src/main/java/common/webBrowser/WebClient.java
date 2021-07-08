package common.webBrowser;

import java.io.File;

import org.openqa.selenium.WebDriver;

public class WebClient {
	public WebDriver getWebDriver() throws Exception {
		return WebBrowser.getCurrent().getWebDriver();
	}

	public File getDownloadDirectory() throws Exception {
		return WebBrowser.getCurrent().getDownloadDirectory();
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
