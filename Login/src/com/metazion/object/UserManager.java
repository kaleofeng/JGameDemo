package com.metazion.object;

import java.util.HashMap;

public class UserManager {

	private HashMap<Integer, User> userMap = new HashMap<Integer, User>();
	private HashMap<String, User> nameUserMap = new HashMap<String, User>();

	public UserManager() {

	}

	public void putUserMapping(User user) {
		int userId = user.getUserData().id;
		putUser(userId, user);

		String username = user.getUserData().username;
		putUser(username, user);
	}

	public void removeUserMapping(User user) {
		int userId = user.getUserData().id;
		removeUser(userId);

		String username = user.getUserData().username;
		removeUser(username);
	}

	public int getUserSize() {
		return userMap.size();
	}

	public User getUser(int userId) {
		return userMap.get(userId);
	}

	private void putUser(int userId, User user) {
		userMap.put(userId, user);
	}

	private void removeUser(int userId) {
		userMap.remove(userId);
	}

	public User getUser(String username) {
		return nameUserMap.get(username);
	}

	private void putUser(String username, User user) {
		nameUserMap.put(username, user);
	}

	private void removeUser(String username) {
		nameUserMap.remove(username);
	}
}