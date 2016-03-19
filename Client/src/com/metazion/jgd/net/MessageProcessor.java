package com.metazion.jgd.net;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import com.metazion.jgd.def.ServerBean;
import com.metazion.jgd.protocal.Message;
import com.metazion.jgd.protocal.Protocal;
import com.metazion.jgd.protocal.cl.SelectServerLC;
import com.metazion.jgd.protocal.cl.UserLoginLC;
import com.metazion.jgd.protocal.cl.UserRegisterLC;
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
		case Protocal.PN_CL_USERREGISTER:
			handleUserRegisterLC(msg);
			break;
		case Protocal.PN_CL_USERLOGIN:
			handleUserLoginLC(msg);
			break;
		case Protocal.PN_CL_SELECTSERVER:
			handleSelectServerLC(msg);
			break;

		default:
			break;
		}
	}

	private static void handleUserRegisterLC(Message msg) {
		UserRegisterLC rsp = (UserRegisterLC) msg;
		JgdLogger.getLogger().debug("Message processor handle user register lc: result[{}]", rsp.result);

		if (rsp.result == UserLoginLC.SUCCESS) {
			// Nothing to do.
		}
	}

	private static void handleUserLoginLC(Message msg) {
		UserLoginLC rsp = (UserLoginLC) msg;
		JgdLogger.getLogger().debug("Message processor handle user login lc: result[{}]", rsp.result);

		if (rsp.result == UserLoginLC.SUCCESS) {
			ClientHelper.userId = rsp.userId;
			ClientHelper.token = rsp.token;

			JgdLogger.getLogger().debug("user id[{}] token[{}]", rsp.userId, rsp.token);

			for (ServerBean sb : rsp.serverList) {
				JgdLogger.getLogger().debug("server id[{}] name[{}] status[{}]", sb.id, sb.name, sb.status);
			}
		}
	}

	private static void handleSelectServerLC(Message msg) {
		SelectServerLC rsp = (SelectServerLC) msg;
		JgdLogger.getLogger().debug("Message processor handle select server lc: result[{}]", rsp.result);

		if (rsp.result == SelectServerLC.SUCCESS) {
			JgdLogger.getLogger().debug("user id[{}] token[{}] host[{}] port[{}]", rsp.userId, rsp.token, rsp.host, rsp.port);
		}
	}
}