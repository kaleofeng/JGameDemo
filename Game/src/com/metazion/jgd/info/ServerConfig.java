package com.metazion.jgd.info;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import com.metazion.jgd.util.JgdLogger;
import com.metazion.jm.net.Endpoint;
import com.metazion.jm.util.FileUtil;

public class ServerConfig {

	private static ServerConfig singleton = new ServerConfig();

	public static ServerConfig getInstance() {
		return singleton;
	}

	// 服务器组ID
	public int serverId = 0;

	// 内网监听地址
	public String privateHost = "";
	public int privatePort = 0;

	// 外网监听地址
	public String publicHost = "";
	public int publicPort = 0;

	// 登录服务器地址
	public ArrayList<Endpoint> loginEndpointList = new ArrayList<Endpoint>();

	// 区域
	public String locale = "";

	// 服务器状态阈值
	public int crowdThreshold = 0;
	public int fullThreshold = 0;

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

	public void print() {
		JgdLogger.getLogger().info("{}", info());
	}

	public String info() {
		String info = "Game Server Config As Below:\n";
		info += String.format("  server id[%d]\n", serverId);
		info += String.format("  private host[%s] port[%d]\n", privateHost, privatePort);
		info += String.format("  public host[%s] port[%d]\n", publicHost, publicPort);
		for (Endpoint endpoint : loginEndpointList) {
			info += String.format("  login endpoint host[%s] port[%d]\n", endpoint.host, endpoint.port);
		}
		info += String.format("  locale[%s]\n", locale);
		info += String.format("  crowd threshold[%d] full threshold[%d]\n", crowdThreshold, fullThreshold);
		return info;
	}

	private void loadProperties(Properties properties) {
		serverId = Integer.parseInt(properties.getProperty("serverId"));

		privateHost = properties.getProperty("privateHost");
		privatePort = Integer.parseInt(properties.getProperty("privatePort"));

		publicHost = properties.getProperty("publicHost");
		publicPort = Integer.parseInt(properties.getProperty("publicPort"));

		String loginHosts = properties.getProperty("loginHosts");
		String loginPorts = properties.getProperty("loginPorts");
		parseEndpoints(loginHosts, loginPorts, loginEndpointList);

		locale = properties.getProperty("locale");

		crowdThreshold = Integer.parseInt(properties.getProperty("crowdThreshold"));
		fullThreshold = Integer.parseInt(properties.getProperty("fullThreshold"));
	}

	private void parseEndpoints(String hosts, String ports, ArrayList<Endpoint> endpointList) {
		if (hosts.isEmpty() || ports.isEmpty()) {
			return;
		}

		String[] journalHostArray = hosts.split(",");
		String[] journalPortArray = ports.split(",");

		if (journalHostArray.length != journalPortArray.length) {
			return;
		}

		final int journalSize = journalHostArray.length;
		for (int index = 0; index < journalSize; ++index) {
			Endpoint endpoint = new Endpoint();
			endpoint.host = journalHostArray[index];
			endpoint.port = Integer.parseInt(journalPortArray[index]);
			endpointList.add(endpoint);
		}
	}
}