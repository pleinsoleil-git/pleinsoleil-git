package a00100;

import common.app.Bean;
import common.app.Model;

public class App extends common.app.App {
	public static final String PACKAGE_NAME = App.class.getPackage().getName();
	public static final String DEFAULT_DSN = String.format("jdbc/%s/default", PACKAGE_NAME);

	public App(final Bean bean, final Model model) {
		super(bean, model);
	}

	@Override
	public String getDataSourceName() {
		return DEFAULT_DSN;
	}
}
