package com.kamontat.checkidnumber.api

import android.text.InputType
import com.afollestad.materialdialogs.MaterialDialog
import com.kamontat.checkidnumber.R
import com.kamontat.checkidnumber.model.file.ExcelModel
import com.kamontat.checkidnumber.model.strategy.worksheet.DefaultWorksheetFormat
import com.kamontat.checkidnumber.presenter.MainPresenter
import com.kamontat.checkidnumber.raw.Showable

/**
 * @author kamontat
 * *
 * @version 1.0
 * *
 * @since Sat 20/May/2017 - 3:37 PM
 */
class Export(private val root: MainPresenter) : Showable {

    private fun setting(): MaterialDialog.Builder {
        return MaterialDialog.Builder(root.context).title(title).content(content).inputType(inputType).inputRange(minLength, maxLength).input(R.string.input_file_name_hint, R.string.empty_string, false, callBack).checkBoxPromptRes(R.string.delete_message, false, null).positiveText(R.string.save_message).negativeText(R.string.cancel_message)
    }

    private val title: String
        get() = root.context.resources.getString(R.string.export_title)

    private val content: String
        get() = root.context.resources.getString(R.string.export_content)

    private val inputType: Int
        get() = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS

    private val minLength: Int
        get() = 1

    private val maxLength: Int
        get() = 50

    private val callBack: MaterialDialog.InputCallback
        get() = MaterialDialog.InputCallback { dialog, input ->
            var m = ExcelModel(root).setAutoSize(true)
            if (dialog.isPromptCheckBoxChecked) m = m.setAutoClear(true)
            m.setFileName(input.toString())?.createSheet(root.context.resources.getString(R.string.default_sheet_name))?.addAll(DefaultWorksheetFormat(), root.idNumbers)?.close()
        }

    override fun show() {
        setting().canceledOnTouchOutside(true).show()
    }
}
