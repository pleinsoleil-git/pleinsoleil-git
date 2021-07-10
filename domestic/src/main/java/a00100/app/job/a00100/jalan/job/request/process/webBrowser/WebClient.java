package a00100.app.job.a00100.jalan.job.request.process.webBrowser;

import org.openqa.selenium.WebDriver;

import a00100.app.job.a00100.jalan.job.request.process.connection.Connection;
import common.jdbc.JDBCConnection;

public class WebClient {
	public WebDriver getDriver() throws Exception {
		return WebBrowser.getWebDriver();
	}

	public JDBCConnection getConnection() {
		return Connection.app();
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
