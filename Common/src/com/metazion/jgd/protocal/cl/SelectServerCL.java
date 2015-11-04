package com.metazion.jgd.protocal.cl;

import com.metazion.jgd.protocal.Message;
import com.metazion.jgd.protocal.Protocal;

@SuppressWarnings("serial")
public class SelectServerCL extends Message {

	public int userId = 0;
	public String token = "";
	public int serverId = 0;

	public SelectServerCL() {
		super(Protocal.PN_CL_SELECTSERVER);
	}
}