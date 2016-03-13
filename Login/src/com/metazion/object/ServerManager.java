package com.metazion.object;

import java.util.HashMap;

public class ServerManager {

	private HashMap<Integer, Server> serverMap = new HashMap<Integer, Server>();

	public Server getServer(int id) {
		return serverMap.get(id);
	}

	public void addServer(int id, Server server) {
		serverMap.put(id, server);
	}

	public void removeServer(int id) {
		serverMap.remove(id);
	}
}