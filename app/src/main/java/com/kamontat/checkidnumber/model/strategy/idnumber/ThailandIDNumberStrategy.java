package com.kamontat.checkidnumber.model.strategy.idnumber;

import com.kamontat.checkidnumber.api.constants.Status;

import java.util.*;

import static com.kamontat.checkidnumber.api.constants.Status.*;

/**
 * @author kamontat
 * @version 1.0
 * @since Thu 11/May/2017 - 11:47 PM
 */
public class ThailandIDNumberStrategy implements IDNumberStrategy {
	@Override
	public int getIDLength() {
		return 13;
	}
	
	@Override
	public Status checking(String id) {
		if (id == null || Objects.equals(id, "")) return NOT_CREATE;
		
		char[] splitID = id.toCharArray();
		if (splitID[0] == '9') {
			return NOT_NINE;
		}
		
		int total = 0;
		for (int i = 1; i <= 12; i++) {
			int digit = Character.getNumericValue(splitID[i - 1]);
			total += (14 - i) * digit;
		}
		total = (total % 11);
		
		int lastDigit = Character.getNumericValue(splitID[splitID.length - 1]);
		if (total <= 1) {
			if (lastDigit == 1 - total) return OK;
		} else {
			if (total > 1) if (lastDigit == 11 - total) return OK;
		}
		return UNCORRECTED;
	}
}
