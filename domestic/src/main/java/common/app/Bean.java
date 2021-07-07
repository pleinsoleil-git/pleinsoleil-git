package common.app;

public class Bean {
	public static Bean getInstance() {
		return App.getInstance().getBean();
	}
}
