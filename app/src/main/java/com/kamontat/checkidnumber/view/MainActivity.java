package com.kamontat.checkidnumber.view;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.kamontat.checkidnumber.R;
import com.kamontat.checkidnumber.adapter.ViewPagerAdapter;
import com.kamontat.checkidnumber.api.constants.Status;
import com.kamontat.checkidnumber.api.getter.About;
import com.kamontat.checkidnumber.api.getter.Export;
import com.kamontat.checkidnumber.api.getter.NonExport;
import com.kamontat.checkidnumber.model.IDNumber;
import com.kamontat.checkidnumber.model.Pool;
import com.kamontat.checkidnumber.presenter.MainPresenter;
import com.kamontat.checkidnumber.view.fragment.InputFragment;

import java.util.*;

public class MainActivity extends AppCompatActivity implements MainView {
	public static boolean EXPORT_FEATURE = true;
	public static final int PERMISSION_CODE = 1;
	private MainPresenter presenter;
	private String header;
	
	private BottomNavigationView navigation;
	private ViewPager viewPager;
	
	private InputFragment inputFragment;
	private ListFragment listFragment;
	
	private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
		
		@Override
		public boolean onNavigationItemSelected(@NonNull MenuItem item) {
			switch (item.getItemId()) {
				case R.id.navigation_insert:
					viewPager.setCurrentItem(0);
					item.setChecked(true);
					showKeyBoard();
					return true;
				case R.id.navigation_list:
					viewPager.setCurrentItem(1);
					item.setChecked(true);
					hideKeyBoard();
					return true;
			}
			return false;
		}
	};
	
	private final ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		}
		
		@Override
		public void onPageSelected(int position) {
			for (int i = 0; i < navigation.getMenu().size(); i++)
				navigation.getMenu().getItem(i).setChecked(false);
			navigation.getMenu().getItem(position).setChecked(true);
			if (navigation.getMenu().getItem(position).getItemId() == R.id.navigation_insert) showKeyBoard();
			else if (navigation.getMenu().getItem(position).getItemId() == R.id.navigation_list) hideKeyBoard();
		}
		
		@Override
		public void onPageScrollStateChanged(int state) {
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		presenter = new MainPresenter(this, new Pool(this));
		header = getResources().getString(R.string.input_message);
		
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		viewPager.addOnPageChangeListener(mOnPageChangeListener);
		
		navigation = (BottomNavigationView) findViewById(R.id.navigation);
		navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
		
		setViewPaperAdapter();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.top_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.top_menu_export:
				int re = requestPermission();
				if (re == 0) new Export(presenter).show();
				else if (re == 2) new NonExport(this).show();
				break;
			case R.id.top_menu_about:
				new About(this).show();
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		toggleExportFeatureMenu(menu);
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case PERMISSION_CODE:
				toggleExportFeature();
				if (requestPermission() == 2) new NonExport(this).show();
				else new Export(presenter).show();
				break;
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}
	
	private void setViewPaperAdapter() {
		inputFragment = new InputFragment();
		inputFragment.setButton(getInputOnClick());
		inputFragment.setInputListener(getInputTextChange());
		
		listFragment = new ListFragment();
		listFragment.setListAdapter(presenter.getPool());
		
		ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
		adapter.addFragment(inputFragment);
		adapter.addFragment(listFragment);
		viewPager.setAdapter(adapter);
	}
	
	private TextWatcher getInputTextChange() {
		return new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}
			
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}
			
			@Override
			public void afterTextChanged(Editable editable) {
				updateInput();
			}
		};
	}
	
	private View.OnClickListener getInputOnClick() {
		return new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				checkAndInsert();
			}
		};
	}
	
	private void updateInput() {
		updateInput(new IDNumber(getInputText()));
	}
	
	@Override
	public String getInputText() {
		return inputFragment.getInput();
	}
	
	@Override
	public void checkAndInsert() {
		IDNumber id = new IDNumber(getInputText());
		if (id.getStatus() == Status.OK) {
			presenter.addID(id);
			inputFragment.clearText();
		} else updateInput(id);
	}
	
	@Override
	public void updateInput(IDNumber id) {
		inputFragment.setButtonEnable(id.getStatus() == Status.OK);
		inputFragment.updateStatus(id.getStatus(), getResources());
	}
	
	@Override
	public void hideKeyBoard() {
		inputFragment.hideKeyBoard(this);
	}
	
	@Override
	public void showKeyBoard() {
		inputFragment.showKeyboard(this);
	}
	
	@Override
	public boolean isStorageWritable() {
		return isExternalStorageWritable();
	}
	
	/**
	 * Checks if external storage is available for read and write
	 */
	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state);
	}
	
	/**
	 * Checks if external storage is available to at least read
	 */
	public boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
	}
	
	@Override
	public boolean checkPermission() {
		int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
		return permissionCheck == PackageManager.PERMISSION_GRANTED;
	}
	
	@Override
	public int requestPermission() {
		// Here, thisActivity is the current activity
		if (!checkPermission()) {
			// Should we show an explanation?
			if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
				return 2;
			} else {
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE);
				return 1;
			}
		}
		return 0;
	}
	
	@Override
	public IDNumber[] getIDNumbers() {
		return presenter.getIDNumbers();
	}
	
	@Override
	public Context getContext() {
		return this;
	}
	
	public ViewPager getViewPager() {
		return viewPager;
	}
	
	public void toggleExportFeatureMenu(Menu menu) {
		String title = String.format(Locale.ENGLISH, "%s %d id (%s)", getResources().getString(R.string.export), presenter.getPool().getCount(), getResources().getString(R.string.xls));
		menu.findItem(R.id.top_menu_export).setVisible(EXPORT_FEATURE);
		menu.findItem(R.id.top_menu_export).setTitle(title);
		menu.findItem(R.id.top_menu_export).setEnabled(presenter.getPool().getCount() > 0);
	}
	
	public void toggleExportFeature() {
		EXPORT_FEATURE = checkPermission();
	}
}
