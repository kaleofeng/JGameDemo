package com.metazion.jgd.dao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DaoService {

	public static final int IOTHREADNUMBER = Runtime.getRuntime().availableProcessors() * 2;

	private static final ExecutorService exeSvc = Executors.newFixedThreadPool(IOTHREADNUMBER);

	public static void execute(Runnable daoOperation) {
		exeSvc.execute(daoOperation);
	}
}