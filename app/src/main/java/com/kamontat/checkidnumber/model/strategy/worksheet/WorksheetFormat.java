package com.kamontat.checkidnumber.model.strategy.worksheet;

/**
 * @author kamontat
 * @version 1.0
 * @since Thu 18/May/2017 - 2:40 PM
 */
public interface WorksheetFormat<T, R> {
	
	R[] getCellsInRow(long id, T t);
}
