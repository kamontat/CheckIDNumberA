package com.kamontat.checkidnumber.view

import android.content.Context
import com.kamontat.checkidnumber.model.IDNumber
import java.io.Serializable

/**
 * @author kamontat
 * *
 * @version 1.0
 * *
 * @since Mon 15/May/2017 - 1:23 AM
 */
interface MainView : Serializable {
    val inputText: String

    fun checkAndInsert()

    fun updateInput(id: IDNumber)

    fun hideKeyBoard()

    fun showKeyBoard()

    val isStorageWritable: Boolean

    val idNumbers: Array<IDNumber>

    val context: Context

    fun checkPermission(): Boolean

    /**
     * @return
     * * 0 - already have permission
     * * 1 - on requesting permission
     * * 2 - deny before
     */
    fun requestPermission(): Boolean
}