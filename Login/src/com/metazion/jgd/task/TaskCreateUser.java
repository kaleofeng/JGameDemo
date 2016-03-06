package com.metazion.jgd.task;

import com.metazion.jgd.dao.DaoService;
import com.metazion.jgd.dao.DbUser;
import com.metazion.jgd.model.UserData;
import com.metazion.jgd.protocal.cl.UserRegisterCL;
import com.metazion.jgd.protocal.cl.UserRegisterLC;
import com.metazion.jgd.util.JgdLogger;
import com.metazion.jm.net.TransmitSession;
import com.metazion.jm.task.Task;

public class TaskCreateUser extends Task {

	private TransmitSession session = null;
	private UserRegisterCL reqIn = null;

	private UserRegisterLC rspOut = new UserRegisterLC();

	private volatile UserData userDataOut = null;

	public TaskCreateUser(TransmitSession session, UserRegisterCL req) {
		this.session = session;
		this.reqIn = req;
	}

	@Override
	public void execute() {
		setResult(UserRegisterLC.SUCCESS);
		incDesire();

		DaoService.execute(() -> {
			do {
				final String username = reqIn.username.trim().toLowerCase();
				final String password = reqIn.password.trim();

				// 检查用户名是否已存在
				if (DbUser.isUsernameExists(username)) {
					setResult(UserRegisterLC.ERROR_DUPLICATE);
					JgdLogger.getLogger().warn("Task create user execute: already exist in db");
					break;
				}

				// 添加用户数据
				UserData userData = new UserData();
				userData.username = username;
				userData.password = password;
				userData.salt = 0;
				userData.registerTime = System.currentTimeMillis();
				userData.lastLoginTime = userData.registerTime;
				if (!DbUser.insertUserData(userData)) {
					setResult(UserRegisterLC.ERROR_DBINSERT);
					JgdLogger.getLogger().warn("Task create user execute: insert into db failed");
					break;
				}

				userData = DbUser.getUserDataByName(username);
				if (userData == null) {
					setResult(UserRegisterLC.ERROR_DBINSERT);
					JgdLogger.getLogger().warn("Task create user execute: no data in db");
					break;
				}

				userDataOut = userData;
			} while (false);
			decDesire();
		});
	}

	@Override
	public void onFinish() {
		JgdLogger.getLogger().debug("Task create user on finish: seq[{}] result[{}]", seq, result);

		if (result != UserRegisterLC.SUCCESS) {
			rollback();
			failResponse();
			return;
		}

		succeedResponse();
	}

	@Override
	public void onTimeout() {
		JgdLogger.getLogger().warn("Task create user on timeout: seq[{}] result[{}]", seq, result);

		setResult(UserRegisterLC.TIMEOUT);
		rollback();
		failResponse();
	}

	private void rollback() {
		DaoService.execute(() -> {
			if (userDataOut != null) {
				DbUser.deleteUserData(userDataOut.id);
			}
		});
	}

	private void succeedResponse() {
		JgdLogger.getLogger().info("Task create user successful: seq[{}] result[{}] req username[{}] user id[{}] register address[{}]", seq, result, reqIn.username, userDataOut.id, session.getChannel().remoteAddress().toString());

		rspOut.result = (byte) result;
		session.send(rspOut);
	}

	private void failResponse() {
		JgdLogger.getLogger().info("Task create user failed: seq[{}] result[{}] req username[{}] register address[{}]", seq, result, reqIn.username, session.getChannel().remoteAddress().toString());

		rspOut.result = (byte) result;
		session.send(rspOut);
	}
}