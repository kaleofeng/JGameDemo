package com.metazion.jgd.action;

import com.metazion.jgd.AppLogin;
import com.metazion.jgd.info.ServerInfo;
import com.metazion.jgd.protocal.Message;
import com.metazion.jgd.protocal.cl.SelectServerLC;
import com.metazion.jgd.protocal.sl.UserCandidateSL;
import com.metazion.jgd.util.JgdLogger;
import com.metazion.object.Server;
import com.metazion.object.User;

public class RAUserCandidateLS extends RequestAction {

	private UserCandidateSL req = new UserCandidateSL();

	@Override
	public void setRequest(Message msg) {
		req = (UserCandidateSL) msg;
	}

	@Override
	public void execute() {
		final int serverId = req.serverId;
		final int userId = req.userId;

		JgdLogger.getLogger().info("User candidate ls: serverId[{}] userId[{}]", serverId, userId);

		Server server = AppLogin.getLogicService().getServerManager().getServer(serverId);
		if (server == null) {
			JgdLogger.getLogger().error("User candidate ls error: no server data[{}]", serverId);
			return;
		}

		ServerInfo serverInfo = server.getServerInfo();
		if (serverInfo == null) {
			JgdLogger.getLogger().error("User candidate ls error: no server info[{}]", serverId);
			return;
		}

		SelectServerLC rsp = new SelectServerLC();

		User user = AppLogin.getLogicService().getUserManager().getUser(userId);
		if (user == null) {
			JgdLogger.getLogger().warn("User candidate ls failed: user is null");
			return;
		}

		AppLogin.getLogicService().getUserManager().removeUserMapping(user);

		JgdLogger.getLogger().info("User candidate ls successful: server id[{}] user id[{}] status[{}] host[{}] port[{}]", serverId, userId, serverInfo.serverBean.status, serverInfo.host, serverInfo.port);

		rsp.result = SelectServerLC.SUCCESS;
		rsp.userId = userId;
		rsp.token = user.getToken();
		rsp.host = serverInfo.host;
		rsp.port = serverInfo.port;
		user.send(rsp);
	}
}