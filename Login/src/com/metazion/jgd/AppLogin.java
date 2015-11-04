package com.metazion.jgd;

import com.metazion.jgd.logic.LogicService;
import com.metazion.jgd.net.NetworkService;
import com.metazion.jgd.util.JgdLogger;

public class AppLogin {

	public static void main(String[] args) {
		final boolean result = AppLogin.getInstance().init();
		if (result) {
			AppLogin.getInstance().start();
			AppLogin.getInstance().tick();
			AppLogin.getInstance().stop();
		}

		System.exit(0);
	}

	private static AppLogin singleton = new AppLogin();

	public static AppLogin getInstance() {
		return singleton;
	}

	public static LogicService getLogicService() {
		return getInstance().logicService;
	}

	public static NetworkService getNetworkService() {
		return getInstance().networkService;
	}

	private static final int STOPWAITINTERVAL = 30 * 1000; // 优雅关闭等待间隔（ms）
	private static final int TICKSTANDARDINTERVAL = 10; // 逻辑循环标准间隔（ms）
	private static final int TICKMAXIMUMINTERVAL = 50; // 逻辑循环最大间隔（ms）

	private volatile boolean stopDesired = false; // 优雅关闭
	private volatile long stopBeginTime = 0;

	private long lastTickTime = 0;

	private LogicService logicService = new LogicService();
	private NetworkService networkService = new NetworkService();

	private AppLogin() {

	}

	public boolean init() {
		JgdLogger.getLogger().fatal("Login Server Init...");

		boolean result = logicService.init();
		if (!result) {
			return false;
		}

		result = networkService.init();
		if (!result) {
			return false;
		}

		return true;
	}

	public void start() {
		JgdLogger.getLogger().fatal("Login Server Start...");

		printInfo();

		logicService.start();
		networkService.start();
	}

	public void stop() {
		JgdLogger.getLogger().fatal("Login Server Stop...");

		logicService.stop();
		networkService.stop();
	}

	public void tick() {
		JgdLogger.getLogger().fatal("Login Server Tick...");

		while (true) {
			final long now = System.currentTimeMillis();

			if (stopDesired) {
				if (now - stopBeginTime >= STOPWAITINTERVAL) {
					break;
				}
			}

			if (lastTickTime == 0) {
				lastTickTime = now;
			}

			final long interval = now - lastTickTime;

			if (interval < TICKSTANDARDINTERVAL) {
				sleep(TICKSTANDARDINTERVAL - interval);
				continue;
			}

			if (interval > TICKMAXIMUMINTERVAL) {
				JgdLogger.getLogger().warn("Login Server Tick: abnormal interval[{}]", interval);
			}

			lastTickTime = now;

			logicService.tick(interval);
		}
	}

	public void shutdownGracefully() {
		JgdLogger.getLogger().fatal("Login Server shutdownGracefully...");

		stopDesired = true;
	}

	public void printInfo() {
		JgdLogger.getLogger().fatal("System Info: processors[{}]", Runtime.getRuntime().availableProcessors());
		JgdLogger.getLogger().fatal("System Info: max memory[{}]", Runtime.getRuntime().maxMemory());
		JgdLogger.getLogger().fatal("System Info: total memory[{}]", Runtime.getRuntime().totalMemory());
		JgdLogger.getLogger().fatal("System Info: free memory[{}]", Runtime.getRuntime().freeMemory());
	}

	private void sleep(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}