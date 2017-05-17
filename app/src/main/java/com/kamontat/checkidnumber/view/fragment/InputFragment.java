package com.kamontat.checkidnumber.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
	
	public InputFragment() {
		// Required empty public constructor
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_input, container, false);
		
		message = (TextView) view.findViewById(R.id.input_message);
		input = (EditText) view.findViewById(R.id.input_id_number);
		button = (Button) view.findViewById(R.id.input_btn);
		
		return view;
	}
}
