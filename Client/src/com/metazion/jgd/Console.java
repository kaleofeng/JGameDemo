package com.metazion.jgd;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.metazion.jgd.net.ClientHelper;
import com.metazion.jgd.util.JgdLogger;

public class Console extends Thread {

	@FunctionalInterface
	public interface Function {

		void execute(String[] args);
	}

	public HashMap<String, Function> functionMap = new HashMap<String, Function>();

	public void init() {
		initCommand();
		start();
	}

	@Override
	public void run() {
		JgdLogger.getLogger().debug("Console ready");

		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			try {
				String strLine = stdin.readLine();
				processCommand(strLine);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void processCommand(String command) throws Exception {
		JgdLogger.getLogger().debug("Console process command: {}", command);

		String[] args = command.split(" ");

		Function function = functionMap.get(args[0]);
		if (function != null) {
			function.execute(args);
		}
	}

	private void initCommand() {
		functionMap.put("connect", (String[] args) -> {
			assert args.length >= 3;

			final String host = args[1];
			final int port = Integer.parseInt(args[2]);
			ClientHelper.connect(host, port);
		});

		functionMap.put("set", (String[] args) -> {
			assert args.length >= 3;

			final String key = args[1];
			final String value = args[2];
			ClientHelper.set(key, value);
		});

		functionMap.put("userregister", (String[] args) -> {
			assert args.length >= 3;

			final String username = args[1];
			final String password = args[2];
			ClientHelper.userRegister(username, password);
		});

		functionMap.put("userlogin", (String[] args) -> {
			assert args.length >= 3;

			final String username = args[1];
			final String password = args[2];
			ClientHelper.userLogin(username, password);
		});

		functionMap.put("selectserver", (String[] args) -> {
			assert args.length >= 2;

			final int serverId = Integer.parseInt(args[1]);
			ClientHelper.selectServer(serverId);
		});
	}
}