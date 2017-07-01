package com.kamontat.checkidnumber.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.io.Serializable

import java.util.*

/**
 * @author kamontat
 * *
 * @version 1.0
 * *
 * @since Thu 11/May/2017 - 9:20 PM
 */
class Pool(context: Context) : ArrayAdapter<IDNumber>(context, android.R.layout.two_line_list_item, android.R.id.text1), Serializable {
    private var idNumbers: ArrayList<IDNumber> = ArrayList<IDNumber>()

    override fun add(`object`: IDNumber?) {
        super.add(`object`)
        `object`?.let { idNumbers.add(it) }
    }

    override fun remove(`object`: IDNumber?) {
        super.remove(`object`)
        idNumbers.remove(`object`)
    }

    override fun clear() {
        super.clear()
        idNumbers.clear()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null)
            view = LayoutInflater.from(context).inflate(android.R.layout.two_line_list_item, parent, false)

        val textView1 = view!!.findViewById(android.R.id.text1) as TextView
        val textView2 = view.findViewById(android.R.id.text2) as TextView

        val idNumber = getItem(position) ?: return super.getView(position, view, parent)

        textView1.text = idNumber.getId()

        textView2.text = String.format(Locale.ENGLISH, "Status: %s", idNumber.status.toString())
        textView2.setTextColor(idNumber.status.getColor(context.resources))

        return super.getView(position, view, parent)
    }

    fun getIDNumbers(): Array<IDNumber> {
        return idNumbers.toTypedArray()
    }
}
