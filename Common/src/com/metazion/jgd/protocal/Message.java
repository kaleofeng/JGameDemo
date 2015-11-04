package com.metazion.jgd.protocal;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Message implements Serializable {

	public static final byte SUCCESS = 1; // 成功
	public static final byte FAILED = 0; // 失败
	public static final byte TIMEOUT = -1; // 超时

	public short procotal = 0;

	public Message(short command) {
		this.procotal = command;
	}
}
