package com.kamontat.checkidnumber.model

import com.kamontat.checkidnumber.api.constants.Status
import com.kamontat.checkidnumber.model.strategy.idnumber.ThailandIDNumberStrategy
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * @author kamontat
 * *
 * @version 1.0
 * *
 * @since Mon 15/May/2017 - 1:55 AM
 */
class ThaiIDNumberTest {

    @Before
    @Throws(Exception::class)
    fun setUp() {
        IDNumber.strategy = ThailandIDNumberStrategy()
    }

    @Test
    fun shouldOK() {
        val id = IDNumber("1100600361966")
        Assert.assertEquals(Status.OK, id.status)
    }

    @Test
    fun shouldUnmatched() {
        val id1 = IDNumber("1234123414")
        Assert.assertEquals(Status.UNMATCHED_LENGTH, id1.status)
        val id2 = IDNumber("123412341234123")
        Assert.assertEquals(Status.UNMATCHED_LENGTH, id2.status)
    }

    @Test
    fun shouldNotCreate() {
        val id = IDNumber()
        Assert.assertEquals(Status.NOT_CREATE, id.status)
    }

    @Test
    fun firstCharCannotBeNine() {
        val id = IDNumber("9123123123132")
        Assert.assertEquals(Status.NOT_NINE, id.status)
    }

    @Test
    fun shouldUnCorrect() {
        val id = IDNumber("1100600361961")
        Assert.assertEquals(Status.UNCORRECTED, id.status)
    }

    @Test
    fun shouldCheckWhenSetNewID() {
        val id = IDNumber("1100600361966")
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