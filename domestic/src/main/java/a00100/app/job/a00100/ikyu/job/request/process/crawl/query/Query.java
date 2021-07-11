package a00100.app.job.a00100.ikyu.job.request.process.crawl.query;

import java.util.ArrayList;

import org.apache.tomcat.util.buf.StringUtils;

import a00100.app.job.a00100.ikyu.job.request.process.crawl.Crawl;
import a00100.app.job.a00100.ikyu.job.request.process.webBrowser.WebClient;
import lombok.val;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
public class Query extends WebClient {
	static final ThreadLocal<Query> m_instances = new ThreadLocal<Query>() {
		@Override
		protected Query initialValue() {
			return new Query();
		}
	};

	Query() {
	}

	public static Query getInstance() {
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
		return null;
	}

	static class _00000 extends WebClient {
		public void navigate() throws Exception {
			val URL = "https://www.ikyu.com/%s/?";
			val crawl = Crawl.getCurrent();
			val url = String.format(URL, crawl.getHotelCode()) +
					StringUtils.join(new ArrayList<String>() {
						{
							add(String.format("cid=%s", crawl.getCheckInDate()));
							add(String.format("cod=%s", crawl.getCheckOutDate()));
							add(String.format("rc=%s", crawl.getRoomNums()));
							add(String.format("ppc=%s", crawl.getAdultNums()));
							add(String.format("cac=%s", crawl.getUpperGradeNums()));
							add(String.format("cbc=%s", crawl.getLowerGradeNums()));
						}
					}, '&');

			val driver = getDriver();
			driver.get(url);
		}
	}
}
