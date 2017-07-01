package com.kamontat.checkidnumber.api

import android.Manifest
import android.app.Activity
import android.support.v4.app.ActivityCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.kamontat.checkidnumber.R
import com.kamontat.checkidnumber.raw.Showable
import com.kamontat.checkidnumber.view.MainActivity.Constants

/**
 * @author kamontat
 * *
 * @version 1.0
 * *
 * @since Sun 21/May/2017 - 4:17 PM
 */
class NonExport(private val root: Activity) : Showable {

    private fun setting(): MaterialDialog.Builder {
        return MaterialDialog.Builder(root).title(title).content(content).positiveText(R.string.yes).negativeText(R.string.no).onPositive(requestCallBack)
    }

    private val title: String
        get() = root.resources.getString(R.string.no_permission)

    private val content: String
        get() = root.resources.getString(R.string.ask_for_sure)

    private val requestCallBack: MaterialDialog.SingleButtonCallback
        get() = MaterialDialog.SingleButtonCallback { _, _ -> ActivityCompat.requestPermissions(root, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), Constants.PERMISSION_CODE) }

    override fun show() {
        setting().canceledOnTouchOutside(true).show()
    }
}
