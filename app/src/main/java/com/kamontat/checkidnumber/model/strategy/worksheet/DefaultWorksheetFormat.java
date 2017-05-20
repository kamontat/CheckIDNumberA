package com.kamontat.checkidnumber.model.strategy.worksheet;

import com.kamontat.checkidnumber.model.IDNumber;

/**
 * @author kamontat
 * @version 1.0
 * @since Thu 18/May/2017 - 2:55 PM
 */
public class DefaultWorksheetFormat implements WorksheetFormat<IDNumber, DefaultWorksheetFormat.PositionValue> {
	@Override
	public PositionValue[] getCellsInRow(long id, IDNumber idNumber) {
		return new PositionValue[]{new PositionValue(0, String.valueOf(id)), new PositionValue(1, idNumber.getId())};
	}
	
	public class PositionValue {
		private int column;
		private String value;
		
		private PositionValue(int column, String value) {
			this.column = column;
			this.value = value;
		}
		
		public int getColumn() {
			return column;
		}
		
		public String getValue() {
			return value;
		}
	}
}
