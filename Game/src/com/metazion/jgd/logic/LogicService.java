package com.metazion.jgd.logic;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.metazion.jgd.action.RequestAction;
import com.metazion.jgd.info.ServerConfig;
import com.metazion.jgd.info.SystemConfig;
import com.metazion.jgd.util.DbUtil;
import com.metazion.jgd.util.JgdLogger;
import com.metazion.jm.task.TaskManager;
import com.metazion.object.PlayerManager;
import com.metazion.object.UserManager;;

public class LogicService {

	private volatile boolean stopDesired = false; // 优雅关闭

	private ConcurrentLinkedQueue<RequestAction> requestActionQueue = new ConcurrentLinkedQueue<RequestAction>();
	private TaskManager taskManager = new TaskManager();

	private UserManager userManager = new UserManager(); // 用户
	private PlayerManager playerManager = new PlayerManager(); // 玩家

	public boolean init() {
		JgdLogger.getLogger().fatal("Logic service init...");

		boolean result = ServerConfig.getInstance().load();
		if (!result) {
			return false;
		}

		result = SystemConfig.getInstance().load();
		if (!result) {
			return false;
		}

		result = DbUtil.init();
		if (!result) {
			return false;
		}

		return true;
	}

	public void start() {
		JgdLogger.getLogger().fatal("Logic service start...");
	}

	public void stop() {
		JgdLogger.getLogger().fatal("Logic service stop...");
	}

	public void tick(long interval) {
		processRequestAction();

		taskManager.tick(interval);
	}

	public void shutdownGracefully() {
		JgdLogger.getLogger().fatal("Logic service shutdownGracefully...");

		stopDesired = true;
	}

	public void pushRequestAction(RequestAction requestAction) {
		// 优雅关闭状态下，不再接受网络请求
		if (stopDesired) {
			return;
		}

		requestActionQueue.add(requestAction);
	}

	public TaskManager getTaskManager() {
		return taskManager;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	private void processRequestAction() {
		while (!requestActionQueue.isEmpty()) {
			RequestAction requestAction = requestActionQueue.poll();
			requestAction.execute();
		}
	}
}