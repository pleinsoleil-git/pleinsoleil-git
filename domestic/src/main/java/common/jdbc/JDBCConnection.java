package common.jdbc;

import java.sql.Connection;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import lombok.val;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Accessors(prefix = "m_", chain = false)
public class JDBCConnection implements AutoCloseable {
	String m_dsn;
	Connection m_connection;

	public JDBCConnection(final String dsn) {
		m_dsn = dsn;
	}

	Connection get() throws Exception {
		if (m_connection == null) {
			log.debug(String.format("Open connection[dsn=%s]", m_dsn));
			val icx = new InitialContext();
			try {
				val ds = (DataSource) icx.lookup("java:comp/env/" + m_dsn);
				m_connection = ds.getConnection();
				m_connection.setAutoCommit(false);
			} finally {
				icx.close();
			}
		}

		return m_connection;
	}

	public Connection getConnection() throws Exception {
		return get();
	}

	public void commit() throws Exception {
		get().commit();
	}

	public void rollback() throws Exception {
		get().rollback();
	}

	@Override
	public void close() throws Exception {
		if (m_connection != null) {
			try (val x = m_connection) {
				log.debug(String.format("Close connection[dsn=%s]", m_dsn));
			} finally {
				m_connection = null;
			}
		}
	}
}
