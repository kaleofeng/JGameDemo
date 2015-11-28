package com.metazion.jgd.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserData implements Serializable {

	// 用户ID
	public int userId = 0;

	// 用户名
	public String username = "";

	// 密码
	public String password = "";

	// 盐
	public int salt = 0;

	// 注册时间
	public long registerTime = 0;

	// 上次登录时间
	public long lastLoginTime = 0;

	@Override
	public String toString() {
		return String.format("%d:%s", userId, username);
	}
}