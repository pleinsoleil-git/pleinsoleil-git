package a00100.app.job.a00100.jalan.job.request.process.webBrowser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import lombok.val;

public class WebElementUtils {
	public static String getAttribute(final WebElement element, final By by, final String name) {
		for (val e : element.findElements(by)) {
			return e.getAttribute(name);
		}

		return null;
	}

	public static String getValue(final WebElement element, final By by) {
		return getAttribute(element, by, "value");
	}

	public static String getText(final WebElement element, final By by) {
		for (val e : element.findElements(by)) {
			return e.getText();
		}

		return null;
	}
}
