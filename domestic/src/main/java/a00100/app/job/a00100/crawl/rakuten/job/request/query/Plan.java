package a00100.app.job.a00100.crawl.rakuten.job.request.query;

import java.util.ArrayList;
import java.util.Collection;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import a00100.app.job.a00100.crawl.rakuten.job.webBrowser.Attributes;
import a00100.app.job.a00100.crawl.rakuten.job.webBrowser.WebClient;
import lombok.val;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Accessors(prefix = "m_", chain = true)
class Plan extends WebClient {
	static final ThreadLocal<Plan> m_instances = new ThreadLocal<Plan>() {
		@Override
		protected Plan initialValue() {
			return new Plan();
		}
	};
	_Current m_current;

	Plan() {
	}

	static Plan getInstance() {
		return m_instances.get();
	}

	static _Current getCurrent() {
		return getInstance().m_current;
	}

	public WebClient execute() throws Exception {
		try {
			for (val r : query()) {
				for (m_current = r; m_current != null;) {
					m_current = (_Current) m_current.execute();
				}
			}

			return null;
		} finally {
			m_instances.remove();
		}
	}

	Collection<_00000> query() throws Exception {
		return new ArrayList<_00000>() {
			{
				val driver = getWebDriver();
				val by = By.xpath("//ul[contains(@class,'htlPlnRmTypLst')]/li");

				for (val element : driver.findElements(by)) {
					add(new _00000() {
						{
							m_element = element;
						}
					});
				}
			}
		};
	}

	static class _Current extends WebClient {
	}

	static class _00000 extends _Current {
		String m_hotelNo;
		WebElement m_element;

		String getHotelNo() throws Exception {
			if (m_hotelNo == null) {
				val by = By.name("f_no");
				val element = m_element.findElement(by);
				m_hotelNo = element.getAttribute(Attributes.VALUE);
			}

			return m_hotelNo;
		}

		@Override
		public WebClient execute() throws Exception {
			System.out.println(getHotelNo());
			return super.execute();
		}
	}
}
