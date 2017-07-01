package com.kamontat.checkidnumber.model.strategy.idnumber

import com.kamontat.checkidnumber.api.constants.Status

/**
 * @author kamontat
 * *
 * @version 1.0
 * *
 * @since Thu 11/May/2017 - 11:47 PM
 */
class ThailandIDNumberStrategy : IDNumberStrategy {
    override val idLength: Int
        get() = 13

    override fun checking(id: String?): Status {
        if (id == null || id == "") return Status.NOT_CREATE
        val splitID = id.toCharArray()

        when {
            splitID[0] == '9' -> return Status.NOT_NINE
            splitID.size != idLength -> return Status.UNMATCHED_LENGTH
            else -> {
                var total: Int = 0
                (1..12).forEach { i ->
                    val digit = Character.getNumericValue(splitID[i - 1])
                    total += (14 - i) * digit
                }
                total %= 11
                val lastDigit = Character.getNumericValue(splitID[splitID.size - 1])
                when {
                    total <= 1 -> if (lastDigit == 1 - total) return Status.OK
                    else -> if (total > 1) if (lastDigit == 11 - total) return Status.OK
                }
                return Status.UNCORRECTED
            }
        }
    }
}
