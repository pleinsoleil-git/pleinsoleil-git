package common.webBrowser;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

import lombok.val;

public class WebBrowser {
	static {
		File driver = null;
		val p = Pattern.compile("chromedriver\\..*\\.exe", Pattern.CASE_INSENSITIVE);

		for (val file : new File(System.getProperty("catalina.home"), "lib").listFiles(new FilenameFilter() {
			{
			}

			@Override
			public boolean accept(final File dir, final String name) {
				return p.matcher(name).find();
			}
		})) {
			driver = file;
		}

		System.setProperty("webdriver.chrome.driver", driver.getPath());
	}
}
