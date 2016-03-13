package com.metazion.jgd.action;

import com.metazion.jgd.AppLogin;
import com.metazion.jgd.info.ServerInfo;
import com.metazion.jgd.info.ServerInfoManager;
import com.metazion.jgd.net.SSServer;
import com.metazion.jgd.protocal.Message;
import com.metazion.jgd.protocal.gl.ServerJoinSL;
import com.metazion.jgd.util.JgdLogger;
import com.metazion.object.Server;

public class RAServerJoinSL extends RequestAction {

	private ServerJoinSL req = null;

	@Override
	public void setRequest(Message msg) {
		req = (ServerJoinSL) msg;
	}

	@Override
	public void execute() {
		final int serverId = req.serverId;
		final String host = req.host;
		final int port = req.port;
		final int crowdThreshold = req.crowdThreshold;
		final int fullThreshold = req.fullThreshold;
		final int playerNumber = req.playerNumber;

		JgdLogger.getLogger().info("Server join sl: serverId[{}] host[{}] port[{}] crowd threshold[{}] full threshold[{}] playerNumber[{}]", serverId, host, port, crowdThreshold, fullThreshold, playerNumber);

		ServerInfo serverInfo = ServerInfoManager.getInstance().getServerInfo(serverId);
		if (serverInfo == null) {
			JgdLogger.getLogger().error("Server join sl error: no server info[{}]", serverId);
			return;
		}

		Server server = AppLogin.getLogicService().getServerManager().getServer(serverId);
		if (server == null) {
			server = new Server();
			server.setId(serverId);
			AppLogin.getLogicService().getServerManager().addServer(serverId, server);
		}

		serverInfo.onChanged(host, port, crowdThreshold, fullThreshold, playerNumber);

		assert session instanceof SSServer;

		SSServer ssServer = (SSServer) session;
		ssServer.setServerId(server.getId());

		server.setServerInfo(serverInfo);
		server.setSession(ssServer);
		server.onJoin();

		JgdLogger.getLogger().info("Server join sl successful: server id[{}] status[{}] crowd threshold[{}] full threshold[{}] player number[{}]", serverId, serverInfo.serverBean.status, crowdThreshold, fullThreshold, playerNumber);
	}
}