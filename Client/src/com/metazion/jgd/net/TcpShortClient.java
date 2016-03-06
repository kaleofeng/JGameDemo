package com.metazion.jgd.net;

import com.metazion.jgd.AppClient;
import com.metazion.jgd.util.JgdLogger;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class TcpShortClient {

	private static Bootstrap bootstrap = new Bootstrap();
	private static EventLoopGroup group = null;

	public static void start() {
		group = new NioEventLoopGroup();
		bootstrap.group(group).channel(NioSocketChannel.class);
		bootstrap.handler(new ChannelInitializer<Channel>() {
			@Override
			protected void initChannel(Channel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast("encode", new ObjectEncoder());
				pipeline.addLast("decode", new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
				pipeline.addLast("handler", new ChannelInboundHandlerAdapter() {
					@Override
					public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
						ContextMsg contextMsg = new ContextMsg(ctx, msg);
						AppClient.client.pushContextMsg(contextMsg);
					}

					@Override
					public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
						// cause.printStackTrace();
						ctx.close();
					}
				});
			}
		});

		bootstrap.option(ChannelOption.SO_KEEPALIVE, false);
	}

	public static void stop() {
		group.shutdownGracefully();
	}

	public static void sendMessage(String host, int port, Object msg) {
		try {
			Channel newChannel = createChannel(host, port);
			if (newChannel != null) {
				newChannel.writeAndFlush(msg).sync();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Channel createChannel(String host, int port) {
		Channel channel = null;
		try {
			channel = bootstrap.connect(host, port).sync().channel();
		} catch (Exception e) {
			JgdLogger.getLogger().info("Tcp short client create channel failed: host[{}] port[{}] exception[{}]", host, port, e.toString());
			e.printStackTrace();
			return null;
		}

		return channel;
	}
}
