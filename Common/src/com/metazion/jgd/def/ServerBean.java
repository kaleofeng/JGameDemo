package com.metazion.jgd.def;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ServerBean implements Serializable {

	public static final byte STATUS_CLOSED = 0; // 未开启
	public static final byte STATUS_NORMAL = 1; // 正常
	public static final byte STATUS_CROWD = 2; // 繁忙
	public static final byte STATUS_FULL = 3; // 爆满

	public int id = 0; // 服务器ID
	public String name = ""; // 服务器名称
	public byte status = 0; // 服务器状态
}