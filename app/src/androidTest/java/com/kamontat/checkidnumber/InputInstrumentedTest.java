package com.kamontat.checkidnumber;

import android.content.Context;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.kamontat.checkidnumber.api.constants.Status;
import com.kamontat.checkidnumber.view.MainActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static com.kamontat.checkidnumber.MainInstrumentedTest.*;
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
	public final ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);
	
	@Test
	public void useAppContext() throws Exception {
		// Context of the app under swipeToChangePage.
		Context appContext = InstrumentationRegistry.getTargetContext();
		assertEquals(APP_PACKAGE, appContext.getPackageName());
	}
	
	@Test
	public void validateInputText() throws RemoteException {
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
		onView(withId(R.id.fragment_status_message)).check(matches(withText(Status.UNCORRECTED.toString())));
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
		
		onView(withId(R.id.fragment_status_message)).check(matches(withText(Status.NOT_NINE.toString())));
	}
	
	@Test
	public void checkColorOfInvalidateID_Uncorrected() throws Exception {
		onView(withId(R.id.fragment_input_id_number)).perform(typeText(INVALIDATE_ID_UNCORRECTED));
		onView(withId(R.id.fragment_input_id_number)).check(matches(withCurrentTextColor(Status.UNCORRECTED.getColor(getResources(activityTestRule)))));
		
		onView(withId(R.id.fragment_status_message)).check(matches(withText(Status.UNCORRECTED.toString())));
	}
	
	@Test
	public void checkColorOfInvalidateID_UnmatchedLength() throws Exception {
		onView(withId(R.id.fragment_input_id_number)).perform(typeText(INVALIDATE_ID_UNMATCHED_LENGTH));
		onView(withId(R.id.fragment_input_id_number)).check(matches(withCurrentTextColor(Status.UNMATCHED_LENGTH.getColor(getResources(activityTestRule)))));
		
		onView(withId(R.id.fragment_status_message)).check(matches(withText(Status.UNMATCHED_LENGTH.toString())));
	}
	
	@Test
	public void clearWithInput() throws Exception {
		MainInstrumentedTest.addID(VALIDATE_ID);
		onView(withId(R.id.fragment_input_id_number)).perform(typeText(SIMPLE_STRING)).check(matches(withText(EMPTY_STRING)));
	}
	
	@Test
	public void inputAndSee() throws Exception {
		MainInstrumentedTest.addID(VALIDATE_ID);
		toListPage(activityTestRule);
		onView(withText(VALIDATE_ID)).check(matches(isDisplayed()));
	}
	
	@Test
	public void wrongInputAndNotSee() throws Exception {
		MainInstrumentedTest.addID(INVALIDATE_ID_NOT_NINE);
		
		toListPage(activityTestRule);
		onView(withText(VALIDATE_ID)).check(doesNotExist());
		
		toMainPage(activityTestRule);
		onView(withId(R.id.fragment_input_id_number)).check(matches(withText(INVALIDATE_ID_NOT_NINE)));
	}
	
	
	@Test
	public void multiplyInputAndSee() throws Exception {
		multipleAddID();
		
		toListPage(activityTestRule);
		
		onView(withText(VALIDATE_ID)).check(matches(isDisplayed()));
		onView(withText(VALIDATE_ID_1)).check(matches(isDisplayed()));
		onView(withText(VALIDATE_ID_2)).check(matches(isDisplayed()));
		onView(withText(VALIDATE_ID_3)).check(matches(isDisplayed()));
	}
}