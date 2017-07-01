package com.kamontat.checkidnumber.model.file

import com.kamontat.checkidnumber.MainJunitTest
import com.kamontat.checkidnumber.model.IDNumber
import com.kamontat.checkidnumber.model.strategy.worksheet.DefaultWorksheetFormat
import jxl.Workbook
import jxl.read.biff.BiffException
import org.junit.Assert.*
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.junit.runners.Suite
import org.mockito.junit.MockitoJUnitRunner
import java.io.File
import java.io.IOException
import java.util.*

/**
 * @author kamontat
 * *
 * @version 1.0
 * *
 * @since Sat 20/May/2017 - 1:04 PM
 */
@RunWith(Suite::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Suite.SuiteClasses(ExcelModelTest.ExcelModelWithObserverTest::class, ExcelModelTest.ExcelModelWithNormalTest::class)
open class ExcelModelTest {
    protected lateinit var model: ExcelModel
    private val file = File(FILE_NAME)

    @Before
    @Throws(Exception::class)
    open fun setUp() {
        model = ExcelModel(null)
    }

    // use force close, because close using asyncTask that not usable in junit test
    // see excel model test in android test
    @Test
    @Throws(Exception::class)
    fun createFile() {
        val b = model.setFile(file)!!.createSheet(SHEET_NAME).forceClose()
        assertTrue(b)
        assertTrue(file.exists())
        clean()
    }

    @Test
    @Throws(Exception::class)
    fun emptySheetTesting() {
        checkClose(createEmptySheet())
        checkFile()
        checkSheetName()
        clean()
    }

    @Test
    @Throws(Exception::class)
    fun addIDNumber() {
        val number = IDNumber(MainJunitTest.VALIDATE_ID)
        checkClose(createEmptySheet().add(format, number, 0))
        checkFile()
        checkSheet(number, 0)
        clean()
    }

    @Test
    @Throws(Exception::class)
    fun addIDNumberInPosition() {
        val number = IDNumber(MainJunitTest.VALIDATE_ID)
        val pos = 32

        checkClose(createEmptySheet().add(format, number, pos))
        checkFile()
        checkSheetName()
        checkSheet(number, pos)
        clean()
    }

    @Test(expected = Exception::class)
    fun addAllIDNumber() {
        val v_i = IDNumber(MainJunitTest.VALIDATE_ID)
        val i_i_n_u = IDNumber(MainJunitTest.INVALIDATE_ID_NOT_NINE_UPPER_BOUND)
        val i_i_u = IDNumber(MainJunitTest.INVALIDATE_ID_UNCORRECTED)
        val i_i_l_u = IDNumber(MainJunitTest.INVALIDATE_ID_UNMATCHED_LENGTH_UPPER_BOUND)

        val ids = arrayOf(v_i, i_i_n_u, i_i_u, i_i_l_u)

        checkClose(createEmptySheet().addAll(format, ids))
        checkFile()
        checkSheet(ids)
        clean()
    }

    fun createEmptySheet(): ExcelModel.ExcelProcess.SheetProcess {
        return model.setFile(file)!!.createSheet(SHEET_NAME)
    }

    fun checkClose(p: ExcelModel.ExcelProcess.SheetProcess) {
        assertTrue(p.forceClose())
    }

    fun checkFile() {
        assertTrue(file.exists())
    }

    @Throws(IOException::class, BiffException::class)
    fun checkSheetName() {
        val workbook = Workbook.getWorkbook(file)
        assertTrue(workbook.sheets.size > 0)
        for (name in workbook.sheetNames) {
            if (name == SHEET_NAME) {
                workbook.close()
                return
            }
        }
        workbook.close()
        fail()
    }

    @Throws(IOException::class, BiffException::class)
    fun checkSheet(id: IDNumber, at: Int) {
        val workbook = Workbook.getWorkbook(file)
        val sheet = workbook.getSheet(SHEET_NAME)
        val cellID = sheet.getCell(0, at) // column, row
        val cellName = sheet.getCell(1, at) // column, row
        val realID = java.lang.Long.parseLong(cellID.contents)

        assertEquals((at + 1).toLong(), realID)
        assertEquals(id.getId(), cellName.contents)
    }

    @Throws(IOException::class, BiffException::class)
    fun checkSheet(ids: Array<IDNumber>) {
        checkSheetName()
        ids.forEachIndexed({
            index, idNumber ->
            checkSheet(idNumber, index)

        })
    }

    internal inner class MyObserver : Observer {
        override fun update(observable: Observable, o: Any?) {
            if (o == null) {
                assertTrue(true)
                // System.out.println("log: update on " + getClass().getName());
            } else if (o.javaClass == Boolean::class.java) {
                assertTrue(Boolean::class.java.cast(o))
            } else if (o.javaClass == ExcelModel::class.java) {
                assertEquals("", ExcelModel::class.java.cast(o).stringException)
            }
        }
    }

    fun clean() {
        file.delete()
    }

    @RunWith(MockitoJUnitRunner::class)
    class ExcelModelWithObserverTest : ExcelModelTest() {
        @Before
        @Throws(Exception::class)
        override fun setUp() {
            super.setUp()
            model.addObservers(MyObserver())
        }
    }

    @RunWith(MockitoJUnitRunner::class)
    class ExcelModelWithNormalTest : ExcelModelTest()

    companion object {
        private val FILE_NAME = "test.xls"
        private val SHEET_NAME = "test"

        private val format = DefaultWorksheetFormat()
    }
}