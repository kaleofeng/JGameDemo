package com.metazion.jgd.net;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.util.AttributeKey;

public class TcpClient<T extends Session> {

	private final AttributeKey<T> SESSIONKEY = AttributeKey.valueOf("SESSIONKEY");
	private Class<T> cls = null;

	private final Bootstrap bootstrap = new Bootstrap();
	private EventLoopGroup workerGroup = null;

	private String remoteHost = "";
	private int remotePort = 0;

	private int reconnectInterval = 10;

	private boolean isWorking = false;
	private Channel channel = null;

	public TcpClient(Class<T> cls) {
		this.cls = cls;
	}

	public void setRemoteAddress(String host, int port) {
		remoteHost = host;
		remotePort = port;
	}

	public void setReconnectInterval(int seconds) {
		reconnectInterval = seconds;
	}

	public void connect() {
		isWorking = true;
		channel = null;
		start();
		tryConnect();
	}

	public void close() {
		isWorking = false;
		channel = null;
		stop();
	}

	public void sendMessage(Object msg) {
		if (isConnected()) {
			channel.writeAndFlush(msg);
		}
	}

	public boolean isConnected() {
		return channel != null;
	}

	private void start() {
		workerGroup = new NioEventLoopGroup();
		bootstrap.group(workerGroup);
		bootstrap.channel(NioSocketChannel.class);
		bootstrap.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast("encode", new ObjectEncoder());
				pipeline.addLast("decode", new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
				pipeline.addLast(new ChannelInboundHandlerAdapter() {
					@Override
					public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
						Session session = ctx.attr(SESSIONKEY).get();
						session.onReceive(msg);
					}

					@Override
					public void channelActive(ChannelHandlerContext ctx) throws Exception {
						T session = cls.newInstance();
						ctx.attr(SESSIONKEY).set(session);
						session.setChannel(ctx.channel());
						session.onActive();
					}

					@Override
					public void channelInactive(ChannelHandlerContext ctx) throws Exception {
						Session session = ctx.attr(SESSIONKEY).get();
						session.onInactive();

						ctx.channel().eventLoop().schedule(() -> tryConnect(), reconnectInterval, TimeUnit.SECONDS);
					}

					@Override
					public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
						Session session = ctx.attr(SESSIONKEY).get();
						session.onException();
					}
				});
			}
		});

		bootstrap.option(ChannelOption.SO_KEEPALIVE, false);
	}

	private void stop() {
		workerGroup.shutdownGracefully();
	}

	private void tryConnect() {
		if (!isWorking) {
			return;
		}

		ChannelFuture future = bootstrap.connect(new InetSocketAddress(remoteHost, remotePort));
		future.addListener(new ChannelFutureListener() {
			public void operationComplete(ChannelFuture f) throws Exception {
				if (f.isSuccess()) {
					System.out.println(String.format("Tcp client connect success: %s", getRemoteInfo()));
					channel = f.channel();
				} else {
					System.out.println(String.format("Tcp client connect failed: %s", getRemoteInfo()));
					f.channel().eventLoop().schedule(() -> tryConnect(), reconnectInterval, TimeUnit.SECONDS);
				}
			}
		});
	}

	private String getRemoteInfo() {
		return String.format("remote host[%s] port[%s]", remoteHost, remotePort);
	}
}