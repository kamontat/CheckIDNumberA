package com.kamontat.checkidnumber.view;

import android.content.Context;
import com.kamontat.checkidnumber.model.IDNumber;

/**
 * @author kamontat
 * @version 1.0
 * @since Mon 15/May/2017 - 1:23 AM
 */
public interface MainView {
	String getInputText();
	
	void checkAndInsert();
	
	void updateInput(IDNumber id);
	
	void hideKeyBoard();
	
	void showKeyBoard();
	
	boolean isStorageWritable();
	
	IDNumber[] getIDNumbers();
	
	Context getContext();
	
	boolean checkPermission();
	
	/**
	 * @return <ul>
	 * <li>0 - already have permission</li>
	 * <li>1 - on requesting permission</li>
	 * <li>2 - deny before</li>
	 * </ul>
	 */
	int requestPermission();
}