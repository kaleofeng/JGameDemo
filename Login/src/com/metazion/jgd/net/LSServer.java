package com.metazion.jgd.net;

import com.metazion.jm.net.ListenSession;
import com.metazion.jm.net.ServerSession;

public class LSServer extends ListenSession {

	@Override
	public ServerSession createServerSession() {
		ServerSession session = new SSServer();
		session.open();
		return session;
	}

}