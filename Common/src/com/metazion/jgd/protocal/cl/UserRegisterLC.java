package com.metazion.jgd.protocal.cl;

import com.metazion.jgd.protocal.Message;
import com.metazion.jgd.protocal.Protocal;

@SuppressWarnings("serial")
public class UserRegisterLC extends Message {

	public static final byte ERROR_INVALIDUSERNAME = -99; // 非法用户名
	public static final byte ERROR_DUPLICATE = -98; // 用户名已存在
	public static final byte ERROR_DBINSERT = -97; // 创建失败

	public byte result = 0;

	public UserRegisterLC() {
		super(Protocal.PN_CL_USERREGISTER);
	}
}