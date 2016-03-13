package com.metazion.object;

import com.metazion.jgd.model.PlayerData;
import com.metazion.jm.net.TransmitSession;

public abstract class Player {

	public static final int ONLINESTATUS_SLEEP = 0; // 数据离线玩家离线
	public static final int ONLINESTATUS_OFFLINE = 1; // 数据在线玩家离线
	public static final int ONLINESTATUS_ONLINE = 2; // 数据在线玩家在线

	protected PlayerData playerData = null;
	protected TransmitSession playerSession = null;

	protected int onlineStatus = 0; // 数据在线状态

	protected long lastTouchTime = 0; // 上次访问时间
	protected long offlineHoldTime = 0; // 离线保持时间
	protected boolean expired = false; // 数据过期可卸载

	protected Player() {

	}

	public PlayerData getPlayerData() {
		return playerData;
	}

	public void setPlayerData(PlayerData playerData) {
		this.playerData = playerData;
	}

	public TransmitSession getPlayerSession() {
		return playerSession;
	}

	public void setPlayerSession(TransmitSession playerSession) {
		this.playerSession = playerSession;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	public int getId() {
		return playerData.id;
	}

	public int getAccount() {
		return playerData.account;
	}

	public abstract void enter();

	public abstract void leave();

	public abstract void online();

	public abstract void offline();

	public abstract void touch();

	public abstract void hold();

	public abstract void tick(long interval);
}