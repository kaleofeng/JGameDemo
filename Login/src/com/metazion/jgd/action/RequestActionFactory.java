package com.metazion.jgd.action;

import com.metazion.jgd.protocal.Protocal;

public class RequestActionFactory {

	public static RequestAction createRequestAction(int type) {

		RequestAction requestAction = null;

		switch (type) {
		case Protocal.PN_CL_USERLOGIN:
			requestAction = new RAUserLoginCL();
			break;

		default:
			requestAction = new RAUnknown();
		}

		assert requestAction != null;
		return requestAction;
	}
}