package com.metazion.object;

import java.util.HashMap;

public class UserManager {

	private HashMap<Integer, User> userMap = new HashMap<Integer, User>();

	public UserManager() {

	}

	public int getUserSize() {
		return userMap.size();
	}

	public User getUser(int userId) {
		return userMap.get(userId);
	}

	public void addUser(int userId, User user) {
		userMap.put(userId, user);
	}

	public void removeUser(int userId) {
		userMap.remove(userId);
	}
}