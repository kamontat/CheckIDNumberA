package com.kamontat.checkidnumber.presenter

import android.content.Context
import com.kamontat.checkidnumber.model.IDNumber
import com.kamontat.checkidnumber.model.Pool
import com.kamontat.checkidnumber.view.MainView
import java.io.Serializable

/**
 * @author kamontat
 * *
 * @version 1.0
 * *
 * @since Wed 17/May/2017 - 11:07 PM
 */
class MainPresenter(private val view: MainView, val pool: Pool) : Serializable {

    fun addID(idNumber: IDNumber) {
        pool.add(idNumber)
    }

    val isStorageWritable: Boolean
        get() = view.isStorageWritable

    val idNumbers: Array<IDNumber>
        get() = pool.getIDNumbers()

    val context: Context
        get() = view.context

    fun requestPermission(): Boolean {
        return view.requestPermission()
    }

    fun checkPermission(): Boolean {
        return view.checkPermission()
    }
}
