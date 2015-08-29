package com.metazion.jgd;

import com.metazion.jgd.net.TcpServer;

public class AppServer {
	
	public static void main(String[] args) {
		AppServer appServer = new AppServer();
		appServer.init();
		appServer.tick();
	}
	
	private TcpServer<ServerSession> tcpServer = new TcpServer<ServerSession>(ServerSession.class);
	
	public void init() {
		System.out.println("AppServer init...");
		
		tcpServer.addLocalPort(20001);
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