package common.app;

import common.app.bean.Bean;
import common.app.model.Model;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Accessors(prefix = "m_", chain = true)
public class App {
	static final ThreadLocal<App> m_instances = new ThreadLocal<App>();

	@Getter
	final Bean m_bean;

	@Getter
	final Model m_model;

	public App(final Bean bean, final Model model) {
		m_bean = bean;
		m_model = model;
	}
}
