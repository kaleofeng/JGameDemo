package com.metazion.jgd.net;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import com.metazion.jgd.protocal.Message;
import com.metazion.jgd.protocal.cl.UserLoginCL;

public class ClientHelper {

	// 服务器地址
	public static String serverHost = "127.0.0.1";
	public static int serverPort = 40001;

	// 用户信息
	public static int userId = 0;
	public static String token = "";

	// 角色信息
	public static int playerId = 0;

	public static void connect(String host, int port) {
		serverHost = host;
		serverPort = port;
	}

	public static void set(String key, String value) {
		if (key.equals("user")) {
			userId = Integer.parseInt(value);
		} else if (key.equals("token")) {
			token = value;
		} else if (key.equals("player")) {
			playerId = Integer.parseInt(value);
		}
	}

	public static void userLogin(String username, String password) {
		UserLoginCL request = new UserLoginCL();
		request.username = username;
		request.password = password;
		sendRequest(request);
	}

	private static void sendRequest(Message msg) {
		byte[] bytes;
		try {
			bytes = serialize(msg);
			TcpShortClient.sendMessage(serverHost, serverPort, bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static byte[] serialize(Message msg) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(msg);
		oos.close();
		baos.close();

		byte[] bytes = baos.toByteArray();
		// 对数据加密压缩等
		return bytes;
	}
}