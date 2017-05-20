package com.kamontat.checkidnumber;

import com.kamontat.checkidnumber.model.IDNumberTest;
import com.kamontat.checkidnumber.model.ThaiIDNumberTest;
import com.kamontat.checkidnumber.model.file.ExcelModelTest;
import com.kamontat.checkidnumber.model.strategy.ThailandIDNumberStrategyTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({IDNumberTest.class, ThaiIDNumberTest.class, ThailandIDNumberStrategyTest.class, ExcelModelTest.class})
public class MainJunitTest {
	public static final String SIMPLE_INT = "1234";
	public static final String SIMPLE_STRING = "asdf";
	public static final String EMPTY_STRING = "";
	
	public static final String VALIDATE_ID = "1100600361966";
	public static final String INVALIDATE_ID_UNCORRECTED = "1234872345876";
	public static final String INVALIDATE_ID_NOT_NINE_LOWER_BOUND = "923";
	public static final String INVALIDATE_ID_NOT_NINE_NORMAL_BOUND = "9876543211234";
	public static final String INVALIDATE_ID_NOT_NINE_UPPER_BOUND = "987654321123456789";
	public static final String INVALIDATE_ID_UNMATCHED_LENGTH_UPPER_BOUND = "55";
	public static final String INVALIDATE_ID_UNMATCHED_LENGTH_LOWER_BOUND = "558712394791732497";
}