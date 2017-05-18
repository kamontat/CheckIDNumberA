package com.kamontat.checkidnumber;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.EditText;
import com.kamontat.checkidnumber.api.constants.Status;
import com.kamontat.checkidnumber.view.MainActivity;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.core.deps.guava.base.Preconditions.checkNotNull;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static com.kamontat.checkidnumber.MainInstrumentedTest.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

/**
 * Instrumentation swipeToChangePage, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InputInstrumentedTest {
	
	// Preferred JUnit 4 mechanism of specifying the activity to be launched before each swipeToChangePage
	@Rule
	public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);
	
	@Test
	public void useAppContext() throws Exception {
		// Context of the app under swipeToChangePage.
		Context appContext = InstrumentationRegistry.getTargetContext();
		assertEquals("com.kamontat.checkidnumber", appContext.getPackageName());
	}
	
	@Test
	public void validateInputText() {
		onView(withId(R.id.fragment_input_id_number)).perform(typeText(SIMPLE_INT)).check(matches(withText(SIMPLE_INT)));
	}
	
	@Test
	public void invalidateInputText() {
		onView(withId(R.id.fragment_input_id_number)).perform(typeText(SIMPLE_STRING)).check(matches(withText(EMPTY_STRING)));
	}
	
	@Test
	public void validateID() {
		onView(withId(R.id.fragment_input_id_number)).perform(typeText(VALIDATE_ID)).check(matches(withText(VALIDATE_ID)));
		onView(withId(R.id.fragment_btn)).check(matches(isEnabled()));
	}
	
	@Test
	public void invalidateID() {
		onView(withId(R.id.fragment_input_id_number)).perform(typeText(INVALIDATE_ID_UNCORRECTED)).check(matches(withText(INVALIDATE_ID_UNCORRECTED)));
		onView(withId(R.id.fragment_btn)).check(matches(not(isEnabled())));
	}
	
	@Test
	public void checkColorOfValidateID() throws Exception {
		onView(withId(R.id.fragment_input_id_number)).perform(typeText(VALIDATE_ID));
		onView(withId(R.id.fragment_input_id_number)).check(matches(withCurrentTextColor(Status.OK.getColor(getResources(activityTestRule)))));
	}
	
	@Test
	public void checkColorOfInvalidateID_NotNine() throws Exception {
		onView(withId(R.id.fragment_input_id_number)).perform(typeText(INVALIDATE_ID_NOT_NINE));
		onView(withId(R.id.fragment_input_id_number)).check(matches(withCurrentTextColor(Status.NOT_NINE.getColor(getResources(activityTestRule)))));
	}
	
	@Test
	public void checkColorOfInvalidateID_Uncorrected() throws Exception {
		onView(withId(R.id.fragment_input_id_number)).perform(typeText(INVALIDATE_ID_UNCORRECTED));
		onView(withId(R.id.fragment_input_id_number)).check(matches(withCurrentTextColor(Status.UNCORRECTED.getColor(getResources(activityTestRule)))));
	}
	
	@Test
	public void checkColorOfInvalidateID_UnmatchedLength() throws Exception {
		onView(withId(R.id.fragment_input_id_number)).perform(typeText(INVALIDATE_ID_UNMATCHED_LENGTH));
		onView(withId(R.id.fragment_input_id_number)).check(matches(withCurrentTextColor(Status.UNMATCHED_LENGTH.getColor(getResources(activityTestRule)))));
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
}