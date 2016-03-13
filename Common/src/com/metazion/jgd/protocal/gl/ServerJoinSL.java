package com.metazion.jgd.protocal.gl;

import com.metazion.jgd.protocal.Message;
import com.metazion.jgd.protocal.Protocal;

@SuppressWarnings("serial")
public class ServerJoinSL extends Message {

	public int serverId = 0;
	public String host = "";
	public int port = 0;
	public int crowdThreshold = 0;
	public int fullThreshold = 0;
	public int playerNumber = 0;

	public ServerJoinSL() {
		super(Protocal.PN_SL_SERVERJOIN);
	}
}