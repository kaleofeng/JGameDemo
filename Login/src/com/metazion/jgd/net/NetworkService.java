package com.metazion.jgd.net;

import com.metazion.jgd.info.ServerConfig;
import com.metazion.jgd.util.JgdLogger;
import com.metazion.jm.net.TcpServer;

public class NetworkService {

	private TcpServer tcpServer = new TcpServer();

	private LSServer lsServer = new LSServer();
	private LSClient lsClient = new LSClient();

	public boolean init() {
		JgdLogger.getLogger().fatal("Network service init...");

		final int privatePort = ServerConfig.getInstance().privatePort;
		lsServer.setLocalPort(privatePort);
		lsServer.setRelistenInterval(10);
		tcpServer.attach(lsServer);

		final int publicPort = ServerConfig.getInstance().publicPort;
		lsClient.setLocalPort(publicPort);
		lsClient.setRelistenInterval(10);
		tcpServer.attach(lsClient);

		return true;
	}

	public void start() {
		JgdLogger.getLogger().fatal("Network service start...");

		lsServer.open();
		lsClient.open();

		tcpServer.listen();
	}

	public void stop() {
		JgdLogger.getLogger().fatal("Network service stop...");

		lsServer.close();
		lsClient.close();

		tcpServer.close();
	}
}