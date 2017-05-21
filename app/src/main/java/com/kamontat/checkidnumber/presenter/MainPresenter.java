package com.kamontat.checkidnumber.presenter;

import android.content.Context;
import com.kamontat.checkidnumber.model.IDNumber;
import com.kamontat.checkidnumber.model.Pool;
import com.kamontat.checkidnumber.view.MainView;

/**
 * @author kamontat
 * @version 1.0
 * @since Wed 17/May/2017 - 11:07 PM
 */
public class MainPresenter {
	private Pool pool;
	private MainView view;
	
	public MainPresenter(MainView view, Pool pool) {
		this.pool = pool;
		this.view = view;
	}
	
	public Pool getPool() {
		return pool;
	}
	
	public void addID(IDNumber idNumber) {
		pool.add(idNumber);
	}
	
	public boolean isStorageWritable() {
		return view.isStorageWritable();
	}
	
	public IDNumber[] getIDNumbers() {
		return pool.getIDNumbers();
	}
	
	public Context getContext() {
		return view.getContext();
	}
	
	public boolean requestPermission() {
		return view.requestPermission() == 0;
	}
	
	public boolean checkPermission() {
		return view.checkPermission();
	}
}
