package com.kamontat.checkidnumber.model.strategy.worksheet;

import com.kamontat.checkidnumber.model.IDNumber;
import org.xlsx4j.jaxb.Context;
import org.xlsx4j.sml.Cell;

/**
 * @author kamontat
 * @version 1.0
 * @since Thu 18/May/2017 - 2:55 PM
 */
public class DefaultWorksheetFormat implements WorksheetFormat<IDNumber> {
	@Override
	public Cell[] getCellsInRow(long id, IDNumber idNumber) {
		Cell numberCell = Context.getsmlObjectFactory().createCell();
		numberCell.setV(String.valueOf(id));
		
		Cell idCell = Context.getsmlObjectFactory().createCell();
		idCell.setV(idNumber.getId());
		
		return new Cell[]{numberCell, idCell};
	}
}
