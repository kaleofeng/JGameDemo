package com.metazion.jgd.info;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import com.metazion.jgd.util.JgdLogger;
import com.metazion.jm.util.FileUtil;

public class SystemConfig {

	private static SystemConfig singleton = new SystemConfig();

	public static SystemConfig getInstance() {
		return singleton;
	}

	public int onlineExpireTime = 300 * 1000; // 在线过期时间（ms)
	public int offlineHoldTime = 600 * 1000; // 离线保持时间（ms)
	public int cacheInterval = 30 * 1000; // 自动缓存间隔（ms）
	public int saveInterval = 300 * 1000; // 自动存档间隔（ms）

	private SystemConfig() {

	}

	public boolean load() {
		String path = FileUtil.getAbsolutePath("data/config/system.properties");
		try {
			InputStream is = new FileInputStream(path);
			Properties properties = new Properties();
			properties.load(is);
			is.close();

			loadProperties(properties);
		} catch (Exception e) {
			JgdLogger.getLogger().error("Load global value failed: file[{}] exception[{}]", path, e.toString());
			return false;
		}

		return true;
	}

	public void print() {
		JgdLogger.getLogger().info("{}", info());
	}

	public String info() {
		String info = "Game System Config As Below:\n";
		info += String.format("  online expire time[%d]\n", onlineExpireTime);
		info += String.format("  offline hold time[%d]\n", offlineHoldTime);
		info += String.format("  cache interval[%d]\n", cacheInterval);
		info += String.format("  save interval[%d]\n", saveInterval);
		return info;
	}

	private void loadProperties(Properties properties) {
		onlineExpireTime = Integer.parseInt(properties.getProperty("onlineExpireTime"));
		offlineHoldTime = Integer.parseInt(properties.getProperty("offlineHoldTime"));
		cacheInterval = Integer.parseInt(properties.getProperty("cacheInterval"));
		saveInterval = Integer.parseInt(properties.getProperty("saveInterval"));
	}
}