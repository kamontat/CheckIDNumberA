package com.kamontat.checkidnumber.model.file;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import com.afollestad.materialdialogs.MaterialDialog;
import com.kamontat.checkidnumber.model.IDNumber;
import com.kamontat.checkidnumber.model.strategy.worksheet.DefaultWorksheetFormat;
import com.kamontat.checkidnumber.model.strategy.worksheet.WorksheetFormat;
import com.kamontat.checkidnumber.presenter.MainPresenter;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class use to create new excel file in <code>folderList</code> location <br>
 * It's have only one method call <b><code>createExcelFile</code></b> to create Excel by using <code>idList</code>
 *
 * @author kamontat
 * @version 1.0
 * @since 17/8/59 - 21:49
 */
public class ExcelModel extends Observable {
	private static final String XLS = ".xls";
	private static final String XLSX = ".xlsx";
	
	private MainPresenter presenter;
	private List<Exception> e;
	
	private SpreadsheetMLPackage sheetPackage;
	
	public ExcelModel(MainPresenter presenter) {
		this.presenter = presenter;
	}
	
	/**
	 * create file
	 *
	 * @param fileName
	 * 		file name without extension
	 * @return this
	 */
	public ExcelProcess setFileName(String fileName) {
		File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName + XLS);
		try {
			return new ExcelProcess(Workbook.createWorkbook(file));
		} catch (IOException e) {
			this.e.add(e);
			return new ExcelProcess();
		}
	}
	
	public synchronized ExcelModel addObservers(Observer o) {
		super.addObserver(o);
		return this;
	}
	
	public Exception[] getException() {
		return e.toArray(new Exception[e.size()]);
	}
	
	public String getStringException() {
		StringBuilder sb = new StringBuilder();
		for (Exception e : this.e) {
			if (sb.length() == 0) sb.append(e.getMessage());
			else sb.append(", ").append(e.getMessage());
		}
		return sb.toString();
	}
	
	public class ExcelProcess {
		private WritableWorkbook workbook;
		private WritableSheet sheet;
		
		private ExcelProcess(WritableWorkbook workbook) {
			this.workbook = workbook;
		}
		
		private ExcelProcess() {
		}
		
		public SheetProcess createSheet(String name) {
			if (isError()) return new SheetProcess();
			else return new SheetProcess(workbook.createSheet(name, workbook.getNumberOfSheets()));
		}
		
		public void close() {
			ExecutorService service = Executors.newCachedThreadPool();
			new FileTask(ExcelModel.this).executeOnExecutor(service, this);
		}
		
		public boolean forceClose() {
			if (isError()) return false;
			if (!checkPermission() && !presenter.requestPermission()) return false;
			
			try {
				workbook.write();
			} catch (IOException e1) {
				e.add(e1);
			}
			try {
				workbook.close();
			} catch (IOException | WriteException e1) {
				e.add(e1);
			}
			return !isError();
		}
		
		public class SheetProcess {
			private WritableSheet sheet;
			
			private SheetProcess() {
			}
			
			private SheetProcess(WritableSheet sheet) {
				this.sheet = sheet;
			}
			
			public void add(WorksheetFormat<IDNumber, DefaultWorksheetFormat.PositionValue> format, IDNumber id, int atRow) {
				if (isError()) return;
				DefaultWorksheetFormat.PositionValue[] vs = format.getCellsInRow(0, id);
				try {
					for (DefaultWorksheetFormat.PositionValue v : vs) {
						Log.i("READ ID", v.getValue());
						sheet.addCell(new Label(v.getColumn(), atRow, v.getValue()));
					}
				} catch (WriteException e1) {
					e.add(e1);
				}
			}
			
			public SheetProcess addAll(WorksheetFormat<IDNumber, DefaultWorksheetFormat.PositionValue> format, IDNumber[] ids) {
				if (isError()) return this;
				
				int i = 0;
				for (IDNumber id : ids) {
					add(format, id, i++);
				}
				return this;
			}
			
			public void close() {
				ExcelModel.ExcelProcess.this.close();
			}
			
			public boolean forceClose() {
				return ExcelModel.ExcelProcess.this.forceClose();
			}
		}
	}
	
	private boolean hadObserver() {
		return countObservers() > 0;
	}
	
	private boolean isError() {
		return e != null && e.size() > 0;
	}
	
	
	private boolean checkPermission() {
		int permissionCheck = ContextCompat.checkSelfPermission(presenter.getContext(), Manifest.permission.WRITE_CALENDAR);
		Log.d("PERMISSION", String.valueOf(permissionCheck == PackageManager.PERMISSION_GRANTED));
		return permissionCheck == PackageManager.PERMISSION_GRANTED;
	}
	
	private class FileTask extends AsyncTask<ExcelProcess, Void, Boolean> {
		private ExcelModel model;
		
		private FileTask(ExcelModel model) {
			this.model = model;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected Boolean doInBackground(ExcelProcess[] str) {
			Log.d("SAVE FILE THREAD", Thread.currentThread().toString());
			return str[0].forceClose();
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
					new MaterialDialog.Builder(presenter.getContext()).title("Fail").content(getStringException()).canceledOnTouchOutside(true).show();
				else
					new MaterialDialog.Builder(presenter.getContext()).title("Success").content("Done!").canceledOnTouchOutside(true).show();
			}
		}
	}
}
