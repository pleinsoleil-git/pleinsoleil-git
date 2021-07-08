package common.jdbc;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import common.lang.StringUtils;
import lombok.val;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Accessors(prefix = "m_", chain = true)
public class JDBCStatement implements AutoCloseable {
	Connection m_connection;
	PreparedStatement m_statement;

	public JDBCStatement(final Connection connection) {
		m_connection = connection;
	}

	public JDBCStatement(final JDBCConnection connection) throws Exception {
		this(connection.getConnection());
	}

	public Connection getConnection() {
		return m_connection;
	}

	public JDBCStatement parse(final String sql, final int resultSetType, final int resultSetConcurrency) throws Exception {
		log.debug("Open statement");
		m_statement = m_connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
		return this;
	}

	public void parse(final String sql) throws Exception {
		parse(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
	}

	public void addBatch() throws Exception {
		m_statement.addBatch();
	}

	public void addBatch(final String sql) throws Exception {
		m_statement.addBatch(sql);
	}

	public void clearBatch() throws Exception {
		m_statement.clearBatch();
	}

	public void clearParameters() throws Exception {
		m_statement.clearParameters();
	}

	public int[] executeBatchAndClear() throws Exception {
		try {
			return executeBatch();
		} finally {
			clearBatch();
		}
	}

	public void setFetchSize(final int rowNums) throws Exception {
		m_statement.setFetchSize(rowNums);
	}

	public boolean execute() throws Exception {
		return m_statement.execute();
	}

	public int executeUpdate() throws Exception {
		return m_statement.executeUpdate();
	}

	public int[] executeBatch() throws Exception {
		return m_statement.executeBatch();
	}

	public int getUpdateCount() throws Exception {
		return m_statement.getUpdateCount();
	}

	@Override
	public void close() throws Exception {
		if (m_statement != null) {
			try (val x = m_statement) {
				log.debug("Close statement");
			} finally {
				m_statement = null;
			}
		}
	}

	public void setValues(final Collection<Object> params) throws Exception {
		int colNum = 1;
		for (val param : params) {
			setValue(colNum++, param);
		}
	}

	public void setValue(final int colNum, final Object value) throws Exception {
		if (value == null) {
			setNull(colNum, Types.VARCHAR);
		} else if (value instanceof String) {
			setString(colNum, (String) value);
		} else if (value instanceof Integer) {
			setInt(colNum, (Integer) value);
		} else if (value instanceof Long) {
			setLong(colNum, (Long) value);
		} else if (value instanceof Boolean) {
			setBoolean(colNum, (Boolean) value);
		} else if (value instanceof Double) {
			setDouble(colNum, (Double) value);
		} else if (value instanceof BigDecimal) {
			setString(colNum, ((BigDecimal) value).toString());
		} else if (value instanceof Time) {
			setTime(colNum, (Time) value);
		} else if (value instanceof Timestamp) {
			setTimestamp(colNum, (Timestamp) value);
		} else if (value instanceof Calendar) {
			setDate(colNum, (Calendar) value);
		} else if (value instanceof Date) {
			setDate(colNum, (Date) value);
		} else if (value instanceof File) {
			setFile(colNum, (File) value);
		} else {
			log.error(String.format("not support %s", value.getClass().getName()));
			throw new IllegalArgumentException();
		}
	}

	public void setNull(final int colNum, final int paramType) throws Exception {
		m_statement.setNull(colNum, paramType);
	}

	public void setBoolean(final int colNum, final Boolean value) throws Exception {
		if (value == null) {
			setNull(colNum, Types.BOOLEAN);
		} else {
			m_statement.setBoolean(colNum, value);
		}
	}

	public void setString(final int colNum, final String value) throws Exception {
		if (StringUtils.isEmpty(value) == true) {
			setNull(colNum, Types.VARCHAR);
		} else {
			m_statement.setString(colNum, value);
		}
	}

	public void setInt(final int colNum, final Integer value) throws Exception {
		if (value == null) {
			setNull(colNum, Types.INTEGER);
		} else {
			m_statement.setInt(colNum, value);
		}
	}

	public void setLong(final int colNum, final Long value) throws Exception {
		if (value == null) {
			setNull(colNum, Types.INTEGER);
		} else {
			m_statement.setLong(colNum, value);
		}
	}

	public void setDouble(final int colNum, final Double value) throws Exception {
		if (value == null) {
			setNull(colNum, Types.DOUBLE);
		} else {
			m_statement.setDouble(colNum, value);
		}
	}

	public void setDate(final int colNum, final Date value) throws Exception {
		if (value == null) {
			setNull(colNum, Types.DATE);
		} else {
			m_statement.setDate(colNum, new java.sql.Date(value.getTime()));
		}
	}

	public void setDate(final int colNum, final Calendar value) throws Exception {
		if (value == null) {
			setNull(colNum, Types.DATE);
		} else {
			setDate(colNum, value.getTime());
		}
	}

	public void setTime(final int colNum, final Time value) throws Exception {
		if (value == null) {
			setNull(colNum, Types.TIME);
		} else {
			m_statement.setTime(colNum, value);
		}
	}

	public void setTimestamp(final int colNum, final Timestamp value) throws Exception {
		if (value == null) {
			setNull(colNum, Types.TIMESTAMP);
		} else {
			m_statement.setTimestamp(colNum, value);
		}
	}

	public void setBlob(final int colNum, final InputStream value) throws Exception {
		if (value == null) {
			setNull(colNum, Types.BLOB);
		} else {
			m_statement.setBlob(colNum, value);
		}
	}

	public void setBinaryStream(final int colNum, final InputStream value) throws Exception {
		if (value == null) {
			setNull(colNum, Types.BLOB);
		} else {
			m_statement.setBinaryStream(colNum, value);
		}
	}

	public void setFile(final int colNum, final File value) throws Exception {
		if (value == null) {
			setNull(colNum, Types.BLOB);
		} else {
			try (val stream = new FileInputStream(value)) {
				setBinaryStream(colNum, stream);
			}
		}
	}
}
