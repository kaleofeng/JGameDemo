package com.metazion.jgd.action;

import com.metazion.jgd.AppGame;
import com.metazion.jgd.info.ServerConfig;
import com.metazion.jgd.protocal.Message;
import com.metazion.jgd.protocal.sl.UserCandidateLS;
import com.metazion.jgd.protocal.sl.UserCandidateSL;
import com.metazion.jgd.util.JgdLogger;
import com.metazion.object.User;

public class RAUserCandidateLS extends RequestAction {

	private UserCandidateLS req = new UserCandidateLS();

	@Override
	public void setRequest(Message msg) {
		req = (UserCandidateLS) msg;
	}

	@Override
	public void execute() {
		final int userId = req.userId;
		final String token = req.token;

		JgdLogger.getLogger().info("User candidate ls: userId[{}] token[{}]", userId, token);

		User user = AppGame.getLogicService().getUserManager().getUser(userId);
		if (user == null) {
			user = new User();
			user.setId(userId);
			AppGame.getLogicService().getUserManager().addUser(userId, user);
		}

		user.setToken(token);

		JgdLogger.getLogger().info("User candidate ls successful: userId[{}] token[{}]", userId, token);

		UserCandidateSL reqUCSL = new UserCandidateSL();
		reqUCSL.serverId = ServerConfig.getInstance().serverId;
		reqUCSL.userId = userId;
		session.send(reqUCSL);
	}
}