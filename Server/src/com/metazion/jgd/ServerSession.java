package com.metazion.jgd;

import com.metazion.jgd.net.Session;

import io.netty.channel.Channel;

public class ServerSession extends Session {

	@Override
	public void send(Object data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReceive(Object data) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onActive() throws Exception {
		System.out.println(String.format("Server session active: [%s]", getChannelAddress(channel)));
	}

	@Override
	public void onInactive() throws Exception {
		System.out.println(String.format("Server session inactive: [%s]", getChannelAddress(channel)));
	}

	@Override
	public void onException() throws Exception {
		System.out.println(String.format("Server session exception: [%s]", getChannelAddress(channel)));
	}

	private String getChannelAddress(Channel channel) {
		return String.format("localAddress[%s] remoteAddress[%s]", channel.localAddress(), channel.remoteAddress());
	}
}