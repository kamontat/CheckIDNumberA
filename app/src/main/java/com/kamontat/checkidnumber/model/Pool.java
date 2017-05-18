package com.kamontat.checkidnumber.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.*;

/**
 * @author kamontat
 * @version 1.0
 * @since Thu 11/May/2017 - 9:20 PM
 */
public class Pool extends ArrayAdapter<IDNumber> {
	protected int size;
	protected ArrayList<IDNumber> idNumbers;
	
	public Pool(@NonNull Context context) {
		super(context, android.R.layout.two_line_list_item, android.R.id.text1);
		this.idNumbers = new ArrayList<>();
	}
	
	@Override
	public void add(@Nullable IDNumber object) {
		super.add(object);
		idNumbers.add(object);
	}
	
	@Override
	public void remove(@Nullable IDNumber object) {
		super.remove(object);
		idNumbers.remove(object);
	}
	
	@Override
	public void clear() {
		super.clear();
		idNumbers.clear();
	}
	
	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		if (convertView == null)
			convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.two_line_list_item, parent, false);
		
		TextView textView1 = (TextView) convertView.findViewById(android.R.id.text1);
		TextView textView2 = (TextView) convertView.findViewById(android.R.id.text2);
		
		IDNumber idNumber = getItem(position);
		if (idNumber == null) return super.getView(position, convertView, parent);
		
		textView1.setText(idNumber.getId());
		
		textView2.setText(String.format(Locale.ENGLISH, "Status: %s", idNumber.getStatus().toString()));
		textView2.setTextColor(idNumber.getStatus().getColor(getContext().getResources()));
		
		return super.getView(position, convertView, parent);
	}
	
	public IDNumber[] getIDNumbers() {
		return idNumbers.toArray(new IDNumber[idNumbers.size()]);
	}
}
