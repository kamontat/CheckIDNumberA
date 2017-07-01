package com.kamontat.checkidnumber.model.strategy.worksheet

import com.kamontat.checkidnumber.model.IDNumber

/**
 * @author kamontat
 * *
 * @version 1.0
 * *
 * @since Thu 18/May/2017 - 2:55 PM
 */
class DefaultWorksheetFormat : WorksheetFormat<IDNumber, DefaultWorksheetFormat.PositionValue> {
    override fun getCellsInRow(id: Long, t: IDNumber): Array<PositionValue> {
        return arrayOf(PositionValue(0, id.toString()), t.getId()?.let { PositionValue(1, it) } ?: PositionValue(1, "-1"))
    }

    inner class PositionValue internal constructor(val column: Int, val value: String)
}