package com.metazion.jgd.protocal.cl;

import java.util.ArrayList;

import com.metazion.jgd.def.ServerBean;
import com.metazion.jgd.protocal.Message;
import com.metazion.jgd.protocal.Protocal;

@SuppressWarnings("serial")
public class UserLoginLC extends Message {

	public static final byte ERROR_NOUSER = -99; // 账户不存在
	public static final byte ERROR_BANNED = -98; // 账户在黑名单
	public static final byte ERROR_WRONGPWD = -97; // 密码错误
	public static final byte ERROR_ALREADYLOGIN = -96; // 已经登录

	public byte result = 0;
	public int userId = 0;
	public String token = "";
	public ArrayList<ServerBean> serverList = new ArrayList<ServerBean>();

	public UserLoginLC() {
		super(Protocal.PN_CL_USERLOGIN);
	}
}