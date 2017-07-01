package com.kamontat.checkidnumber.model.file

import android.os.AsyncTask
import android.os.Environment
import com.afollestad.materialdialogs.MaterialDialog
import com.kamontat.checkidnumber.R
import com.kamontat.checkidnumber.model.IDNumber
import com.kamontat.checkidnumber.model.strategy.worksheet.DefaultWorksheetFormat
import com.kamontat.checkidnumber.model.strategy.worksheet.WorksheetFormat
import com.kamontat.checkidnumber.presenter.MainPresenter
import jxl.CellView
import jxl.Workbook
import jxl.write.Label
import jxl.write.WritableSheet
import jxl.write.WritableWorkbook
import jxl.write.WriteException
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.Executors

/**
 * This class use to create new excel file in `folderList` location <br></br>
 * It's have only one method call **`createExcelFile`** to create Excel by using `idList`

 * @author kamontat
 * *
 * @version 1.0
 * *
 * @since 17/8/59 - 21:49
 */
class ExcelModel(private val presenter: MainPresenter?) : Observable() {
    private val e: MutableList<Exception>? = null

    private var location: String? = null
    private var autoSize: Boolean = false
    private var autoClear: Boolean = false

    /**
     * create file

     * @param fileName
     * * 		file name without extension
     * *
     * @return this
     */
    fun setFileName(fileName: String): ExcelProcess? {
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName + XLS)
        location = file.absolutePath
        try {
            return ExcelProcess(Workbook.createWorkbook(file))
        } catch (e: IOException) {
            this.e!!.add(e)
            return null
        }

    }

    /**
     * create file

     * @param f
     * * 		file [File]
     * *
     * @return this
     */
    fun setFile(f: File): ExcelProcess? {
        location = f.absolutePath
        try {
            return ExcelProcess(Workbook.createWorkbook(f))
        } catch (e: IOException) {
            this.e!!.add(e)
            return null
        }

    }

    fun setAutoSize(enable: Boolean): ExcelModel {
        autoSize = enable
        return this
    }

    fun setAutoClear(enable: Boolean): ExcelModel {
        autoClear = enable
        return this
    }

    @Synchronized fun addObservers(o: Observer): ExcelModel {
        super.addObserver(o)
        return this
    }

    val exception: Array<Exception>
        get() = e!!.toTypedArray()

    val stringException: String
        get() {
            if (!isError) return ""
            val sb = StringBuilder()
            for (e in this.e!!) {
                if (sb.isEmpty())
                    sb.append(e.message)
                else
                    sb.append(", ").append(e.message)
            }
            return sb.toString()
        }

    inner class ExcelProcess(val workbook: WritableWorkbook) {
        private val sheet: WritableSheet? = null

        fun createSheet(name: String): SheetProcess {
            if (isError)
                return SheetProcess(workbook.createSheet(name, workbook.numberOfSheets))
            else
                return SheetProcess(workbook.createSheet(name, workbook.numberOfSheets))
        }

        private fun close() {
            val service = Executors.newCachedThreadPool()
            FileTask(this@ExcelModel).executeOnExecutor(service, this)
        }

        internal fun forceClose(): Boolean {
            if (isError) return false
            if (presenter != null && !presenter.checkPermission() && !presenter.requestPermission()) return false
            try {
                workbook.write()
            } catch (e1: IOException) {
                e!!.add(e1)
            }

            try {
                workbook.close()
            } catch (e1: IOException) {
                e!!.add(e1)
            } catch (e1: WriteException) {
                e!!.add(e1)
            }

            if (hadObserver()) notifyAllObserver()
            return !isError
        }

        inner class SheetProcess internal constructor(private val sheet: WritableSheet) {
            fun add(format: WorksheetFormat<IDNumber, DefaultWorksheetFormat.PositionValue>, id: IDNumber, atRow: Int): SheetProcess {
                if (isError) return this
                val vs = format.getCellsInRow((atRow + 1).toLong(), id)
                try {
                    // Log.i("READ ID", vs[0].getValue());
                    for (v in vs) {
                        sheet.addCell(Label(v.column, atRow, v.value))
                        if (autoSize) setAutoSize(sheet, v.column)
                    }
                } catch (e1: WriteException) {
                    e!!.add(e1)
                }

                return this
            }

            fun addAll(format: WorksheetFormat<IDNumber, DefaultWorksheetFormat.PositionValue>, ids: Array<IDNumber>): SheetProcess {
                if (isError) return this

                var i = 0
                ids.forEachIndexed { index, idNumber ->
                    {
                        add(format, idNumber, index)
                    }
                }
                return this
            }

            fun close() {
                this@ExcelProcess.close()
            }

            fun forceClose(): Boolean {
                return this@ExcelProcess.forceClose()
            }
        }
    }

    private fun setAutoSize(sheet: WritableSheet, column: Int) {
        val autoSize = CellView()
        autoSize.isAutosize = true
        sheet.setColumnView(column, autoSize)
    }

    private fun hadObserver(): Boolean {
        return countObservers() > 0
    }

    private val isError: Boolean
        get() = e != null && e.size > 0

    private fun notifyAllObserver() {
        setChanged()
        notifyObservers(!isError)
        setChanged()
        notifyObservers(this)
        setChanged()
        notifyObservers()
    }

    private inner class FileTask(private val model: ExcelModel) : AsyncTask<ExcelProcess, Void, Boolean>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(str: Array<ExcelProcess>): Boolean? {
            // Log.d("SAVE FILE THREAD", Thread.currentThread().toString());
            return str[0].forceClose()
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
            val success = presenter!!.context.resources.getString(R.string.success)
            val fail = presenter.context.resources.getString(R.string.fail)
            if (result!!) {
                if (autoClear) presenter.pool.clear()
                MaterialDialog.Builder(presenter.context).title(success).content("Location: " + location!!).negativeText(R.string.cancel_message).canceledOnTouchOutside(true).show()
            } else
                MaterialDialog.Builder(presenter.context).title(fail).content(stringException).negativeText(R.string.cancel_message).canceledOnTouchOutside(true).show()
        }
    }

    companion object {
        private val XLS = ".xls"
        private val XLSX = ".xlsx"
    }
}
