package com.metazion.object;

import com.metazion.jgd.model.UserData;
import com.metazion.jm.net.TransmitSession;

public class User {

	private TransmitSession session = null; // 会话

	private UserData userData = null;
	private String token = "";

	public User() {

	}

	public TransmitSession getSession() {
		return session;
	}

	public void setSession(TransmitSession session) {
		this.session = session;
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

	public void send(Object data) {
		if (session != null) {
			session.send(data);
		}
	}
}