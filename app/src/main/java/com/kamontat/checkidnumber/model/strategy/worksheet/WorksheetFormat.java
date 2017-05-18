package com.kamontat.checkidnumber.model.strategy.worksheet;

import org.xlsx4j.sml.Cell;

/**
 * @author kamontat
 * @version 1.0
 * @since Thu 18/May/2017 - 2:40 PM
 */
public interface WorksheetFormat<R> {
	
	Cell[] getCellsInRow(long id, R r);
}
