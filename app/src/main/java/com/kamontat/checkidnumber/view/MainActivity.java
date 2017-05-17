package com.kamontat.checkidnumber.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.kamontat.checkidnumber.R;
import com.kamontat.checkidnumber.adapter.ViewPagerAdapter;
import com.kamontat.checkidnumber.model.Pool;
import com.kamontat.checkidnumber.view.fragment.InputFragment;

public class MainActivity extends AppCompatActivity implements MainView {
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
					// Log.d("NEXT PAGE", "Input Page");
					return true;
				case R.id.navigation_show:
					viewPager.setCurrentItem(1);
					item.setChecked(true);
					// Log.d("NEXT PAGE", "List Page");
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
		}
		
		@Override
		public void onPageScrollStateChanged(int state) {
			
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		header = getResources().getString(R.string.input_message);
		
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		viewPager.addOnPageChangeListener(mOnPageChangeListener);
		
		navigation = (BottomNavigationView) findViewById(R.id.navigation);
		navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
		
		setViewPaperAdapter();
	}
	
	private void setViewPaperAdapter() {
		ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
		inputFragment = new InputFragment();
		listFragment = new ListFragment();
		listFragment.setListAdapter(new Pool(this)); // mockup
		
		adapter.addFragment(inputFragment);
		adapter.addFragment(listFragment);
		viewPager.setAdapter(adapter);
	}
	
	@Override
	public void setPool(Pool p) {
		
	}
}
