package a00100.app.job.a00100.crawl.job.rakuten;

public class Rakuten {
	static Rakuten m_instance;

	Rakuten() {
	}

	public static Rakuten getInstance() {
		return (m_instance == null ? m_instance = new Rakuten() : m_instance);
	}

	public void execute() throws Exception {
		try {
		} finally {
			m_instance = null;
		}
	}
}
