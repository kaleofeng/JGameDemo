package com.metazion.object;

import java.util.ArrayList;
import java.util.HashMap;

import com.metazion.jgd.util.JgdLogger;

public class PlayerManager {

	private HashMap<Integer, Player> playerMap = new HashMap<Integer, Player>();
	private HashMap<Integer, Integer> accountPlayerMap = new HashMap<Integer, Integer>();

	private ArrayList<Player> expiredPlayerList = new ArrayList<Player>();

	public PlayerManager() {

	}

	public boolean init() {
		JgdLogger.getLogger().info("Player manager init...");
		return true;
	}

	public void start() {
		JgdLogger.getLogger().info("Player manager start...");
	}

	public void stop() {
		JgdLogger.getLogger().info("Player manager stop...");
	}

	public void tick(long iterval) {
		expiredPlayerList.clear();

		for (Player player : playerMap.values()) {
			player.tick(iterval);

			if (player.isExpired()) {
				expiredPlayerList.add(player);
			}
		}

		for (Player player : expiredPlayerList) {
			removePlayerMapping(player);
		}
	}

	public void shutdownGracefully() {
		JgdLogger.getLogger().info("Player manager shutdownGracefully...");

		expiredPlayerList.clear();

		for (Player player : playerMap.values()) {
			expiredPlayerList.add(player);
		}

		for (Player player : expiredPlayerList) {
			removePlayerMapping(player);
		}
	}

	public void putPlayerMapping(Player player) {
		final int playerId = player.getId();
		final int account = player.getAccount();

		putPlayer(playerId, player);
		putAccountPlayer(account, playerId);
		player.enter();
	}

	public void removePlayerMapping(Player player) {
		final int playerId = player.getId();
		final int account = player.getAccount();

		player.leave();
		removeAccountPlayer(account);
		removePlayer(playerId);
	}

	public Player getPlayerByAccount(int account) {
		Integer playerId = getAccountPlayer(account);
		if (playerId != null) {
			return getPlayer(playerId);
		}
		return null;
	}

	public Player getPlayerById(int playerId) {
		return getPlayer(playerId);
	}

	public int getAccountSize() {
		return accountPlayerMap.size();
	}

	public int getPlayerSize() {
		return playerMap.size();
	}

	public void kickPlayer(int playerId) {
		Player player = getPlayer(playerId);
		if (player != null) {
			player.setExpired(true);
		}
	}

	public void kickAllPlayer() {
		for (Player player : playerMap.values()) {
			player.setExpired(true);
		}
	}

	private Player getPlayer(int playerId) {
		return playerMap.get(playerId);
	}

	private void putPlayer(int playerId, Player player) {
		playerMap.put(playerId, player);
	}

	private void removePlayer(int playerId) {
		playerMap.remove(playerId);
	}

	private Integer getAccountPlayer(int account) {
		return accountPlayerMap.get(account);
	}

	private void putAccountPlayer(int account, int playerId) {
		accountPlayerMap.put(account, playerId);
	}

	private void removeAccountPlayer(int account) {
		accountPlayerMap.remove(account);
	}
}