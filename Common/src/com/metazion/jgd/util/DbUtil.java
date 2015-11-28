package com.metazion.jgd.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSourceFactory;

import com.metazion.jm.util.FileUtil;

public class DbUtil {

	private static ThreadLocal<Connection> connLocal = new ThreadLocal<Connection>();
	private static Properties properties = new Properties();
	private static DataSource dataSource = null;

	public static boolean init() {
		String path = FileUtil.getAbsolutePath("data/config/db.properties");
		try {
			InputStream is = new FileInputStream(path);
			properties.load(is);
			is.close();

			connect();
		} catch (Exception e) {
			JgdLogger.getLogger().error("DbUtil init failed: file[{}] exception[{}]", path, e.toString());
			return false;
		}

		return true;
	}

	public static Connection openConnection() {
		Connection conn = null;
		try {
			conn = connLocal.get();
			if (conn == null || conn.isClosed()) {
				conn = dataSource.getConnection();
				connLocal.set(conn);
			}
		} catch (Exception e) {
			try {
				connect();
				conn = connLocal.get();
				if (conn == null || conn.isClosed()) {
					conn = dataSource.getConnection();
					connLocal.set(conn);
				}
			} catch (Exception e1) {
				JgdLogger.getLogger().error("DbUtil open connection failed: exception[{}]", e.toString());
			}
		}

		return conn;
	}

	public static void closeConnection(ResultSet rs, PreparedStatement pst) {
		Connection conn = connLocal.get();
		try {
			if (rs != null) {
				rs.close();
			}
			if (pst != null) {
				pst.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
				connLocal.set(null);
			}
		} catch (SQLException e) {
			JgdLogger.getLogger().error("DbUtil close connection failed: exception[{}]", e.toString());
		}
	}

	private static void connect() throws Exception {
		dataSource = BasicDataSourceFactory.createDataSource(properties);
	}
}
