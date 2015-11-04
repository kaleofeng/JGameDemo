package com.metazion.jgd.protocal;

public class Protocal {

	// client <--> login
	public static final short PN_CL_USERREGISTER = 1000; // 注册帐号
	public static final short PN_CL_USERLOGIN = 1001; // 登录验证
	public static final short PN_CL_SELECTSERVER = 1002; // 选择服务器

	// server <--> login
	public static final short PN_SL_SERVERJOIN = 2001; // 服务器组加入
	public static final short PN_SL_SERVEREXIT = 2002; // 服务器组退出
	public static final short PN_SL_SERVERREPORT = 2003; // 服务器组汇报

	public static final short PN_LS_USERCANDIDATE = 2101; // 登录通知

	// client <--> server
	public static final short PN_CS_PLAYERENTER = 3000; // 进入游戏
	public static final short PN_CS_PLAYERLEAVE = 3001; // 离开游戏
	public static final short PN_CS_PLAYERCREATE = 3002; // 创建角色

	// server <--> journal
	public static final short PN_SJ_PLAYERINOUT = 4001; // 玩家进入离开游戏
	public static final short PN_SJ_PLAYERRESOURCE = 4002; // 玩家资源变动情况
}