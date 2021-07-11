package a00100.app.job.a00100.ikyu.job.request.process.crawl.query;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

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
		@Override
		public void navigate() throws Exception {
			val URL = "https://www.ikyu.com/%s";
			val crawl = Crawl.getCurrent();
			val driver = getDriver();
			driver.get(String.format(URL, crawl.getHotelCode()));
		}

		@Override
		public WebClient submit() throws Exception {
			//return new _00100();
			return null;
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
			setRoom();
			setAdult();
			setChild();
			pushQuery();
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

		void setRoom() throws Exception {
			// --------------------------------------------------
			// 部屋
			// --------------------------------------------------
			val crawl = Crawl.getCurrent();
			if (crawl.getRoomNums() != null) {
				val driver = getDriver();
				val by = By.id("dyn_room_num");
				val element = new Select(driver.findElement(by));

				element.selectByValue(crawl.getRoomNums());
			}
		}

		void setAdult() throws Exception {
			// --------------------------------------------------
			// 大人人数
			// --------------------------------------------------
			val crawl = Crawl.getCurrent();
			if (crawl.getAdultNums() != null) {
				val driver = getDriver();
				val by = By.id("dyn_adult_num");
				val element = new Select(driver.findElement(by));

				element.selectByValue(crawl.getAdultNums());
			}
		}

		void setChild() throws Exception {
			// --------------------------------------------------
			// 子供人数
			// 高学年は大人扱い
			// --------------------------------------------------
			val driver = getDriver();

			try {
				val by = By.id("dyn_child_num_txt_id1");
				val element = driver.findElement(by);

				element.click();
				setLowerGrade();
			} finally {
				val by = By.id("panel-close-btn");
				val element = driver.findElement(by);

				element.click();
			}
		}

		void setLowerGrade() throws Exception {
			// --------------------------------------------------
			// 低学年
			// --------------------------------------------------
			val crawl = Crawl.getCurrent();
			if (crawl.getLowerGradeNums() != null) {
				val driver = getDriver();
				val by = By.id("panel_child_num_1_1");
				val element = new Select(driver.findElement(by));

				element.selectByValue(crawl.getLowerGradeNums());
			}
		}

		void pushQuery() throws Exception {
			// --------------------------------------------------
			// 【検索】押下
			// --------------------------------------------------
			val driver = getDriver();
			val by = By.id("research");
			val element = driver.findElement(by);

			element.click();
		}
	}
}
