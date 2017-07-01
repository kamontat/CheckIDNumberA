package com.kamontat.checkidnumber.model

import com.kamontat.checkidnumber.MainJunitTest
import com.kamontat.checkidnumber.api.constants.Status
import org.junit.Assert
import org.junit.Test

/**
 * @author kamontat
 * *
 * @version 1.0
 * *
 * @since Mon 15/May/2017 - 1:55 AM
 */
class IDNumberTest {
    @Test
    fun shouldOK() {
        val id = IDNumber(MainJunitTest.VALIDATE_ID)
        Assert.assertEquals(Status.OK, id.status)
    }

    @Test
    fun shouldUnmatched() {
        val id1 = IDNumber(MainJunitTest.INVALIDATE_ID_UNMATCHED_LENGTH_UPPER_BOUND)
        Assert.assertEquals(Status.UNMATCHED_LENGTH, id1.status)
        val id2 = IDNumber(MainJunitTest.INVALIDATE_ID_UNMATCHED_LENGTH_LOWER_BOUND)
        Assert.assertEquals(Status.UNMATCHED_LENGTH, id2.status)
    }

    @Test
    fun shouldNotCreate() {
        val id = IDNumber()
        Assert.assertEquals(Status.NOT_CREATE, id.status)
    }

    @Test
    fun firstCharCannotBeNine() {
        val id1 = IDNumber(MainJunitTest.INVALIDATE_ID_NOT_NINE_LOWER_BOUND)
        Assert.assertEquals(Status.NOT_NINE, id1.status)

        val id2 = IDNumber(MainJunitTest.INVALIDATE_ID_NOT_NINE_NORMAL_BOUND)
        Assert.assertEquals(Status.NOT_NINE, id2.status)

        val id3 = IDNumber(MainJunitTest.INVALIDATE_ID_NOT_NINE_UPPER_BOUND)
        Assert.assertEquals(Status.NOT_NINE, id3.status)
    }

    @Test
    fun shouldUnCorrect() {
        val id = IDNumber(MainJunitTest.INVALIDATE_ID_UNCORRECTED)
        Assert.assertEquals(Status.UNCORRECTED, id.status)
    }

    @Test
    fun shouldCheckWhenSetNewID() {
        val id = IDNumber(MainJunitTest.VALIDATE_ID)
        Assert.assertEquals(Status.OK, id.status)
        id.setId("1000")
        Assert.assertEquals(Status.UNMATCHED_LENGTH, id.status)
        id.setId("9291823347812")
        Assert.assertEquals(Status.NOT_NINE, id.status)
    }

    @Test
    fun shouldEqual() {
        val id1 = IDNumber("1")
        val id2 = IDNumber("1")
        val id3 = IDNumber("2")

        Assert.assertTrue(id1 == id2)
        Assert.assertFalse(id1 == id3)
    }
}