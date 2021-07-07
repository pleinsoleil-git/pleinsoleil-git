package common.app;

import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.config.entities.Parameterizable;

public class Form extends ActionSupport implements Parameterizable {
	final App m_app;

	public Form(final App app) {
		m_app = app;
	}

	@Override
	public void addParam(String name, String value) {
	}

	@Override
	public void setParams(Map<String, String> params) {
	}

	@Override
	public Map<String, String> getParams() {
		return null;
	}
}
