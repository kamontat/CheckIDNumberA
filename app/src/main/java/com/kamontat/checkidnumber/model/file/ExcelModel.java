package com.kamontat.checkidnumber.model.file;

import android.os.AsyncTask;
import com.afollestad.materialdialogs.MaterialDialog;
import com.kamontat.checkidnumber.model.IDNumber;
import com.kamontat.checkidnumber.model.strategy.worksheet.WorksheetFormat;
import com.kamontat.checkidnumber.presenter.MainPresenter;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.SpreadsheetML.WorksheetPart;
import org.xlsx4j.jaxb.Context;
import org.xlsx4j.sml.Cell;
import org.xlsx4j.sml.Row;
import org.xlsx4j.sml.SheetData;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * This class use to create new excel file in <code>folderList</code> location <br>
 * It's have only one method call <b><code>createExcelFile</code></b> to create Excel by using <code>idList</code>
 *
 * @author kamontat
 * @version 1.0
 * @since 17/8/59 - 21:49
 */
public class ExcelModel extends Observable {
	private MainPresenter presenter;
	
	private WorksheetPart worksheet;
	
	private Exception e;
	private SpreadsheetMLPackage sheetPackage;
	
	public ExcelModel(MainPresenter presenter) {
		this.presenter = presenter;
	}
	
	public ExcelModel createWorkSheet(String sheetName) {
		try {
			sheetPackage = SpreadsheetMLPackage.createPackage();
			worksheet = sheetPackage.createWorksheetPart(new PartName("/xl/worksheets/sheet1.xml"), sheetName, 0);
		} catch (Exception e) {
			this.e = e;
		}
		return this;
	}
	
	public ExcelModel add(WorksheetFormat<IDNumber> strategy, IDNumber[] numbers) {
		if (isError()) return this;
		else if (worksheet == null) createWorkSheet("auto-create");
		
		try {
			SheetData sheetData = worksheet.getContents().getSheetData();
			
			long i = 0;
			Cell numericCell, idCell;
			
			for (IDNumber id : numbers) {
				Row row = Context.getsmlObjectFactory().createRow();
				row.setR(i++);
				for (Cell c : strategy.getCellsInRow(i, id))
					row.getC().add(c);
				sheetData.getRow().add(row);
			}
		} catch (Docx4JException e) {
			this.e = e;
		}
		return this;
	}
	
	public void save(ExecutorService service, String fileName) {
		new FileTask(this).executeOnExecutor(service, fileName);
	}
	
	/**
	 * @param service
	 * 		thread service
	 * @param observer
	 * 		update with 3 type <ul><li>boolean - true if successful</li><li>model - ExcelModel</li><li>none - nothing pass</li></ul>
	 * @param fileName
	 * 		file name
	 */
	public void save(ExecutorService service, Observer observer, String fileName) {
		addObserver(observer);
		new FileTask(this).executeOnExecutor(service, fileName);
	}
	
	private boolean load(String fileName) {
		if (isError()) return false;
		File file = new File(presenter.getContext().getExternalFilesDir(null), fileName);
		try {
			sheetPackage.save(file);
			return true;
		} catch (Docx4JException e) {
			this.e = e;
			return false;
		}
	}
	
	private boolean hadObserver() {
		return countObservers() > 0;
	}
	
	private boolean isError() {
		return e != null;
	}
	
	private class FileTask extends AsyncTask<String, Void, Boolean> {
		private ExcelModel model;
		
		private FileTask(ExcelModel model) {
			this.model = model;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected Boolean doInBackground(String[] str) {
			return model.load(str[0]);
		}
		
		@Override
		protected void onPostExecute(Boolean aBoolean) {
			super.onPostExecute(aBoolean);
			if (model.hadObserver()) {
				setChanged();
				notifyObservers(aBoolean);
				setChanged();
				notifyObservers(model);
				setChanged();
				notifyObservers();
			} else {
				if (isError())
					new MaterialDialog.Builder(presenter.getContext()).title("Fail").content(e.getMessage()).canceledOnTouchOutside(true).show();
				else
					new MaterialDialog.Builder(presenter.getContext()).title("Success").content("Done!").canceledOnTouchOutside(true).show();
			}
		}
	}
}
