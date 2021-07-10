package a00100.app.job.a00100.jalan.job.request.process.crawl.query;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import a00100.app.job.a00100.jalan.job.request.process.crawl.Crawl;
import a00100.app.job.a00100.jalan.job.request.process.webBrowser.WebClient;
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

			return null;
		} finally {
			m_instances.remove();
		}
	}

	static class _00000 extends WebClient {
		@Override
		public void navigate() throws Exception {
			val URL = "https://www.jalan.net/yad%s/plan/?yadNo=%s";
			val crawl = Crawl.getCurrent();
			val driver = getDriver();
			driver.get(String.format(URL, crawl.getHotelCode(), crawl.getHotelCode()));
		}

		@Override
		public WebClient submit() throws Exception {
			return new _00100();
		}
	}

	static class _00100 extends WebClient {
		@Override
		public WebClient submit() throws Exception {
			pushDate();
			setYearFrom();
			setMonthFrom();
			setDayFrom();
			setStay();
			setAdult();
			//setChild();
			//pushQuery();
			return null;
		}

		void pushDate() throws Exception {
			// --------------------------------------------------
			// 【日付未定】チェックを外す
			// --------------------------------------------------
			val driver = getDriver();
			val by = By.id("datecheck");
			val element = driver.findElement(by);

			if (element.isSelected() == true) {
				element.click();
			}
		}

		void setYearFrom() throws Exception {
			// --------------------------------------------------
			// 宿泊日　年
			// --------------------------------------------------
			val crawl = Crawl.getCurrent();
			val driver = getDriver();
			val by = By.id("dyn_y_txt");
			val element = driver.findElement(by);

			element.clear();
			element.sendKeys(crawl.getYearFrom());
		}

		void setMonthFrom() throws Exception {
			// --------------------------------------------------
			// 宿泊日　月
			// --------------------------------------------------
			val crawl = Crawl.getCurrent();
			val driver = getDriver();
			val by = By.id("dyn_m_txt");
			val element = driver.findElement(by);

			element.clear();
			element.sendKeys(crawl.getMonthFrom());
		}

		void setDayFrom() throws Exception {
			// --------------------------------------------------
			// 宿泊日　日
			// --------------------------------------------------
			val crawl = Crawl.getCurrent();
			val driver = getDriver();
			val by = By.id("dyn_d_txt");
			val element = driver.findElement(by);

			element.clear();
			element.sendKeys(crawl.getDayFrom());
		}

		void setStay() throws Exception {
			// --------------------------------------------------
			// 泊
			// --------------------------------------------------
			val crawl = Crawl.getCurrent();
			val driver = getDriver();
			val by = By.id("dyn_stay_txt");
			val element = new Select(driver.findElement(by));

			element.selectByValue(crawl.getStayNums());
		}

		void setAdult() throws Exception {
			// --------------------------------------------------
			// 大人人数
			// --------------------------------------------------
			val crawl = Crawl.getCurrent();
			if (crawl.getAdultNums() != null) {
				val driver = getDriver();
				val by = By.id("dhAdult1");
				val element = new Select(driver.findElement(by));

				element.selectByValue(String.format("%d", crawl.getAdultNums()));
			}
		}

		void setChild() throws Exception {
			// --------------------------------------------------
			// 子供人数
			// --------------------------------------------------
			val driver = getDriver();
			val by = By.id("chldNum1");
			val element = driver.findElement(by);

			element.click();
			setUpperGrade();
			setLowerGrade();
			element.click();
		}

		void setUpperGrade() throws Exception {
			// --------------------------------------------------
			// 高学年
			// --------------------------------------------------
			val crawl = Crawl.getCurrent();
			if (crawl.getUpperGradeNums() != null) {
				val driver = getDriver();
				val by = By.id("dh-s1");
				val element = new Select(driver.findElement(by));

				element.selectByValue(String.format("%d", crawl.getUpperGradeNums()));
			}
		}

		void setLowerGrade() throws Exception {
			// --------------------------------------------------
			// 低学年
			// --------------------------------------------------
			val crawl = Crawl.getCurrent();
			if (crawl.getLowerGradeNums() != null) {
				val driver = getDriver();
				val by = By.id("dh-s2");
				val element = new Select(driver.findElement(by));

				element.selectByValue(String.format("%d", crawl.getLowerGradeNums()));
			}
		}

		void pushQuery() throws Exception {
			// --------------------------------------------------
			// 【検索】押下
			// --------------------------------------------------
			val driver = getDriver();
			val by = By.id("dh-submit");
			val element = driver.findElement(by);

			element.click();
		}
	}
}
