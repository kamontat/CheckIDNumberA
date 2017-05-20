package com.kamontat.checkidnumber.api.getter;

import android.support.annotation.NonNull;
import android.text.InputType;
import com.afollestad.materialdialogs.MaterialDialog;
import com.kamontat.checkidnumber.R;
import com.kamontat.checkidnumber.model.file.ExcelModel;
import com.kamontat.checkidnumber.model.strategy.worksheet.DefaultWorksheetFormat;
import com.kamontat.checkidnumber.presenter.MainPresenter;
import com.kamontat.checkidnumber.raw.Showable;

/**
 * @author kamontat
 * @version 1.0
 * @since Sat 20/May/2017 - 3:37 PM
 */
public class Export implements Showable {
	private MainPresenter root;
	
	public Export(MainPresenter root) {
		this.root = root;
	}
	
	private MaterialDialog.Builder setting() {
		return new MaterialDialog.Builder(root.getContext()).title(getTitle()).content(getContent()).inputType(getInputType()).inputRange(getMinLength(), getMaxLength()).input(R.string.input_file_name_hint, R.string.empty_string, false, getCallBack()).checkBoxPromptRes(R.string.delete_message, false, null).positiveText(R.string.save_message).negativeText(R.string.cancel_message);
	}
	
	private String getTitle() {
		return root.getContext().getResources().getString(R.string.export_title);
	}
	
	private String getContent() {
		return root.getContext().getResources().getString(R.string.export_content);
	}
	
	private int getInputType() {
		return InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;
	}
	
	private int getMinLength() {
		return 1;
	}
	
	private int getMaxLength() {
		return 50;
	}
	
	private MaterialDialog.InputCallback getCallBack() {
		return new MaterialDialog.InputCallback() {
			@Override
			public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
				ExcelModel m = new ExcelModel(root).setAutoSize(true);
				if (dialog.isPromptCheckBoxChecked()) m = m.setAutoClear(true);
				m.setFileName(input.toString()).createSheet(root.getContext().getResources().getString(R.string.default_sheet_name)).addAll(new DefaultWorksheetFormat(), root.getIDNumbers()).close();
			}
		};
	}
	
	@Override
	public void show() {
		setting().canceledOnTouchOutside(true).show();
	}
}
