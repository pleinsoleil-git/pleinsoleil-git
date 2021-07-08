package common.jdbc;

import java.sql.Connection;
import java.util.Collection;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import common.app.App;
import lombok.val;

public class JDBCUtils {
	public static Connection getConnection() throws Exception {
		return App.getInstance().getConnection().getConnection();
	}

	public static JDBCStatement createStatement(final Connection conn) throws Exception {
		return new JDBCStatement(conn);
	}

	public static JDBCStatement createStatement(final JDBCConnection conn) throws Exception {
		return createStatement(conn.getConnection());
	}

	public static JDBCStatement createStatement() throws Exception {
		return createStatement(getConnection());
	}

	public static JDBCStatement createStatement(final Connection conn, String sql) throws Exception {
		val stmt = new JDBCStatement(conn);
		stmt.parse(sql);
		return stmt;
	}

	public static JDBCStatement createStatement(final JDBCConnection conn, String sql) throws Exception {
		return createStatement(conn.getConnection(), sql);
	}

	public static JDBCStatement createStatement(final String sql) throws Exception {
		return createStatement(getConnection(), sql);
	}

	public static <T> T query(final Connection conn, final String sql, final ResultSetHandler<T> rs) throws Exception {
		return (new QueryRunner()).query(conn, sql, rs);
	}

	public static <T> T query(final Connection conn, final String sql, final ResultSetHandler<T> rs, final Object... params) throws Exception {
		return (new QueryRunner()).query(conn, sql, rs, params);
	}

	public static <T> T query(final Connection conn, final String sql, final ResultSetHandler<T> rs, final List<Object> params) throws Exception {
		return (new QueryRunner()).query(conn, sql, rs, params.toArray());
	}

	public static <T> T query(final Connection conn, final String sql, final ResultSetHandler<T> rs, final Collection<Object> params)
			throws Exception {
		return (new QueryRunner()).query(conn, sql, rs, params.toArray());
	}

	public static <T> List<T> query(final Connection conn, final String sql, final BeanListHandler<T> rs) throws Exception {
		return (new QueryRunner()).query(conn, sql, rs);
	}

	public static <T> List<T> query(final Connection conn, final String sql, final BeanListHandler<T> rs, final Object... params) throws Exception {
		return (new QueryRunner()).query(conn, sql, rs, params);
	}

	public static <T> List<T> query(final Connection conn, final String sql, final BeanListHandler<T> rs, final List<Object> params)
			throws Exception {
		return (new QueryRunner()).query(conn, sql, rs, params.toArray());
	}

	public static <T> List<T> query(final Connection conn, final String sql, final BeanListHandler<T> rs, final Collection<Object> params)
			throws Exception {
		return (new QueryRunner()).query(conn, sql, rs, params.toArray());
	}

	public static <T> T query(final JDBCConnection conn, final String sql, final ResultSetHandler<T> rs) throws Exception {
		return query(conn.getConnection(), sql, rs);
	}

	public static <T> T query(final JDBCConnection conn, final String sql, final ResultSetHandler<T> rs, final Object... params) throws Exception {
		return query(conn.getConnection(), sql, rs, params);
	}

	public static <T> T query(final JDBCConnection conn, final String sql, final ResultSetHandler<T> rs, final List<Object> params) throws Exception {
		return query(conn.getConnection(), sql, rs, params);
	}

	public static <T> T query(final JDBCConnection conn, final String sql, final ResultSetHandler<T> rs, final Collection<Object> params)
			throws Exception {
		return query(conn.getConnection(), sql, rs, params);
	}

	public static <T> List<T> query(final JDBCConnection conn, final String sql, final BeanListHandler<T> rs) throws Exception {
		return query(conn.getConnection(), sql, rs);
	}

	public static <T> List<T> query(final JDBCConnection conn, final String sql, final BeanListHandler<T> rs, final Object... params)
			throws Exception {
		return query(conn.getConnection(), sql, rs, params);
	}

	public static <T> List<T> query(final JDBCConnection conn, final String sql, final BeanListHandler<T> rs, final List<Object> params)
			throws Exception {
		return query(conn.getConnection(), sql, rs, params);
	}

	public static <T> List<T> query(final JDBCConnection conn, final String sql, final BeanListHandler<T> rs, final Collection<Object> params)
			throws Exception {
		return query(conn.getConnection(), sql, rs, params);
	}

	public static <T> T query(final String sql, final ResultSetHandler<T> rs) throws Exception {
		return query(getConnection(), sql, rs);
	}

	public static <T> T query(final String sql, final ResultSetHandler<T> rs, final Object... params) throws Exception {
		return query(getConnection(), sql, rs, params);
	}

	public static <T> T query(final String sql, final ResultSetHandler<T> rs, final List<Object> params) throws Exception {
		return query(getConnection(), sql, rs, params);
	}

	public static <T> T query(final String sql, final ResultSetHandler<T> rs, final Collection<Object> params) throws Exception {
		return query(getConnection(), sql, rs, params);
	}

	public static <T> List<T> query(final String sql, final BeanListHandler<T> rs) throws Exception {
		return query(getConnection(), sql, rs);
	}

	public static <T> List<T> query(final String sql, final BeanListHandler<T> rs, final Object... params) throws Exception {
		return query(getConnection(), sql, rs, params);
	}

	public static <T> List<T> query(final String sql, final BeanListHandler<T> rs, final List<Object> params) throws Exception {
		return query(getConnection(), sql, rs, params);
	}

	public static <T> List<T> query(final String sql, final BeanListHandler<T> rs, final Collection<Object> params) throws Exception {
		return query(getConnection(), sql, rs, params);
	}

	public static int execute(final Connection conn, final String sql) throws Exception {
		return (new QueryRunner()).update(conn, sql);
	}

	public static int execute(final Connection conn, final String sql, final Object... params) throws Exception {
		return (new QueryRunner()).update(conn, sql, params);
	}

	public static int execute(final Connection conn, final String sql, final List<Object> params) throws Exception {
		return (new QueryRunner()).update(conn, sql, params.toArray());
	}

	public static int execute(final Connection conn, final String sql, final Collection<Object> params) throws Exception {
		return (new QueryRunner()).update(conn, sql, params.toArray());
	}

	public static int execute(final JDBCConnection conn, final String sql) throws Exception {
		return execute(conn.getConnection(), sql);
	}

	public static int execute(final JDBCConnection conn, String sql, Object... params) throws Exception {
		return execute(conn.getConnection(), sql, params);
	}

	public static int execute(final JDBCConnection conn, String sql, List<Object> params) throws Exception {
		return execute(conn.getConnection(), sql, params.toArray());
	}

	public static int execute(final JDBCConnection conn, final String sql, final Collection<Object> params) throws Exception {
		return execute(conn.getConnection(), sql, params.toArray());
	}

	public static int execute(final String sql) throws Exception {
		return execute(getConnection(), sql);
	}

	public static int execute(final String sql, final Object... params) throws Exception {
		return execute(getConnection(), sql, params);
	}

	public static int execute(final String sql, final List<Object> params) throws Exception {
		return execute(getConnection(), sql, params.toArray());
	}

	public static int execute(final String sql, final Collection<Object> params) throws Exception {
		return execute(getConnection(), sql, params.toArray());
	}

	public static void commit(final Connection conn) throws Exception {
		conn.commit();
	}

	public static void commit(final JDBCConnection conn) throws Exception {
		commit(conn.getConnection());
	}

	public static void commit() throws Exception {
		commit(getConnection());
	}

	public static void rollback(final Connection conn) throws Exception {
		conn.rollback();
	}

	public static void rollback(final JDBCConnection conn) throws Exception {
		rollback(conn.getConnection());
	}

	public static void rollback() throws Exception {
		rollback(getConnection());
	}

	static public void truncateTable(final Connection conn, final String tableName, final boolean cascade) throws Exception {
		String sql = String.format("TRUNCATE TABLE %s\n", tableName);
		if (cascade == true) {
			sql = String.format("%s CASCADE\n", sql);
		}

		execute(conn, sql);
	}

	public static void truncateTable(final Connection conn, final String tableName) throws Exception {
		truncateTable(conn, tableName, false);
	}

	public static void truncateTable(final JDBCConnection conn, final String tableName, final boolean cascade) throws Exception {
		truncateTable(conn.getConnection(), tableName, cascade);
	}

	public static void truncateTable(final JDBCConnection conn, final String tableName) throws Exception {
		truncateTable(conn.getConnection(), tableName);
	}

	public static void truncateTable(final String tableName, final boolean cascade) throws Exception {
		truncateTable(getConnection(), tableName, cascade);
	}

	public static void truncateTable(final String tableName) throws Exception {
		truncateTable(getConnection(), tableName);
	}
}
