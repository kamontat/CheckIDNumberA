package com.kamontat.checkidnumber.presenter;

import com.kamontat.checkidnumber.model.Pool;

/**
 * @author kamontat
 * @version 1.0
 * @since Wed 17/May/2017 - 11:07 PM
 */
public class MainPresenter {
	private Pool pool;
	
	public MainPresenter(Pool pool) {
		this.pool = pool;
	}
	
	public Pool getPool() {
		return pool;
	}
}
