package com.metazion.jgd.net;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.ServerBootstrap;
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
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.util.AttributeKey;

public class TcpServer<T extends Session> {

	private final AttributeKey<T> SESSIONKEY = AttributeKey.valueOf("SESSIONKEY");
	private Class<T> cls = null;

	private final ServerBootstrap bootstrap = new ServerBootstrap();
	private EventLoopGroup bossGroup = null;
	private EventLoopGroup workerGroup = null;

	private boolean isWorking = false;
	private ArrayList<Integer> portList = new ArrayList<Integer>();

	private int relistenInterval = 10;

	public TcpServer(Class<T> cls) {
		this.cls = cls;
	}

	public void addLocalPort(int port) {
		portList.add(port);
	}

	public void setRelistenInterval(int seconds) {
		relistenInterval = seconds;
	}

	public void listen() {
		isWorking = true;
		start();
		tryListenAll();
	}

	public void close() {
		isWorking = false;
		stop();
	}

	private void start() {
		bossGroup = new NioEventLoopGroup(1);
		workerGroup = new NioEventLoopGroup(4);
		bootstrap.group(bossGroup, workerGroup);
		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast("encode", new ObjectEncoder());
				pipeline.addLast("decode", new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
				pipeline.addLast(workerGroup, new ChannelInboundHandlerAdapter() {
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
					}

					@Override
					public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
						Session session = ctx.attr(SESSIONKEY).get();
						session.onException();
					}
				});
			}
		});

		bootstrap.option(ChannelOption.SO_REUSEADDR, true);
		bootstrap.option(ChannelOption.SO_KEEPALIVE, false);
	}

	private void stop() {
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
	}

	private void tryListenAll() {
		int size = portList.size();
		for (int index = 0; index < size; ++index) {
			int port = portList.get(index);
			tryListen(port);
		}
	}

	private void tryListen(int port) {
		if (!isWorking) {
			return;
		}

		ChannelFuture f = bootstrap.bind(port);
		f.addListener(new ChannelFutureListener() {
			public void operationComplete(ChannelFuture f) throws Exception {
				if (f.isSuccess()) {
					System.out.println(String.format("Tcp server listen success: port[%d]", port));
				} else {
					System.out.println(String.format("Tcp server listen failed: port[%d]", port));
					f.channel().eventLoop().schedule(() -> tryListen(port), relistenInterval, TimeUnit.SECONDS);
				}
			}
		});
	}
}