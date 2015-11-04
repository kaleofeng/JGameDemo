package com.metazion.jgd;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.metazion.jgd.net.ContextMsg;
import com.metazion.jgd.net.MessageProcessor;
import com.metazion.jgd.net.TcpShortClient;
import com.metazion.jgd.util.JgdLogger;

public class AppClient {

	public static AppClient client = new AppClient();

	public static void main(String[] args) throws Exception {
		client.init();
		client.loop();
	}

	private Console console = new Console();

	private ConcurrentLinkedQueue<ContextMsg> contextMsgQueue = new ConcurrentLinkedQueue<ContextMsg>();

	public AppClient() {

	}

	public void init() {
		JgdLogger.getLogger().debug("Client init...");

		TcpShortClient.start();

		console.init();
	}

	public void loop() {
		JgdLogger.getLogger().debug("Client loop...");

		while (true) {
			try {
				processContextMsg();

				Thread.sleep(10);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void pushContextMsg(ContextMsg contextMsg) {
		contextMsgQueue.add(contextMsg);
	}

	private void processContextMsg() throws Exception {
		while (!contextMsgQueue.isEmpty()) {
			ContextMsg contextMsg = contextMsgQueue.poll();
			MessageProcessor.process(contextMsg.ctx, contextMsg.msg);
		}
	}
}