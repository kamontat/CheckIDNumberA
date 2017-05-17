package com.kamontat.checkidnumber.api.constants;

import android.content.res.Resources;
import com.kamontat.checkidnumber.R;

/**
 * this enum use for check status of id-number
 *
 * @author kamontat
 * @version 1.0
 * @since 16/9/59 - 23:05
 */
public enum Status {
	OK("OK (Good ID)", R.color.colorCorrect),
	UNMATCHED_LENGTH("Warning (MUST be 13 Digit)", R.color.colorWarning),
	NOT_NINE("Warning (First digit CANNOT be 9)", R.color.colorWarning),
	UNCORRECTED("Error (ID NOT match with id rule)", R.color.colorError),
	NOT_CREATE("Error (NEVER assign ID)", R.color.colorError);
	
	private String message;
	private int color;
	
	Status(String message, int r_color) {
		this.message = message;
		this.color = r_color;
	}
	
	public int getColor(Resources resources) {
		return resources.getColor(color);
	}
	
	public int getColor() {
		return color;
	}
	
	@Override
	public String toString() {
		return message;
	}
}