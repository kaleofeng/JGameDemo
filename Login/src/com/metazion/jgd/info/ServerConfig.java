package com.metazion.jgd.info;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import com.metazion.jgd.util.JgdLogger;
import com.metazion.jm.util.FileUtil;

public class ServerConfig {

	private static ServerConfig singleton = new ServerConfig();

	public static ServerConfig getInstance() {
		return singleton;
	}

	// 内网监听地址
	public int privatePort = 0;

	// 外网监听地址
	public int publicPort = 0;

	private ServerConfig() {

	}

	public boolean load() {
		String path = FileUtil.getAbsolutePath("data/config/server.properties");
		try {
			InputStream is = new FileInputStream(path);
			Properties properties = new Properties();
			properties.load(is);
			is.close();

			loadProperties(properties);
		} catch (Exception e) {
			JgdLogger.getLogger().error("Load server config failed: file[{}] exception[{}]", path, e.toString());
			return false;
		}

		return true;
	}

	private void loadProperties(Properties properties) {
		privatePort = Integer.parseInt(properties.getProperty("privatePort"));
		publicPort = Integer.parseInt(properties.getProperty("publicPort"));
	}
}