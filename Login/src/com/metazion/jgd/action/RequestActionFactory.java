package com.metazion.jgd.action;

import com.metazion.jgd.protocal.Protocal;

public class RequestActionFactory {

	public static RequestAction createRequestAction(int type) {

		RequestAction requestAction = null;

		switch (type) {
		case Protocal.PN_SL_SERVERJOIN:
			requestAction = new RAServerJoinSL();
			break;
		case Protocal.PN_SL_SERVEREXIT:
			requestAction = new RAServerExitSL();
			break;

		case Protocal.PN_LS_USERCANDIDATE:
			requestAction = new RAUserCandidateLS();
			break;

		case Protocal.PN_CL_USERREGISTER:
			requestAction = new RAUserRegisterCL();
			break;
		case Protocal.PN_CL_USERLOGIN:
			requestAction = new RAUserLoginCL();
			break;
		case Protocal.PN_CL_SELECTSERVER:
			requestAction = new RASelectServerCL();
			break;

		default:
			requestAction = new RAUnknown();
		}

		assert requestAction != null;
		return requestAction;
	}
}