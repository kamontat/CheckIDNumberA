package com.kamontat.checkidnumber.api.getter;

import android.Manifest;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.kamontat.checkidnumber.R;
import com.kamontat.checkidnumber.raw.Showable;

import static com.kamontat.checkidnumber.view.MainActivity.PERMISSION_CODE;

/**
 * @author kamontat
 * @version 1.0
 * @since Sun 21/May/2017 - 4:17 PM
 */
public class NonExport implements Showable {
	private Activity root;
	
	public NonExport(Activity root) {
		this.root = root;
	}
	
	private MaterialDialog.Builder setting() {
		return new MaterialDialog.Builder(root).title(getTitle()).content(getContent()).positiveText(R.string.yes).negativeText(R.string.no).onPositive(getRequestCallBack());
	}
	
	private String getTitle() {
		return root.getResources().getString(R.string.no_permission);
	}
	
	private String getContent() {
		return root.getResources().getString(R.string.ask_for_sure);
	}
	
	private MaterialDialog.SingleButtonCallback getRequestCallBack() {
		return new MaterialDialog.SingleButtonCallback() {
			@Override
			public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
				ActivityCompat.requestPermissions(root, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE);
			}
		};
	}
	
	@Override
	public void show() {
		setting().canceledOnTouchOutside(true).show();
	}
}
