package com.kamontat.checkidnumber;

import android.content.res.Resources;
import android.support.test.rule.ActivityTestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author kamontat
 * @version 1.0
 * @since Thu 18/May/2017 - 1:07 AM
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({InputInstrumentedTest.class, PageInstrumentedTest.class})
public class MainInstrumentedTest {
	static final String SIMPLE_INT = "1234";
	static final String SIMPLE_STRING = "asdf";
	static final String EMPTY_STRING = "";
	
	static final String VALIDATE_ID = "1100600361966";
	static final String INVALIDATE_ID_UNCORRECTED = "1234872345876";
	static final String INVALIDATE_ID_NOT_NINE = "9239807132412";
	static final String INVALIDATE_ID_UNMATCHED_LENGTH = "55";
	
	private static final long SLEEP_CONSTANT = 5000L; // 5 second
	
	public static void debug() throws Exception {
		Thread.sleep(SLEEP_CONSTANT);
	}
	
	public static Resources getResources(ActivityTestRule rule) {
		return rule.getActivity().getResources();
	}
}