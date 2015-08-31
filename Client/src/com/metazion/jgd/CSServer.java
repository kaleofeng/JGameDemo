package com.metazion.jgd;

import com.metazion.jm.net.ClientSession;
import com.metazion.jm.net.Session;

import io.netty.channel.Channel;

public class CSServer extends ClientSession {

	@Override
	public void onActive() throws Exception {
		System.out.println(String.format("Client session to server active: [%s]", getChannelAddress(channel)));
	}

	@Override
	public void onInactive() throws Exception {
		System.out.println(String.format("Client session to server inactive: [%s]", getChannelAddress(channel)));
	}

	@Override
	public void onException() throws Exception {
		System.out.println(String.format("Client session to server exception: [%s]", getChannelAddress(channel)));
	}

	private String getChannelAddress(Channel channel) {
		return String.format("localAddress[%s] remoteAddress[%s]", channel.localAddress(), channel.remoteAddress());
	}
	
	@Override
	public void onReceive(Object data) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void send(Object data) {
		// TODO Auto-generated method stub
		
	}

}