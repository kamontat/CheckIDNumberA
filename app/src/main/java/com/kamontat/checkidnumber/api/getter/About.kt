package com.kamontat.checkidnumber.api.getter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.afollestad.materialdialogs.MaterialDialog
import com.kamontat.checkidnumber.BuildConfig
import com.kamontat.checkidnumber.R
import com.kamontat.checkidnumber.raw.Showable

import java.util.*

/**
 * @author kamontat
 * *
 * @version 1.0
 * *
 * @since Sat 20/May/2017 - 3:10 PM
 */
class About(private val root: Activity) : Showable {

    private fun setting(): MaterialDialog.Builder {
        return MaterialDialog.Builder(root).title(title).content(content).items(R.array.developer_name).itemsCallback(callBack).positiveText(R.string.ok_message)
    }

    private val title: String
        get() = String.format(Locale.ENGLISH, "%s", root.resources.getString(R.string.about_title))

    private val content: String
        get() = root.resources.getString(R.string.app_description) + "\n\n" + VERSION + "\n\nDevelop by"

    private val callBack: MaterialDialog.ListCallback
        get() = MaterialDialog.ListCallback { dialog, itemView, which, text ->
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(root.resources.getString(R.string.developer_github)))
            root.startActivity(browserIntent)
        }

    override fun show() {
        setting().canceledOnTouchOutside(true).show()
    }

    companion object {
        private val VERSION = String.format(Locale.ENGLISH, "v%s-build: %s", BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE)
    }
}
