package com.kamontat.checkidnumber.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.kamontat.checkidnumber.R;
import com.kamontat.checkidnumber.api.constants.Status;

import java.util.*;

/**
 * @author kamontat
 * @version 1.0
 * @since Thu 11/May/2017 - 10:22 PM
 */
public class InputFragment extends Fragment {
	private TextView message;
	private TextView statusMessage;
	private EditText input;
	private Button button;
	
	private View.OnClickListener clickListener;
	private TextWatcher watcher;
	private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
		@Override
		public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
			if (i == EditorInfo.IME_ACTION_GO) {
				button.callOnClick();
				// hideKeyBoard(getActivity());
				return true;
			}
			return false;
		}
	};
	
	public InputFragment() {
		// Required empty public constructor
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_input, container, false);
		
		message = (TextView) view.findViewById(R.id.fragment_message);
		updateInputSize(0);
		statusMessage = (TextView) view.findViewById(R.id.fragment_status_message);
		input = (EditText) view.findViewById(R.id.fragment_input_id_number);
		if (watcher != null) input.addTextChangedListener(watcher);
		input.setOnEditorActionListener(editorActionListener);
		button = (Button) view.findViewById(R.id.fragment_btn);
		if (clickListener != null) button.setOnClickListener(clickListener);
		updateStatus(Status.NOT_CREATE, view.getResources());
		return view;
	}
	
	public String getInput() {
		return input.getText().toString();
	}
	
	public void updateStatus(Status status, Resources r) {
		statusMessage.setText(status.toString());
		statusMessage.setTextColor(status.getColor(r));
		input.setTextColor(status.getColor(r));
	}
	
	public void updateInputSize(int size) {
		String message = String.format(Locale.ENGLISH, "%s (%d)", getResources().getString(R.string.input_message), size);
		this.message.setText(message);
	}
	
	public void setInputListener(TextWatcher watcher) {
		this.watcher = watcher;
	}
	
	public void setButton(View.OnClickListener clickListener) {
		this.clickListener = clickListener;
	}
	
	public void setButtonEnable(boolean b) {
		button.setEnabled(b);
	}
	
	public void showKeyboard(Activity activity) {
		if (input != null && input.requestFocus()) {
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
		}
	}
	
	public void hideKeyBoard(Activity activity) {
		View v = activity.getWindow().getCurrentFocus();
		if (v != null) {
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	}
	
	public void clearText() {
		input.setText("");
	}
}
