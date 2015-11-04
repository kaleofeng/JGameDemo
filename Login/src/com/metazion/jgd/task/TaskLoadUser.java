package com.metazion.jgd.task;

import com.metazion.jgd.dao.DaoService;
import com.metazion.jgd.model.UserData;
import com.metazion.jgd.protocal.cl.UserLoginCL;
import com.metazion.jgd.protocal.cl.UserLoginLC;
import com.metazion.jgd.util.JgdLogger;
import com.metazion.jm.net.TransmitSession;
import com.metazion.jm.task.Task;

public class TaskLoadUser extends Task {

	private TransmitSession sessionIn = null;
	private UserLoginCL reqIn = null;

	private UserLoginLC rspOut = new UserLoginLC();

	private volatile UserData userDataOut = null;

	public TaskLoadUser(TransmitSession session, UserLoginCL req) {
		this.sessionIn = session;
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

				userDataOut = new UserData();
				userDataOut.userId = 1;
				userDataOut.username = username;
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
		JgdLogger.getLogger().info("Task load user successful: seq[{}] result[{}] req username[{}] user id[{}]", seq,
				result, reqIn.username, userDataOut.userId);

		rspOut.result = (byte) result;
		rspOut.userId = userDataOut.userId;
		rspOut.token = "THIS IS TOKEN";

		sessionIn.send(rspOut);
	}

	private void failResponse() {
		JgdLogger.getLogger().info("Task load user failed: seq[{}] result[{}] req username[{}]", seq, result,
				reqIn.username);

		rspOut.result = (byte) result;
		sessionIn.send(rspOut);
	}
}