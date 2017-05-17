package com.kamontat.checkidnumber.model.file;

import android.util.Log;
import com.kamontat.checkidnumber.model.IDNumber;
import com.kamontat.checkidnumber.presenter.MainPresenter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * This class use to create new excel file in <code>folderList</code> location <br>
 * It's have only one method call <b><code>createExcelFile</code></b> to create Excel by using <code>idList</code>
 *
 * @author kamontat
 * @version 1.0
 * @since 17/8/59 - 21:49
 */
public class ExcelModel {
	private MainPresenter presenter;
	
	public ExcelModel(MainPresenter presenter) {
		this.presenter = presenter;
	}
	
	/**
	 * create excel file by <code>idList</code>
	 */
	public Workbook createExcelFile() {
		if (!presenter.isStorageWritable()) {
			return null;
		}
		
		//Blank workbook
		Workbook workbook = new HSSFWorkbook();
		
		//Cell style for header row
		CellStyle cs = workbook.createCellStyle();
		cs.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIME.getIndex());
		cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		
		//New Sheet
		//might not fix
		Sheet sheet = workbook.createSheet("id_numbers");
		
		IDNumber[] idNumbers = presenter.getIDNumbers();
		int i = 0;
		
		Cell cell;
		
		for (IDNumber id : idNumbers) {
			Row row = sheet.createRow(i++);
			cell = row.createCell(0);
			cell.setCellValue("1");
			cell.setCellStyle(cs);
			
			cell = row.createCell(1);
			cell.setCellValue(id.getId());
			cell.setCellStyle(cs);
			
			cell = row.createCell(2);
			cell.setCellValue(id.getStatus().toString());
			cell.setCellStyle(cs);
			
			sheet.autoSizeColumn(i);
		}
		return workbook;
	}
	
	public boolean save(String fileName) {
		Workbook workbook = createExcelFile();
		boolean success = false;
		File file = new File(presenter.getContext().getExternalFilesDir(null), fileName);
		
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(file);
			workbook.write(os);
			Log.w("FileUtils", "Writing file" + file);
			success = true;
		} catch (IOException e) {
			Log.w("FileUtils", "Error writing " + file, e);
		} catch (Exception e) {
			Log.w("FileUtils", "Failed to save file", e);
		} finally {
			try {
				if (null != os) os.close();
			} catch (Exception ignored) {
			}
		}
		
		return success;
	}
}
