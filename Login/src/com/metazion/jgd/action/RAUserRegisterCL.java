package com.metazion.jgd.action;

import com.metazion.jgd.AppLogin;
import com.metazion.jgd.protocal.Message;
import com.metazion.jgd.protocal.cl.UserRegisterCL;
import com.metazion.jgd.task.TaskCreateUser;
import com.metazion.jgd.util.JgdLogger;

public class RAUserRegisterCL extends RequestAction {

	UserRegisterCL req = null;

	@Override
	public void setRequest(Message msg) {
		req = (UserRegisterCL) msg;
	}

	@Override
	public void execute() {
		final String username = req.username;
		final String password = req.password;

		JgdLogger.getLogger().info("User register cl: username[{}] password[{}]", username, password);

		JgdLogger.getLogger().info("User register cl next: begin to create user of username[{}]", username);

		TaskCreateUser task = new TaskCreateUser(session, req);
		AppLogin.getLogicService().getTaskManager().pushTask(task);
	}
}