package com.metazion.jgd.protocal;

public class Protocal {

	// server <--> login
	public static final short PN_SL_SERVERJOIN = 1001; // 服务器组加入
	public static final short PN_SL_SERVEREXIT = 1002; // 服务器组退出
	public static final short PN_SL_SERVERREPORT = 1003; // 服务器组汇报

	public static final short PN_LS_USERCANDIDATE = 1101; // 登入通知

	// client <--> login
	public static final short PN_CL_USERREGISTER = 2000; // 注册帐号
	public static final short PN_CL_USERLOGIN = 2001; // 登入验证
	public static final short PN_CL_SELECTSERVER = 2002; // 选择服务器

	// client <--> server
	public static final short PN_CS_PLAYERENTER = 3000; // 进入游戏
	public static final short PN_CS_PLAYERLEAVE = 3001; // 离开游戏
	public static final short PN_CS_PLAYERCREATE = 3002; // 创建角色

	// server <--> journal
	public static final short PN_SJ_PLAYERINOUT = 4001; // 玩家进入离开游戏
	public static final short PN_SJ_PLAYERRESOURCE = 4002; // 玩家资源变动情况
}