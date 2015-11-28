package com.metazion.jgd.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.metazion.jgd.model.UserData;
import com.metazion.jgd.util.DbUtil;
import com.metazion.jm.db.Record;
import com.metazion.jm.db.Table;

public class DbUser {

	private static final Table<UserData> udTable = new Table<UserData>();

	private static final String USERDATA_GETCOUNTBYNAME = "SELECT COUNT(`username`) FROM `user` WHERE `username`=?";
	private static final String USERDATA_GETIDBYNAME = "SELECT `user_id` FROM `user` WHERE `username`=?";
	private static final String USERDATA_GETBYNAME = "SELECT * FROM `user` WHERE `username`=?";

	static {
		try {
			udTable.load("data/db/user.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 获取用户数据
	public static UserData getUserData(int userId) {
		UserData result = null;

		Record<UserData> record = udTable.createRecord();

		UserData userData = new UserData();
		userData.userId = userId;
		record.setObject(userData);

		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String sql = udTable.getSelectSql();
			pst = DbUtil.openConnection().prepareStatement(sql);
			record.setSelectParams(pst);

			rs = pst.executeQuery();
			if (rs.next()) {
				record.setPrimaryResult(rs);
				record.setNormalResult(rs);
				result = userData;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbUtil.closeConnection(rs, pst);
		}

		return result;
	}

	// 添加用户数据
	public static boolean insertUserData(UserData userData) {
		boolean result = false;

		Record<UserData> record = udTable.createRecord();

		record.setObject(userData);

		PreparedStatement pst = null;
		try {
			String sql = udTable.getInsertSql();
			pst = DbUtil.openConnection().prepareStatement(sql);
			record.setInsertParams(pst);

			result = pst.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbUtil.closeConnection(null, pst);
		}

		return result;
	}

	// 更新用户数据
	public static boolean updateUserData(UserData userData) {
		boolean result = false;

		Record<UserData> record = udTable.createRecord();

		record.setObject(userData);

		PreparedStatement pst = null;
		try {
			String sql = udTable.getUpdateSql();
			pst = DbUtil.openConnection().prepareStatement(sql);
			record.setUpdateParams(pst);

			result = pst.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbUtil.closeConnection(null, pst);
		}

		return result;
	}

	// 删除用户数据
	public static boolean deleteUserData(int userId) {
		boolean result = false;

		Record<UserData> record = udTable.createRecord();

		UserData userData = new UserData();
		userData.userId = userId;
		record.setObject(userData);

		PreparedStatement pst = null;
		try {
			String sql = udTable.getDeleteSql();
			pst = DbUtil.openConnection().prepareStatement(sql);
			record.setDeleteParams(pst);

			result = pst.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbUtil.closeConnection(null, pst);
		}

		return result;
	}

	// 检查用户名是否已存在
	public static boolean isUsernameExists(String username) {
		int count = 0;

		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = DbUtil.openConnection().prepareStatement(USERDATA_GETCOUNTBYNAME);
			pst.setString(1, username);
			rs = pst.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbUtil.closeConnection(rs, pst);
		}

		return count > 0;
	}

	// 获取用户ID
	public static int getUserIdByName(String username) {
		int userId = 0;

		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = DbUtil.openConnection().prepareStatement(USERDATA_GETIDBYNAME);
			pst.setString(1, username);
			rs = pst.executeQuery();
			if (rs.next()) {
				userId = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbUtil.closeConnection(rs, pst);
		}

		return userId;
	}

	// 获取用户数据
	public static UserData getUserDataByName(String username) {
		UserData result = null;

		Record<UserData> record = udTable.createRecord();

		UserData userData = new UserData();
		record.setObject(userData);

		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = DbUtil.openConnection().prepareStatement(USERDATA_GETBYNAME);
			pst.setString(1, username);
			rs = pst.executeQuery();
			if (rs.next()) {
				record.setPrimaryResult(rs);
				record.setNormalResult(rs);
				result = userData;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbUtil.closeConnection(rs, pst);
		}

		return result;
	}
}