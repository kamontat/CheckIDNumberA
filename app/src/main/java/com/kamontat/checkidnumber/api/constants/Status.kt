package com.kamontat.checkidnumber.api.constants

import android.content.res.Resources
import com.kamontat.checkidnumber.R
import java.io.Serializable

/**
 * this enum use for check status of id-number
 * @author kamontat
 * @version 1.0
 * @since 1/7/60 - 11:08
 */
enum class Status private constructor(private val message: String, val color: Int) : Serializable {
    OK("OK (Good ID)", R.color.colorCorrect),
    UNMATCHED_LENGTH("Warning (MUST be 13 Digit)", R.color.colorWarning),
    NOT_NINE("Warning (First digit CANNOT be 9)", R.color.colorWarning),
    UNCORRECTED("Error (ID NOT match with id rule)", R.color.colorError),
    NOT_CREATE("Error (NEVER assign ID)", R.color.colorError);

    fun getColor(resources: Resources): Int = resources.getColor(color)

    override fun toString(): String = message
}