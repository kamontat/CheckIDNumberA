package com.kamontat.checkidnumber.api

import com.kamontat.checkidnumber.api.constants.AppConstants

/**
 * @author kamontat
 * @version 1.0
 * @since Sat 01/Jul/2017 - 4:00 PM
 */
object Logger {
    fun Log(function: () -> Unit) {
        if (!AppConstants.production)
            function.invoke()
    }

    fun Log(TAG: String, function: (String) -> Unit) {
        if (!AppConstants.production)
            function.invoke(TAG)
    }
}
