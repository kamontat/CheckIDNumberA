package com.kamontat.checkidnumber.model.file;

import com.kamontat.checkidnumber.MainJunitTest;
import com.kamontat.checkidnumber.model.IDNumber;
import com.kamontat.checkidnumber.model.strategy.worksheet.DefaultWorksheetFormat;
import com.kamontat.checkidnumber.model.strategy.worksheet.WorksheetFormat;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Suite;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

/**
 * @author kamontat
 * @version 1.0
 * @since Sat 20/May/2017 - 1:04 PM
 */
@RunWith(Suite.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Suite.SuiteClasses({ExcelModelTest.ExcelModelWithObserverTest.class, ExcelModelTest.ExcelModelWithNormalTest.class})
public class ExcelModelTest {
	private static final String FILE_NAME = "test.xls";
	private static final String SHEET_NAME = "test";
	
	private static final WorksheetFormat<IDNumber, DefaultWorksheetFormat.PositionValue> format = new DefaultWorksheetFormat();
	protected ExcelModel model;
	private File file = new File(FILE_NAME);
	
	@Before
	public void setUp() throws Exception {
		model = new ExcelModel(null);
	}
	
	// use force close, because close using asyncTask that not usable in junit test
	// see excel model test in android test
	@Test
	public void createFile() throws Exception {
		boolean b = model.setFile(file).createSheet(SHEET_NAME).forceClose();
		assertTrue(b);
		assertTrue(file.exists());
		clean();
	}
	
	@Test
	public void emptySheetTesting() throws Exception {
		checkClose(createEmptySheet());
		checkFile();
		checkSheetName();
		clean();
	}
	
	@Test
	public void addIDNumber() throws Exception {
		IDNumber number = new IDNumber(MainJunitTest.VALIDATE_ID);
		checkClose(createEmptySheet().add(format, number, 0));
		checkFile();
		checkSheet(number, 0);
		clean();
	}
	
	@Test
	public void addIDNumberInPosition() throws Exception {
		IDNumber number = new IDNumber(MainJunitTest.VALIDATE_ID);
		int pos = 32;
		
		checkClose(createEmptySheet().add(format, number, pos));
		checkFile();
		checkSheetName();
		checkSheet(number, pos);
		clean();
	}
	
	@Test
	public void addAllIDNumber() throws Exception {
		IDNumber v_i = new IDNumber(MainJunitTest.VALIDATE_ID);
		IDNumber i_i_n_u = new IDNumber(MainJunitTest.INVALIDATE_ID_NOT_NINE_UPPER_BOUND);
		IDNumber i_i_u = new IDNumber(MainJunitTest.INVALIDATE_ID_UNCORRECTED);
		IDNumber i_i_l_u = new IDNumber(MainJunitTest.INVALIDATE_ID_UNMATCHED_LENGTH_UPPER_BOUND);
		
		IDNumber[] ids = new IDNumber[]{v_i, i_i_n_u, i_i_u, i_i_l_u};
		
		checkClose(createEmptySheet().addAll(format, ids));
		checkFile();
		checkSheet(ids);
		clean();
	}
	
	public ExcelModel.ExcelProcess.SheetProcess createEmptySheet() {
		return model.setFile(file).createSheet(SHEET_NAME);
	}
	
	public void checkClose(ExcelModel.ExcelProcess.SheetProcess p) {
		assertTrue(p.forceClose());
	}
	
	public void checkFile() {
		assertTrue(file.exists());
	}
	
	public void checkSheetName() throws IOException, BiffException {
		Workbook workbook = Workbook.getWorkbook(file);
		assertTrue(workbook.getSheets().length > 0);
		for (String name : workbook.getSheetNames()) {
			if (name.equals(SHEET_NAME)) {
				workbook.close();
				return;
			}
		}
		workbook.close();
		fail();
	}
	
	public void checkSheet(IDNumber id, int at) throws IOException, BiffException {
		Workbook workbook = Workbook.getWorkbook(file);
		Sheet sheet = workbook.getSheet(SHEET_NAME);
		Cell cellID = sheet.getCell(0, at); // column, row
		Cell cellName = sheet.getCell(1, at); // column, row
		long realID = Long.parseLong(cellID.getContents());
		
		assertEquals(at + 1, realID);
		assertEquals(id.getId(), cellName.getContents());
	}
	
	public void checkSheet(IDNumber[] ids) throws IOException, BiffException {
		checkSheetName();
		int i = 0;
		for (IDNumber id : ids) {
			checkSheet(id, i++);
		}
	}
	
	class MyObserver implements Observer {
		@Override
		public void update(Observable observable, Object o) {
			if (o == null) {
				assertTrue(true);
				// System.out.println("log: update on " + getClass().getName());
			} else if (o.getClass().equals(Boolean.class)) {
				assertTrue(Boolean.class.cast(o));
			} else if (o.getClass().equals(ExcelModel.class)) {
				assertEquals("", ExcelModel.class.cast(o).getStringException());
			}
		}
	}
	
	public void clean() {
		file.delete();
	}
	
	@RunWith(MockitoJUnitRunner.class)
	public static class ExcelModelWithObserverTest extends ExcelModelTest {
		@Before
		public void setUp() throws Exception {
			super.setUp();
			model.addObservers(new MyObserver());
		}
	}
	
	@RunWith(MockitoJUnitRunner.class)
	public static class ExcelModelWithNormalTest extends ExcelModelTest {
	}
}