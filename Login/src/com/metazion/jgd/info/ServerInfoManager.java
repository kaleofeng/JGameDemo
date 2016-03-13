package com.metazion.jgd.info;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.metazion.jgd.def.ServerBean;
import com.metazion.jgd.util.JgdLogger;
import com.metazion.jm.util.FileUtil;

public class ServerInfoManager {

	private HashMap<Integer, ServerInfo> serverInfoMap = new HashMap<Integer, ServerInfo>();

	private static ServerInfoManager singleton = new ServerInfoManager();

	public static ServerInfoManager getInstance() {
		return singleton;
	}

	private ServerInfoManager() {

	}

	public boolean load() {
		String path = FileUtil.getAbsolutePath("data/config/server_list.json");
		try {
			InputStream is = new FileInputStream(path);
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String content = "";
			String line = null;
			while ((line = br.readLine()) != null) {
				content += line;
			}
			is.close();

			loadServerInfo(content);
		} catch (Exception e) {
			JgdLogger.getLogger().error("Load server list failed: file[{}] exception[{}]", path, e.toString());
			return false;
		}

		return true;
	}

	public int GetServerInfoSize() {
		return serverInfoMap.size();
	}

	public ServerInfo getServerInfo(int id) {
		return serverInfoMap.get(id);
	}

	public HashMap<Integer, ServerInfo> getAllServerInfo() {
		return serverInfoMap;
	}

	public String info() {
		String info = "Login All Game Info As Below:\n";
		info += String.format("  game size[%d]\n", serverInfoMap.size());
		for (ServerInfo serverInfo : serverInfoMap.values()) {
			ServerBean serverBean = serverInfo.serverBean;
			info += String.format("  game id[%d] name[%s] status[%d] host[%s] port[%d] crowd threshold[%d] full threshold[%d] player number[%d]\n", serverBean.id, serverBean.name, serverBean.status, serverInfo.host, serverInfo.port, serverInfo.crowdThreshold, serverInfo.fullThreshold, serverInfo.playerNumber);
		}
		return info;
	}

	private void loadServerInfo(String content) throws Exception {
		serverInfoMap.clear();

		JSONArray jsonArray = new JSONArray(content);
		for (int index = 0; index < jsonArray.length(); index++) {
			JSONObject jsonObject = jsonArray.getJSONObject(index);
			ServerInfo serverInfo = new ServerInfo();
			serverInfo.serverBean.id = jsonObject.getInt("id");
			serverInfo.serverBean.name = jsonObject.getString("name");
			serverInfoMap.put(serverInfo.serverBean.id, serverInfo);
		}
	}
}