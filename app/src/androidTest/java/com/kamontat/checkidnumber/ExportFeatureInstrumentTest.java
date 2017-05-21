package com.kamontat.checkidnumber;

import android.os.Environment;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.AdapterView;
import com.kamontat.checkidnumber.model.IDNumber;
import com.kamontat.checkidnumber.model.Pool;
import com.kamontat.checkidnumber.view.MainActivity;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static com.kamontat.checkidnumber.MainInstrumentedTest.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * @author kamontat
 * @version 1.0
 * @since Sat 20/May/2017 - 10:57 PM
 */
public class ExportFeatureInstrumentTest {
	private static final String FILE_NAME = "test";
	private static final File FILE_LOCATION = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), FILE_NAME + ".xls");
	
	@Rule
	public final ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);
	
	@Test
	public void exportSize() throws Exception {
		multipleAddID();
		int actualSize = activityTestRule.getActivity().getIDNumbers().length;
		openActionBarOverflowOrOptionsMenu(activityTestRule.getActivity());
		onView(withText(Matchers.containsString(actualSize + " id"))).check(matches(isDisplayed()));
	}
	
	@Test
	public void try_clickExportShouldShowDialog() throws Exception {
		multipleAddID();
		
		int actualSize = activityTestRule.getActivity().getIDNumbers().length;
		openActionBarOverflowOrOptionsMenu(activityTestRule.getActivity());
		onView(withText(Matchers.containsString(actualSize + " id"))).perform(click());
		
		onView(withText(R.string.export_title)).check(matches(isDisplayed()));
	}
	
	@Test
	public void try_clickAndCheckDeleteOnSave() throws Exception {
		try_clickExportShouldShowDialog();
		ViewInteraction deleteCheckBox = onView(withId(com.afollestad.materialdialogs.R.id.md_promptCheckbox));
		checkCheckBoxIs(deleteCheckBox, false);
		MainInstrumentedTest.wait(0.5);
		deleteCheckBox.perform(click());
		checkCheckBoxIs(deleteCheckBox, true);
	}
	
	@Test
	public void try_enterExportFileName() throws Exception {
		try_clickExportShouldShowDialog();
		enterName();
	}
	
	@Test
	public void try_enterExportFileNameWithDeleteOption() throws Exception {
		try_clickAndCheckDeleteOnSave();
		enterName();
	}
	
	private void enterName() {
		// check save btn, should disable
		checkSaveBtn(false);
		// insert file name
		ViewInteraction editText = onView(withId(16908297)); // can't get id, so I get raw id
		editText.perform(typeText(FILE_NAME));
		editText.check(matches(withText(FILE_NAME)));
		// check save btn, should enable now
		checkSaveBtn(true);
		clickSave();
	}
	
	@Test
	public void real_clickExport() throws Exception {
		// enter name
		try_enterExportFileName();
		real_export(true);
	}
	
	@Test
	public void real_clickExportWithDelete() throws Exception {
		// enter name
		try_enterExportFileNameWithDeleteOption();
		real_export(false);
	}
	
	private void real_export(boolean dateMustExist) throws InterruptedException, IOException, BiffException {
		// click ok
		MainInstrumentedTest.wait(1.0); // wait create file 1 second
		// check output
		onView(withId(com.afollestad.materialdialogs.R.id.md_title)).check(matches(withText(R.string.success)));
		onView(withId(com.afollestad.materialdialogs.R.id.md_content)).check(matches(withText(containsString(FILE_NAME))));
		onView(withId(com.afollestad.materialdialogs.R.id.md_buttonDefaultNegative)).perform(click()); // dismiss output
		// check file
		checkExcelFile();
		// check id-number list
		checkIDNumberDataArray(dateMustExist);
	}
	
	private void checkExcelFile() throws IOException, BiffException {
		if (!FILE_LOCATION.exists()) fail("File Not Exist");
		Workbook workbook = Workbook.getWorkbook(FILE_LOCATION);
		
		Sheet s = checkSheetName(workbook);
		if (s == null) fail("No Sheet Name!");
		
		checkSheetData(s, activityTestRule.getActivity().getIDNumbers());
		
		workbook.close();
	}
	
	private Sheet checkSheetName(Workbook workbook) throws IOException, BiffException {
		assertTrue(workbook.getSheets().length > 0);
		for (String name : workbook.getSheetNames()) {
			if (name.equals(getResources(activityTestRule).getString(R.string.default_sheet_name))) {
				return workbook.getSheet(name);
			}
		}
		workbook.close();
		return null;
	}
	
	private void checkSheetData(Sheet sheet, IDNumber[] ids) throws IOException, BiffException {
		int i = 0;
		for (IDNumber id : ids) {
			Cell cellID = sheet.getCell(0, i); // column, row
			Cell cellName = sheet.getCell(1, i); // column, row
			long realID = Long.parseLong(cellID.getContents());
			
			assertEquals(++i, realID);
			assertEquals(id.getId(), cellName.getContents());
		}
	}
	
	
	private void checkSaveBtn(boolean check) {
		ViewInteraction okBtn = onView(withId(com.afollestad.materialdialogs.R.id.md_buttonDefaultPositive));
		if (check) okBtn.check(matches(isEnabled()));
		else okBtn.check(matches(not(isEnabled())));
	}
	
	private void clickSave() {
		onView(withId(com.afollestad.materialdialogs.R.id.md_buttonDefaultPositive)).perform(ViewActions.click());
	}
	
	private void checkCheckBoxIs(ViewInteraction checkBox, boolean check) {
		checkBox.check(matches(isDisplayed()));
		if (check) checkBox.check(matches(isChecked()));
		else checkBox.check(matches(not(isChecked())));
	}
	
	private void checkIDNumberDataArray(boolean mustExist) {
		toListPage(activityTestRule);
		for (IDNumber id : getIDNumbers())
			checkData(id, mustExist);
	}
	
	private void checkData(IDNumber id, boolean mustExist) {
		// toListPage(activityTestRule);
		if (mustExist) onData(allOf(is(instanceOf(IDNumber.class)), is(id))).check(matches(isDisplayed()));
		else onView(withId(android.R.id.list)).check(matches(not(withAdaptedData(getIDNumbers()[0]))));
	}
	
	@After
	public void tearDown() throws Exception {
		FILE_LOCATION.delete();
	}
	
	private static Matcher<View> withAdaptedData(final IDNumber id) {
		return new TypeSafeMatcher<View>() {
			@Override
			public void describeTo(Description description) {
				description.appendText("with class: ");
				description.appendValue(id);
			}
			
			@Override
			public boolean matchesSafely(View view) {
				if (!(view instanceof AdapterView)) {
					return false;
				}
				
				Pool adapter = (Pool) ((AdapterView) view).getAdapter();
				for (int i = 0; i < adapter.getCount(); i++) {
					if (id.equals(adapter.getItem(i))) {
						return true;
					}
				}
				return false;
			}
		};
	}
}
