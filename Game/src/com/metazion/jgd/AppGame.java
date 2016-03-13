package com.metazion.jgd;

import com.metazion.jgd.logic.LogicService;
import com.metazion.jgd.net.NetworkService;
import com.metazion.jgd.util.JgdLogger;

public class AppGame {

	public static void main(String[] args) {
		final boolean result = AppGame.getInstance().init();
		if (result) {
			AppGame.getInstance().start();
			AppGame.getInstance().tick();
			AppGame.getInstance().stop();
		}

		System.exit(0);
	}

	private static AppGame singleton = new AppGame();

	public static AppGame getInstance() {
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

	private AppGame() {

	}

	public boolean init() {
		JgdLogger.getLogger().fatal("Game Server Init...");

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
		JgdLogger.getLogger().fatal("Game Server Start...");

		printInfo();

		logicService.start();
		networkService.start();
	}

	public void stop() {
		JgdLogger.getLogger().fatal("Game Server Stop...");

		logicService.stop();
		networkService.stop();
	}

	public void tick() {
		JgdLogger.getLogger().fatal("Game Server Tick...");

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
				JgdLogger.getLogger().warn("Game Server Tick: abnormal interval[{}]", interval);
			}

			lastTickTime = now;

			logicService.tick(interval);
		}
	}

	public void shutdownGracefully() {
		JgdLogger.getLogger().fatal("Game Server shutdownGracefully...");

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