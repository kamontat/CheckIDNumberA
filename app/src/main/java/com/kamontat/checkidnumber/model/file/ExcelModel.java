package com.kamontat.checkidnumber.model.file;

import android.os.AsyncTask;
import android.os.Environment;
import com.afollestad.materialdialogs.MaterialDialog;
import com.kamontat.checkidnumber.R;
import com.kamontat.checkidnumber.model.IDNumber;
import com.kamontat.checkidnumber.model.strategy.worksheet.DefaultWorksheetFormat;
import com.kamontat.checkidnumber.model.strategy.worksheet.WorksheetFormat;
import com.kamontat.checkidnumber.presenter.MainPresenter;
import jxl.CellView;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

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
	
	private String location;
	private boolean autoSize;
	private boolean autoClear;
	
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
		location = file.getAbsolutePath();
		try {
			return new ExcelProcess(Workbook.createWorkbook(file));
		} catch (IOException e) {
			this.e.add(e);
			return new ExcelProcess();
		}
	}
	
	/**
	 * create file
	 *
	 * @param f
	 * 		file {@link File}
	 * @return this
	 */
	public ExcelProcess setFile(File f) {
		location = f.getAbsolutePath();
		try {
			return new ExcelProcess(Workbook.createWorkbook(f));
		} catch (IOException e) {
			this.e.add(e);
			return new ExcelProcess();
		}
	}
	
	public ExcelModel setAutoSize(boolean enable) {
		autoSize = enable;
		return this;
	}
	
	public ExcelModel setAutoClear(boolean enable) {
		autoClear = enable;
		return this;
	}
	
	public synchronized ExcelModel addObservers(Observer o) {
		super.addObserver(o);
		return this;
	}
	
	public Exception[] getException() {
		return e.toArray(new Exception[e.size()]);
	}
	
	public String getStringException() {
		if (!isError()) return "";
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
		
		private void close() {
			ExecutorService service = Executors.newCachedThreadPool();
			new FileTask(ExcelModel.this).executeOnExecutor(service, this);
		}
		
		private boolean forceClose() {
			if (isError()) return false;
			if (presenter != null && !presenter.checkPermission() && !presenter.requestPermission()) return false;
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
			if (hadObserver()) notifyAllObserver();
			return !isError();
		}
		
		public class SheetProcess {
			private WritableSheet sheet;
			
			private SheetProcess() {
			}
			
			private SheetProcess(WritableSheet sheet) {
				this.sheet = sheet;
			}
			
			public SheetProcess add(WorksheetFormat<IDNumber, DefaultWorksheetFormat.PositionValue> format, IDNumber id, int atRow) {
				if (isError()) return this;
				DefaultWorksheetFormat.PositionValue[] vs = format.getCellsInRow(atRow + 1, id);
				try {
					// Log.i("READ ID", vs[0].getValue());
					for (DefaultWorksheetFormat.PositionValue v : vs) {
						sheet.addCell(new Label(v.getColumn(), atRow, v.getValue()));
						if (autoSize) setAutoSize(sheet, v.getColumn());
					}
				} catch (WriteException e1) {
					e.add(e1);
				}
				return this;
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
	
	private void setAutoSize(WritableSheet sheet, int column) {
		CellView autoSize = new CellView();
		autoSize.setAutosize(true);
		sheet.setColumnView(column, autoSize);
	}
	
	private boolean hadObserver() {
		return countObservers() > 0;
	}
	
	private boolean isError() {
		return e != null && e.size() > 0;
	}
	
	private void notifyAllObserver() {
		setChanged();
		notifyObservers(!isError());
		setChanged();
		notifyObservers(this);
		setChanged();
		notifyObservers();
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
			// Log.d("SAVE FILE THREAD", Thread.currentThread().toString());
			return str[0].forceClose();
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			String success = presenter.getContext().getResources().getString(R.string.success);
			String fail = presenter.getContext().getResources().getString(R.string.fail);
			if (result) {
				if (autoClear) presenter.getPool().clear();
				new MaterialDialog.Builder(presenter.getContext()).title(success).content("Location: " + location).negativeText(R.string.cancel_message).canceledOnTouchOutside(true).show();
			} else
				new MaterialDialog.Builder(presenter.getContext()).title(fail).content(getStringException()).negativeText(R.string.cancel_message).canceledOnTouchOutside(true).show();
		}
	}
}
