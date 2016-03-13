package com.metazion.jgd.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.metazion.jgd.AppGame;
import com.metazion.jgd.action.RequestAction;
import com.metazion.jgd.action.RequestActionFactory;
import com.metazion.jgd.protocal.Message;
import com.metazion.jgd.util.JgdLogger;
import com.metazion.jm.net.ServerSession;

import io.netty.channel.Channel;

public class SSClient extends ServerSession {

	@Override
	public void onActive() throws Exception {
		JgdLogger.getLogger().trace("Server session from client active: {}", getChannelAddress(channel));
	}

	@Override
	public void onInactive() throws Exception {
		JgdLogger.getLogger().trace("Server session from client inactive: {}", getChannelAddress(channel));
	}

	@Override
	public void onException(Throwable cause) throws Exception {
		JgdLogger.getLogger().warn("Server session from client exception: {} cause[{}]", getChannelAddress(channel), cause.toString());
	}

	@Override
	public void onReceive(Object data) throws Exception {
		process(data);
	}

	@Override
	public void send(Object data) {
		write(data);
	}

	public void write(Object data) {
		try {
			Message msg = (Message) data;
			byte[] bytes = serialize(msg);
			writeAndFlush(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private byte[] serialize(Message msg) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(msg);
		oos.close();
		baos.close();

		byte[] bytes = baos.toByteArray();
		// 对数据加密压缩等
		return bytes;
	}

	private void process(Object data) throws Exception {
		if (data instanceof byte[]) {
			deserialize(data);
		}
	}

	private void deserialize(Object data) throws Exception {
		byte[] bytes = (byte[]) data;
		// 对数据解压解密等

		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		ObjectInputStream dis = new ObjectInputStream(bais);
		Message msg = (Message) dis.readObject();
		dis.close();
		bais.close();

		final int protocal = msg.procotal;
		RequestAction requestAction = RequestActionFactory.createRequestAction(protocal);
		requestAction.setSession(this);
		requestAction.setRequest(msg);
		AppGame.getLogicService().pushRequestAction(requestAction);
	}

	private String getChannelAddress(Channel channel) {
		return String.format("localAddress[%s] remoteAddress[%s]", channel.localAddress(), channel.remoteAddress());
	}
}