package com.metazion.jgd.action;

import com.metazion.jgd.AppLogin;
import com.metazion.jgd.info.ServerInfo;
import com.metazion.jgd.info.ServerInfoManager;
import com.metazion.jgd.protocal.Message;
import com.metazion.jgd.protocal.gl.ServerExitSL;
import com.metazion.jgd.util.JgdLogger;
import com.metazion.object.Server;

public class RAServerExitSL extends RequestAction {

	private ServerExitSL req = null;

	@Override
	public void setRequest(Message msg) {
		req = (ServerExitSL) msg;
	}

	@Override
	public void execute() {
		final int serverId = req.serverId;

		JgdLogger.getLogger().info("Server exit sl: serverId[{}]", serverId);

		ServerInfo serverInfo = ServerInfoManager.getInstance().getServerInfo(serverId);
		if (serverInfo == null) {
			JgdLogger.getLogger().error("Server exit sl error: no server info[{}]", serverId);
			return;
		}

		Server server = AppLogin.getLogicService().getServerManager().getServer(serverId);
		if (server == null) {
			JgdLogger.getLogger().warn("Server exit sl failed: no server found[{}]", serverId);
			return;
		}

		server.onExit();

		JgdLogger.getLogger().info("Server exit sl successful: server id[{}] status[{}]", serverId, serverInfo.serverBean.status);
	}
}