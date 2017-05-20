package com.kamontat.checkidnumber;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.ListFragment;
import com.kamontat.checkidnumber.view.MainActivity;
import com.kamontat.checkidnumber.view.fragment.InputFragment;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static com.kamontat.checkidnumber.MainInstrumentedTest.getCurrentFragment;
import static com.kamontat.checkidnumber.MainInstrumentedTest.getResources;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertTrue;

/**
 * @author kamontat
 * @version 1.0
 * @since Thu 18/May/2017 - 2:35 AM
 */
@RunWith(AndroidJUnit4.class)
public class PageInstrumentedTest {
	@Rule
	public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);
	
	@Test
	public void swipeToChangePage() throws Exception {
		onView(withId(R.id.container)).perform(ViewActions.swipeLeft());
		assertTrue(getCurrentFragment(activityTestRule).getClass().equals(ListFragment.class));
		
		onView(withId(R.id.container)).perform(ViewActions.swipeRight());
		assertTrue(getCurrentFragment(activityTestRule).getClass().equals(InputFragment.class));
	}
	
	@Test
	public void clickToChangePage() throws Exception {
		onView(withId(R.id.navigation_list)).perform(click());
		assertTrue(getCurrentFragment(activityTestRule).getClass().equals(ListFragment.class));
		
		onView(withId(R.id.navigation_insert)).perform(click());
		assertTrue(getCurrentFragment(activityTestRule).getClass().equals(InputFragment.class));
	}
	
	@Test
	public void checkAboutPopup() throws Exception {
		openActionBarOverflowOrOptionsMenu(getContext());
		onView(withText(getResources(activityTestRule).getString(R.string.about))).perform(click());
		onView(withText(Matchers.startsWith(getResources(activityTestRule).getString(R.string.about_title)))).check(matches(isDisplayed()));
	}
	
	@Test
	public void checkExportDisable() throws Exception {
		openActionBarOverflowOrOptionsMenu(getContext());
		onView(withText(Matchers.startsWith(getResources(activityTestRule).getString(R.string.export)))).check(matches(not(isClickable())));
	}
}
