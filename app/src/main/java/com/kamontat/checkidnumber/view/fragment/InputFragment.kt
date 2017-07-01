package com.kamontat.checkidnumber.view.fragment

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.kamontat.checkidnumber.R
import com.kamontat.checkidnumber.api.constants.Status
import java.util.*

/**
 * @author kamontat
 * *
 * @version 1.0
 * *
 * @since Thu 11/May/2017 - 10:22 PM
 */
class InputFragment() : Fragment() {
    private var message: TextView? = null
    private var statusMessage: TextView? = null
    private var input: EditText? = null
    private var button: Button? = null

    private var clickListener: View.OnClickListener? = null
    private var watcher: TextWatcher? = null
    private val editorActionListener = TextView.OnEditorActionListener { _, i, _ ->
        if (i == EditorInfo.IME_ACTION_GO) {
            button!!.callOnClick()
            // hideKeyBoard(getActivity());
            return@OnEditorActionListener true
        }
        false
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_input, container, false)

        message = view.findViewById(R.id.fragment_message) as TextView
        updateInputSize(0)
        statusMessage = view.findViewById(R.id.fragment_status_message) as TextView
        input = view.findViewById(R.id.fragment_input_id_number) as EditText
        if (watcher != null) input!!.addTextChangedListener(watcher)
        input!!.setOnEditorActionListener(editorActionListener)
        button = view.findViewById(R.id.fragment_btn) as Button
        if (clickListener != null) button!!.setOnClickListener(clickListener)
        updateStatus(Status.NOT_CREATE, view.resources)
        return view
    }

    fun getInput(): String {
        return input!!.text.toString()
    }

    fun updateStatus(status: Status, r: Resources) {
        statusMessage!!.text = status.toString()
        statusMessage!!.setTextColor(status.getColor(r))
        input!!.setTextColor(status.getColor(r))
    }

    fun updateInputSize(size: Int) {
        val message = String.format(Locale.ENGLISH, "%s (%d)", resources.getString(R.string.input_message), size)
        this.message!!.text = message
    }

    fun setInputListener(watcher: TextWatcher) {
        this.watcher = watcher
    }

    fun setButton(clickListener: View.OnClickListener) {
        this.clickListener = clickListener
    }

    fun setButtonEnable(b: Boolean) {
        button!!.isEnabled = b
    }

    fun showKeyboard(activity: Activity) {
        if (input != null && input!!.requestFocus()) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    fun hideKeyBoard(activity: Activity) {
        val v = activity.window.currentFocus
        if (v != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    fun clearText() {
        input!!.setText("")
    }
}// Required empty public constructor
