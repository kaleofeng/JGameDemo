package com.metazion.jgd.protocal.gl;

import com.metazion.jgd.protocal.Message;
import com.metazion.jgd.protocal.Protocal;

@SuppressWarnings("serial")
public class ServerExitSL extends Message {

	public int serverId = 0;

	public ServerExitSL() {
		super(Protocal.PN_SL_SERVEREXIT);
	}
}