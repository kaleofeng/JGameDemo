package com.metazion.object;

import com.metazion.jgd.model.UserData;

public class User {

	private UserData userData = null;
	private String token = "";

	public User() {

	}

	public UserData getUserData() {
		return userData;
	}

	public void setUserData(UserData userData) {
		this.userData = userData;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}