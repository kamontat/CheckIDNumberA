package com.kamontat.checkidnumber.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.kamontat.checkidnumber.R;
import com.kamontat.checkidnumber.adapter.ViewPagerAdapter;
import com.kamontat.checkidnumber.api.constants.Status;
import com.kamontat.checkidnumber.model.IDNumber;
import com.kamontat.checkidnumber.model.Pool;
import com.kamontat.checkidnumber.presenter.MainPresenter;
import com.kamontat.checkidnumber.view.fragment.InputFragment;

public class MainActivity extends AppCompatActivity implements MainView {
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
				case R.id.navigation_show:
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
			else if (navigation.getMenu().getItem(position).getItemId() == R.id.navigation_show) hideKeyBoard();
		}
		
		@Override
		public void onPageScrollStateChanged(int state) {
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		presenter = new MainPresenter(new Pool(this));
		header = getResources().getString(R.string.input_message);
		
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		viewPager.addOnPageChangeListener(mOnPageChangeListener);
		
		navigation = (BottomNavigationView) findViewById(R.id.navigation);
		navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
		
		setViewPaperAdapter();
	}
	
	private void setViewPaperAdapter() {
		inputFragment = new InputFragment();
		inputFragment.setButton(getInputOnClick());
		inputFragment.setInputListener(getInputTextChange());
		
		listFragment = new ListFragment();
		listFragment.setListAdapter(presenter.getPool()); // mockup
		
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
		inputFragment.setTextColor(id.getStatus().getColor(getResources()));
		inputFragment.setButtonEnable(id.getStatus() == Status.OK);
	}
	
	@Override
	public void hideKeyBoard() {
		View v = getWindow().getCurrentFocus();
		if (v != null) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	}
	
	@Override
	public void showKeyBoard() {
		inputFragment.showKeyboard(this);
	}
}
