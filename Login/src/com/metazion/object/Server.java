package com.metazion.object;

import com.metazion.jgd.def.ServerBean;
import com.metazion.jgd.info.ServerInfo;
import com.metazion.jgd.net.SSServer;
import com.metazion.jgd.util.JgdLogger;

public class Server {

	private int id = 0;
	private ServerInfo serverInfo = null;
	private SSServer session = null;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ServerInfo getServerInfo() {
		return serverInfo;
	}

	public void setServerInfo(ServerInfo serverInfo) {
		this.serverInfo = serverInfo;
	}

	public SSServer getSession() {
		return session;
	}

	public void setSession(SSServer session) {
		this.session = session;
	}

	public void send(Object data) {
		if (session != null) {
			session.send(data);
		}
	}

	public void onJoin() {
		JgdLogger.getLogger().info("Server join: id[{}] name[{}] status[{}] host[{}] port[{}]", id, serverInfo.serverBean.name, serverInfo.serverBean.status, serverInfo.host, serverInfo.port);
	}

	public void onExit() {
		serverInfo.serverBean.status = ServerBean.STATUS_CLOSED;

		JgdLogger.getLogger().info("Server exit: id[{}] name[{}] status[{}] host[{}] port[{}]", id, serverInfo.serverBean.name, serverInfo.serverBean.status, serverInfo.host, serverInfo.port);
	}
}