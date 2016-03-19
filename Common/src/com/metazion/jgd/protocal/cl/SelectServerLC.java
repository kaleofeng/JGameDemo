package com.metazion.jgd.protocal.cl;

import com.metazion.jgd.protocal.Message;
import com.metazion.jgd.protocal.Protocal;

@SuppressWarnings("serial")
public class SelectServerLC extends Message {

	public static final byte ERROR_ILLEGAL = -99; // 非法行为
	public static final byte ERROR_CLOSED = -98; // 服务器未开启
	public static final byte ERROR_FULL = -97; // 服务器爆满

	public byte result = 0;
	public int userId = 0;
	public String token = "";
	public String host = "";
	public int port = 0;

	public SelectServerLC() {
		super(Protocal.PN_CL_SELECTSERVER);
	}
}