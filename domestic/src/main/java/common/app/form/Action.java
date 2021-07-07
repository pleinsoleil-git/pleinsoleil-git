package common.app.form;

import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.config.entities.Parameterizable;
import com.opensymphony.xwork2.inject.Container;

import common.app.App;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Action extends ActionSupport implements Parameterizable {
	final App m_app;
	Map<String, String> m_params;

	public Action(final App app) {
		m_app = app;
	}

	@Override
	public void addParam(String name, String value) {
		getParams().put(name, value);
	}

	@Override
	public void setParams(Map<String, String> params) {
		getParams().putAll(params);
	}

	@Override
	public Map<String, String> getParams() {
		return (m_params == null ? m_params = new HashMap<>() : m_params);
	}

	@Override
	public void setContainer(final Container container) {
		m_app.initInstance();
		super.setContainer(container);
	}

	@Override
	public void validate() {
		try {
		} catch (Exception e) {
			addActionError(e.getMessage());
			printError(e);
		} finally {
			if (hasErrors() == true) {
				m_app.exitInstance();
			}
		}
	}

	@Override
	public String execute() {
		try {
		} catch (Exception e) {
			addActionError(e.getMessage());
			printError(e);
		} finally {
			m_app.exitInstance();
		}

		return SUCCESS;
	}

	void printError(final Exception e) {
		if (e instanceof BatchUpdateException) {
			printError((BatchUpdateException) e);
		} else {
			log.error("", e);
		}
	}

	void printError(final BatchUpdateException e) {
		SQLException ex = e.getNextException();

		while (ex != null) {
			log.error("", e);
			ex = ex.getNextException();
		}
	}
}
