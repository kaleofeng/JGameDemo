package com.metazion.jgd.protocal.cl;

import com.metazion.jgd.protocal.Message;
import com.metazion.jgd.protocal.Protocal;

@SuppressWarnings("serial")
public class UserLoginCL extends Message {

	public String username = "";
	public String password = "";

	public UserLoginCL() {
		super(Protocal.PN_CL_USERLOGIN);
	}
}