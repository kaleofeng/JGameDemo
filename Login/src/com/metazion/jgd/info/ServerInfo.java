package com.metazion.jgd.info;

import com.metazion.jgd.def.ServerBean;
import com.metazion.jgd.util.JgdLogger;

public class ServerInfo {

	public ServerBean serverBean = new ServerBean();
	public String host = "";
	public int port = 0;
	public int crowdThreshold = 0;
	public int fullThreshold = 0;
	public int playerNumber = 0;

	public void onIncreased() {
		final int newPlayerNumber = Math.max(playerNumber + 1, 0);
		onChanged(host, port, crowdThreshold, fullThreshold, newPlayerNumber);
	}

	public void onDecreased() {
		final int newPlayerNumber = Math.max(playerNumber - 1, 0);
		onChanged(host, port, crowdThreshold, fullThreshold, newPlayerNumber);
	}

	public void onChanged(String host, int port, int crowdThreshold, int fullThreshold, int playerNumber) {
		this.host = host;
		this.port = port;
		this.crowdThreshold = crowdThreshold;
		this.fullThreshold = fullThreshold;
		this.playerNumber = playerNumber;

		if (playerNumber >= fullThreshold) {
			serverBean.status = ServerBean.STATUS_FULL;
		} else if (playerNumber >= crowdThreshold) {
			serverBean.status = ServerBean.STATUS_CROWD;
		} else {
			serverBean.status = ServerBean.STATUS_NORMAL;
		}

		JgdLogger.getLogger().info("Server info id[{}] name[{}] status[{}] host[{}] port[{}] crowd threshold[{}] full threshold[{}] player number[{}]", serverBean.id, serverBean.name, serverBean.status, host, port, crowdThreshold, fullThreshold, playerNumber);
	}
}