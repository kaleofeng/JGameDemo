package com.metazion.jgd;

import com.metazion.jm.net.TcpClient;

public class AppClient {
	
	public static void main(String[] args) {
		AppClient appClient = new AppClient();
		appClient.init();
		appClient.tick();
	}
	
	private TcpClient tcpClient = new TcpClient();
	
	private CSServer csServer = new CSServer();
	
	public void init() {
		System.out.println("AppClient init...");
		
		csServer.setRemoteHost("127.0.0.1");
		csServer.setRemotePort(20001);
		csServer.setReconnectInterval(10);
		csServer.open();
		
		tcpClient.attach(csServer);
		tcpClient.connect();
	}
	
	public void tick() {
		System.out.println("AppClient tick...");
		
		while (true) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}