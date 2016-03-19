package com.metazion.jgd.action;

import com.metazion.jgd.AppLogin;
import com.metazion.jgd.def.ServerBean;
import com.metazion.jgd.info.ServerInfo;
import com.metazion.jgd.protocal.Message;
import com.metazion.jgd.protocal.cl.SelectServerCL;
import com.metazion.jgd.protocal.cl.SelectServerLC;
import com.metazion.jgd.protocal.sl.UserCandidateLS;
import com.metazion.jgd.util.JgdLogger;
import com.metazion.object.Server;
import com.metazion.object.User;

public class RASelectServerCL extends RequestAction {

	private SelectServerCL req = new SelectServerCL();

	@Override
	public void setRequest(Message msg) {
		req = (SelectServerCL) msg;
	}

	@Override
	public void execute() {
		final int userId = req.userId;
		final String token = req.token;
		final int serverId = req.serverId;

		JgdLogger.getLogger().info("Select server cl: userId[{}] token[{}] serverId[{}]", userId, token, serverId);

		SelectServerLC rsp = new SelectServerLC();

		User user = AppLogin.getLogicService().getUserManager().getUser(userId);
		if (user == null) {
			rsp.result = SelectServerLC.ERROR_ILLEGAL;
			session.send(rsp);
			JgdLogger.getLogger().debug("Select server cl failed: user is null");
			return;
		}

		String userToken = user.getToken();
		if (!token.equals(userToken)) {
			rsp.result = SelectServerLC.ERROR_ILLEGAL;
			session.send(rsp);
			JgdLogger.getLogger().debug("Select server cl failed: token is invalid");
			return;
		}

		Server server = AppLogin.getLogicService().getServerManager().getServer(serverId);
		if (server == null) {
			rsp.result = SelectServerLC.ERROR_CLOSED;
			session.send(rsp);
			JgdLogger.getLogger().debug("Select server cl failed: server[{}] hasn't connected in", serverId);
			return;
		}

		ServerInfo serverInfo = server.getServerInfo();

		if (serverInfo.serverBean.status == ServerBean.STATUS_CLOSED) {
			rsp.result = SelectServerLC.ERROR_CLOSED;
			session.send(rsp);
			JgdLogger.getLogger().debug("Select server cl failed: server[{}] is closed", serverId);
			return;
		}

		if (serverInfo.serverBean.status == ServerBean.STATUS_FULL) {
			rsp.result = SelectServerLC.ERROR_FULL;
			session.send(rsp);
			JgdLogger.getLogger().debug("Select server cl failed: server[{}] is full", serverId);
			return;
		}

		user.setSession(session);
		user.setToken(userToken);

		JgdLogger.getLogger().debug("Select server cl next: server[{}] candidate user[{}]", serverId, userId);

		UserCandidateLS reqUCLS = new UserCandidateLS();
		reqUCLS.userId = userId;
		reqUCLS.token = token;
		server.send(reqUCLS);
	}
}