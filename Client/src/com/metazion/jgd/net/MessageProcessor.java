package com.metazion.jgd.net;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import com.metazion.jgd.protocal.Message;
import com.metazion.jgd.protocal.Protocal;
import com.metazion.jgd.protocal.cl.UserLoginLC;
import com.metazion.jgd.util.JgdLogger;

import io.netty.channel.ChannelHandlerContext;

public class MessageProcessor {

	public static void process(ChannelHandlerContext ctx, Object data) throws Exception {
		ctx.close();

		if (data instanceof byte[]) {
			Message msg = deserialize(data);
			process(ctx, msg);
		}
	}

	private static Message deserialize(Object data) throws Exception {
		byte[] bytes = (byte[]) data;
		// 对数据解压解密等

		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		ObjectInputStream dis = new ObjectInputStream(bais);
		Message msg = (Message) dis.readObject();
		dis.close();
		bais.close();

		return msg;
	}

	private static void process(ChannelHandlerContext ctx, Message msg) throws IOException {
		final int protocal = msg.procotal;

		switch (protocal) {
		case Protocal.PN_CL_USERLOGIN:
			handleUserLoginLC(msg);
			break;
		default:
			break;
		}
	}

	private static void handleUserLoginLC(Message msg) {
		UserLoginLC rsp = (UserLoginLC) msg;
		JgdLogger.getLogger().debug("Message processor handle user login lc: result[{}]", rsp.result);

		if (rsp.result == UserLoginLC.SUCCESS) {
			JgdLogger.getLogger().debug("user id[{}] token[{}]", rsp.userId, rsp.token);
		}
	}
}