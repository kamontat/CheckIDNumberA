package com.kamontat.checkidnumber.api.getter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import com.afollestad.materialdialogs.MaterialDialog;
import com.kamontat.checkidnumber.BuildConfig;
import com.kamontat.checkidnumber.R;
import com.kamontat.checkidnumber.raw.Showable;

import java.util.*;

/**
 * @author kamontat
 * @version 1.0
 * @since Sat 20/May/2017 - 3:10 PM
 */
public class About implements Showable {
	private static final String VERSION = String.format(Locale.ENGLISH, "v%s-build: %s", BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE);
	private Activity root;
	
	public About(Activity root) {
		this.root = root;
	}
	
	private MaterialDialog.Builder setting() {
		return new MaterialDialog.Builder(root).title(getTitle()).content(getContent()).items(R.array.developer_name).itemsCallback(getCallBack()).positiveText(R.string.ok_message);
	}
	
	private String getTitle() {
		return String.format(Locale.ENGLISH, "%s", root.getResources().getString(R.string.about_title));
	}
	
	private String getContent() {
		return root.getResources().getString(R.string.app_description) + "\n\n" + VERSION + "\n\nDevelop by";
	}
	
	private MaterialDialog.ListCallback getCallBack() {
		return new MaterialDialog.ListCallback() {
			@Override
			public void onSelection(MaterialDialog dialog, android.view.View itemView, int which, CharSequence text) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(root.getResources().getString(R.string.developer_github)));
				root.startActivity(browserIntent);
			}
		};
	}
	
	@Override
	public void show() {
		setting().canceledOnTouchOutside(true).show();
	}
}
