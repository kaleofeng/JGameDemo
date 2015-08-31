package com.metazion.jgd;

import com.metazion.jm.net.TcpServer;

public class AppServer {
	
	public static void main(String[] args) {
		AppServer appServer = new AppServer();
		appServer.init();
		appServer.tick();
	}
	
	private TcpServer tcpServer = new TcpServer();
	
	private LSClient lsClient = new LSClient();
	
	public void init() {
		System.out.println("AppServer init...");
		
		lsClient.setLocalPort(20001);
		lsClient.setRelistenInterval(10);
		lsClient.open();
		
		tcpServer.attach(lsClient);
		tcpServer.listen();
	}
	
	public void tick() {
		System.out.println("AppServer tick...");
		
		while (true) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}