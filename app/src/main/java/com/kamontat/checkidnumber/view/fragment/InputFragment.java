package com.kamontat.checkidnumber.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.kamontat.checkidnumber.R;

/**
 * @author kamontat
 * @version 1.0
 * @since Thu 11/May/2017 - 10:22 PM
 */
public class InputFragment extends Fragment {
	private TextView message;
	private EditText input;
	private Button button;
	
	private View.OnClickListener clickListener;
	private TextWatcher watcher;
	
	public InputFragment() {
		// Required empty public constructor
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_input, container, false);
		
		message = (TextView) view.findViewById(R.id.input_message);
		input = (EditText) view.findViewById(R.id.input_id_number);
		if (watcher != null) input.addTextChangedListener(watcher);
		button = (Button) view.findViewById(R.id.input_btn);
		if (clickListener != null) button.setOnClickListener(clickListener);
		
		return view;
	}
	
	public String getInput() {
		return input.getText().toString();
	}
	
	public void setTextColor(int id) {
		input.setTextColor(id);
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
		if (input.requestFocus()) {
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
		}
	}
	
	public void clearText() {
		input.setText("");
	}
}
