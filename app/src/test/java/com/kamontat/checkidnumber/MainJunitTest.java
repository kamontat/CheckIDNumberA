package com.kamontat.checkidnumber;

import com.kamontat.checkidnumber.model.IDNumberTest;
import com.kamontat.checkidnumber.model.ThaiIDNumberTest;
import com.kamontat.checkidnumber.model.strategy.ThailandIDNumberStrategyTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({IDNumberTest.class, ThaiIDNumberTest.class, ThailandIDNumberStrategyTest.class})
public class MainJunitTest {
}