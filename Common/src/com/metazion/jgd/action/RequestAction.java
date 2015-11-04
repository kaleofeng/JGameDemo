package com.metazion.jgd.action;

import com.metazion.jgd.protocal.Message;
import com.metazion.jm.net.TransmitSession;

public abstract class RequestAction {

	protected TransmitSession session = null;

	public void setSession(TransmitSession session) {
		this.session = session;
	}

	public abstract void setRequest(Message msg);

	public abstract void execute();
}