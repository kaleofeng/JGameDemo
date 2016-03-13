package com.metazion.jgd.net;

import java.util.ArrayList;

import com.metazion.jgd.info.ServerConfig;
import com.metazion.jgd.util.JgdLogger;
import com.metazion.jm.net.Endpoint;
import com.metazion.jm.net.TcpClient;
import com.metazion.jm.net.TcpServer;

public class NetworkService {

	private TcpServer tcpServer = new TcpServer();
	private TcpClient tcpClient = new TcpClient();

	private LSServer lsServer = new LSServer();
	private LSClient lsClient = new LSClient();

	private ArrayList<CSLogin> csLoginList = new ArrayList<CSLogin>();

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

		for (Endpoint endpoint : ServerConfig.getInstance().loginEndpointList) {
			CSLogin csLogin = new CSLogin();
			csLogin.setRemoteHost(endpoint.host);
			csLogin.setRemotePort(endpoint.port);
			csLogin.setReconnectInterval(10);
			tcpClient.attach(csLogin);

			csLoginList.add(csLogin);
		}

		return true;
	}

	public void start() {
		JgdLogger.getLogger().fatal("Network service start...");

		lsServer.open();
		lsClient.open();

		for (CSLogin csLogin : csLoginList) {
			csLogin.open();
		}

		tcpServer.listen();
		tcpClient.connect();
	}

	public void stop() {
		JgdLogger.getLogger().fatal("Network service stop...");

		lsServer.close();
		lsClient.close();

		for (CSLogin csLogin : csLoginList) {
			csLogin.close();
		}

		tcpServer.close();
		tcpClient.close();
	}

	public void sendToLogin(Object data) {
		for (CSLogin csLogin : csLoginList) {
			csLogin.send(data);
		}
	}
}