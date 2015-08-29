package com.metazion.jgd;

import com.metazion.jgd.net.TcpClient;

public class AppClient {
	
	public static void main(String[] args) {
		AppClient appClient = new AppClient();
		appClient.init();
		appClient.tick();
	}
	
	private TcpClient<ClientSession> tcpClient = new TcpClient<ClientSession>(ClientSession.class);
	
	public void init() {
		System.out.println("AppClient init...");
		
		tcpClient.setRemoteAddress("127.0.0.1", 20001);
		tcpClient.setReconnectInterval(5);
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