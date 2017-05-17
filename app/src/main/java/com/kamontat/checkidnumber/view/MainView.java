package com.kamontat.checkidnumber.view;

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
}
