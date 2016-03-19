package com.metazion.jgd.task;

import java.util.HashMap;

import com.metazion.jgd.AppLogin;
import com.metazion.jgd.dao.DaoService;
import com.metazion.jgd.dao.DbUser;
import com.metazion.jgd.info.ServerInfo;
import com.metazion.jgd.info.ServerInfoManager;
import com.metazion.jgd.model.UserData;
import com.metazion.jgd.protocal.cl.UserLoginCL;
import com.metazion.jgd.protocal.cl.UserLoginLC;
import com.metazion.jgd.util.JgdLogger;
import com.metazion.jm.net.TransmitSession;
import com.metazion.jm.task.Task;
import com.metazion.object.User;

public class TaskLoadUser extends Task {

	private TransmitSession sessionIn = null;
	private UserLoginCL reqIn = null;

	private UserLoginLC rspOut = new UserLoginLC();

	private volatile UserData userDataOut = null;

	public TaskLoadUser(TransmitSession sessionIn, UserLoginCL req) {
		this.sessionIn = sessionIn;
		this.reqIn = req;
	}

	@Override
	public void execute() {
		setResult(UserLoginLC.SUCCESS);
		incDesire();

		if (userDataOut != null) {
			decDesire();
			return;
		}

		DaoService.execute(() -> {
			do {
				final String username = reqIn.username;

				UserData userData = DbUser.getUserDataByName(username);

				// 用户不存在
				if (userData == null) {
					setResult(UserLoginLC.ERROR_NOUSER);
					JgdLogger.getLogger().error("Task load user execute: no data in db");
					break;
				}

				userDataOut = userData;
			} while (false);
			decDesire();
		});
	}

	@Override
	public void onFinish() {
		if (result != UserLoginLC.SUCCESS) {
			rollback();
			failResponse();
			return;
		}

		succeedResponse();
	}

	@Override
	public void onTimeout() {
		setResult(UserLoginLC.TIMEOUT);
		rollback();
		failResponse();
	}

	public void setUserData(UserData userData) {
		this.userDataOut = userData;
	}

	private void rollback() {
		// Noting to do
	}

	private void succeedResponse() {
		JgdLogger.getLogger().info("Task load user successful: seq[{}] result[{}] req username[{}] user id[{}]", seq, result, reqIn.username, userDataOut.id);

		// 检查密码
		if (!reqIn.password.equals(userDataOut.password)) {
			JgdLogger.getLogger().error("Task load user post failed: seq[{}] user[{}:{}] password wrong", seq, userDataOut.id, userDataOut.username);
			rspOut.result = UserLoginLC.ERROR_WRONGPWD;
			sessionIn.send(rspOut);
			return;
		}

		final int userId = userDataOut.id;

		// TODO: Generate Token
		final String token = "THIS IS TOKEN";

		User user = AppLogin.getLogicService().getUserManager().getUser(userId);
		if (user == null) {
			user = new User();
		}

		user.setUserData(userDataOut);
		user.setToken(token);
		AppLogin.getLogicService().getUserManager().putUserMapping(user);

		// 记录上线时间
		DaoService.execute(() -> {
			userDataOut.lastLoginTime = System.currentTimeMillis();
			DbUser.updateUserData(userDataOut);
		});

		JgdLogger.getLogger().info("Task load user post successful: seq[{}] user[{}:{}] token[{}]", seq, userDataOut.id, userDataOut.username, token);

		rspOut.result = (byte) result;
		rspOut.userId = userId;
		rspOut.token = token;

		HashMap<Integer, ServerInfo> servers = ServerInfoManager.getInstance().getAllServerInfo();
		for (ServerInfo serverInfo : servers.values()) {
			rspOut.serverList.add(serverInfo.serverBean);
		}

		sessionIn.send(rspOut);
	}

	private void failResponse() {
		JgdLogger.getLogger().info("Task load user failed: seq[{}] result[{}] req username[{}]", seq, result, reqIn.username);

		rspOut.result = (byte) result;
		sessionIn.send(rspOut);
	}
}