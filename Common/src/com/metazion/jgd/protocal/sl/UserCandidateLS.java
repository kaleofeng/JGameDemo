package com.metazion.jgd.protocal.sl;

import com.metazion.jgd.protocal.Message;
import com.metazion.jgd.protocal.Protocal;

@SuppressWarnings("serial")
public class UserCandidateLS extends Message {

	public int userId = 0;
	public String token = "";

	public UserCandidateLS() {
		super(Protocal.PN_LS_USERCANDIDATE);
	}
}