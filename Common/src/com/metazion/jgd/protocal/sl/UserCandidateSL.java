package com.metazion.jgd.protocal.sl;

import com.metazion.jgd.protocal.Message;
import com.metazion.jgd.protocal.Protocal;

@SuppressWarnings("serial")
public class UserCandidateSL extends Message {

	public int serverId = 0;
	public int userId = 0;

	public UserCandidateSL() {
		super(Protocal.PN_LS_USERCANDIDATE);
	}
}