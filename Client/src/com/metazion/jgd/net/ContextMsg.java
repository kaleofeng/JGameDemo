package com.metazion.jgd.net;

import io.netty.channel.ChannelHandlerContext;

public class ContextMsg {

	public ChannelHandlerContext ctx = null;
	public Object msg = null;

	ContextMsg(ChannelHandlerContext ctx, Object msg) {
		this.ctx = ctx;
		this.msg = msg;
	}
}