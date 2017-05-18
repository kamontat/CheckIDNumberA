package com.kamontat.checkidnumber.model.strategy.idnumber;

import com.kamontat.checkidnumber.api.constants.Status;

/**
 * @author kamontat
 * @version 1.0
 * @since Thu 11/May/2017 - 11:45 PM
 */
public interface IDNumberStrategy {
	int getIDLength();
	
	Status checking(String id);
}
