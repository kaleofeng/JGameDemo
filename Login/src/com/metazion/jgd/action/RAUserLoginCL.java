package com.metazion.jgd.action;

import com.metazion.jgd.AppLogin;
import com.metazion.jgd.protocal.Message;
import com.metazion.jgd.protocal.cl.UserLoginCL;
import com.metazion.jgd.task.TaskLoadUser;
import com.metazion.jgd.util.JgdLogger;
import com.metazion.object.User;

public class RAUserLoginCL extends RequestAction {

	UserLoginCL req = null;

	@Override
	public void setRequest(Message msg) {
		req = (UserLoginCL) msg;
	}

	@Override
	public void execute() {
		final String username = req.username;
		final String password = req.password;

		JgdLogger.getLogger().info("User login cl: username[{}] password[{}]", username, password);

		TaskLoadUser task = new TaskLoadUser(session, req);

		User user = AppLogin.getLogicService().getUserManager().getUser(username);
		if (user != null) {
			task.setUserData(user.getUserData());
			JgdLogger.getLogger().debug("User login cl: user already in");
		}

		JgdLogger.getLogger().info("User login cl next: begin to load user of username[{}]", username);

		AppLogin.getLogicService().getTaskManager().pushTask(task);
	}
}