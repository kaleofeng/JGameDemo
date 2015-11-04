package com.metazion.jgd.net;

import com.metazion.jm.net.ListenSession;
import com.metazion.jm.net.ServerSession;

public class LSClient extends ListenSession {

	@Override
	public ServerSession createServerSession() {
		ServerSession session = new SSClient();
		session.open();
		return session;
	}

}