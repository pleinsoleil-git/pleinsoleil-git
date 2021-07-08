package common.jdbc;

import java.sql.Connection;

import common.app.App;

public class JDBCUtils {
	public static Connection getConnection() throws Exception {
		return App.getInstance().getConnection().getConnection();
	}
}
