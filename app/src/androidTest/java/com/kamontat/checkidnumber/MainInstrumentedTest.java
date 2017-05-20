package com.kamontat.checkidnumber;

import android.content.res.Resources;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import com.kamontat.checkidnumber.model.IDNumber;
import com.kamontat.checkidnumber.view.MainActivity;
import com.kamontat.checkidnumber.view.fragment.InputFragment;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.core.deps.guava.base.Preconditions.checkNotNull;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

/**
 * @author kamontat
 * @version 1.0
 * @since Thu 18/May/2017 - 1:07 AM
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({InputInstrumentedTest.class, PageInstrumentedTest.class, ExportFeatureInstrumentTest.class})
public class MainInstrumentedTest {
	static final String SIMPLE_INT = "1234";
	static final String SIMPLE_STRING = "asdf";
	static final String EMPTY_STRING = "";
	
	static final String VALIDATE_ID = "1100600361966";
	static final String VALIDATE_ID_1 = "1231231231234";
	static final String VALIDATE_ID_2 = "2344123423420";
	static final String VALIDATE_ID_3 = "1238989482921";
	
	static final String INVALIDATE_ID_UNCORRECTED = "1234872345876";
	static final String INVALIDATE_ID_NOT_NINE = "9239807132412";
	static final String INVALIDATE_ID_UNMATCHED_LENGTH = "55";
	
	private static final long SLEEP_CONSTANT = 5000L; // 5 second
	
	/**
	 * wait for 5 second
	 *
	 * @throws Exception
	 * 		thread error
	 */
	public static void debug() throws Exception {
		Thread.sleep(SLEEP_CONSTANT);
	}
	
	public static void wait(double second) throws InterruptedException {
		Thread.sleep(Math.round(second * 1000));
	}
	
	public static Resources getResources(ActivityTestRule rule) {
		return rule.getActivity().getResources();
	}
	
	public static void addID(IDNumber id) {
		onView(withId(R.id.fragment_input_id_number)).perform(typeText(id.getId()));
		onView(withId(R.id.fragment_btn)).perform(click());
	}
	
	public static void addID(String id) {
		onView(withId(R.id.fragment_input_id_number)).perform(typeText(id));
		onView(withId(R.id.fragment_btn)).perform(click());
	}
	
	public static void toMainPage(ActivityTestRule<MainActivity> rule) {
		if (getCurrentFragment(rule).getClass().equals(InputFragment.class)) return;
		onView(withId(R.id.navigation_insert)).perform(click());
		assertTrue(getCurrentFragment(rule).getClass().equals(InputFragment.class));
	}
	
	public static void toListPage(ActivityTestRule<MainActivity> rule) {
		if (getCurrentFragment(rule).getClass().equals(ListFragment.class)) return;
		onView(withId(R.id.navigation_list)).perform(click());
		assertTrue(getCurrentFragment(rule).getClass().equals(ListFragment.class));
	}
	
	public static Fragment getCurrentFragment(ActivityTestRule<MainActivity> rule) {
		ViewPager p = rule.getActivity().getViewPager();
		return ((FragmentStatePagerAdapter) p.getAdapter()).getItem(p.getCurrentItem());
	}
	
	
	/**
	 * Returns a matcher that matches {@link android.widget.EditText}s based on text property value. Note: View's
	 * text property is never null. If you setText(null) it will still be "". Do not use null
	 * matcher.
	 *
	 * @param integerMatcher
	 * 		{@link Matcher} of {@link String} with text to match
	 */
	public static Matcher<View> withCurrentTextColor(final Matcher<Integer> integerMatcher) {
		checkNotNull(integerMatcher);
		return new BoundedMatcher<View, EditText>(EditText.class) {
			@Override
			public void describeTo(Description description) {
				description.appendText("with text color: ");
				integerMatcher.describeTo(description);
			}
			
			@Override
			public boolean matchesSafely(EditText editText) {
				return integerMatcher.matches(editText.getCurrentTextColor());
			}
		};
	}
	
	/**
	 * Returns a matcher that matches {@link EditText} based on it's text property value. Note:
	 * View's Sugar for withTextColor(is("string")).
	 */
	public static Matcher<View> withCurrentTextColor(int color) {
		return withCurrentTextColor(is(color));
	}
	
	public static void multipleAddID() {
		for (String id : getStringIDNumbers()) {
			MainInstrumentedTest.addID(id);
		}
	}
	
	/**
	 * for multipleAddID and It's test only
	 * {@link #multipleAddID()}
	 *
	 * @return array of test {@code string} id
	 */
	public static String[] getStringIDNumbers() {
		return new String[]{VALIDATE_ID, VALIDATE_ID_1, VALIDATE_ID_2, VALIDATE_ID_3};
	}
	
	/**
	 * for multipleAddID and It's test only
	 * {@link #multipleAddID()}
	 *
	 * @return array of test {@code IDNumber}
	 */
	public static IDNumber[] getIDNumbers() {
		return new IDNumber[]{new IDNumber(VALIDATE_ID), new IDNumber(VALIDATE_ID_1), new IDNumber(VALIDATE_ID_2), new IDNumber(VALIDATE_ID_3)};
	}
}