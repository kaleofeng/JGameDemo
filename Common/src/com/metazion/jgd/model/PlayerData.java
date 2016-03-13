package com.metazion.jgd.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PlayerData implements Serializable {

	// 角色ID
	public int id = 0;

	// 账户ID
	public int account = 0;

	// 昵称
	public String nickname = "";

	// 上次登入时间
	public long lastLoginTime = 0;

	// 上次登出时间
	public long lastLogoutTime = 0;

	@Override
	public String toString() {
		return String.format("%d:%s", id, nickname);
	}
}