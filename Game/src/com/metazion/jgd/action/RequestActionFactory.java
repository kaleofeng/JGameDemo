package com.metazion.jgd.action;

public class RequestActionFactory {

	public static RequestAction createRequestAction(int type) {

		RequestAction requestAction = null;

		switch (type) {
		default:
			requestAction = new RAUnknown();
		}

		assert requestAction != null;
		return requestAction;
	}
}