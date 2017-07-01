package com.kamontat.checkidnumber.model.strategy

import com.kamontat.checkidnumber.api.constants.Status
import com.kamontat.checkidnumber.model.strategy.idnumber.IDNumberStrategy
import com.kamontat.checkidnumber.model.strategy.idnumber.ThailandIDNumberStrategy
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * @author kamontat
 * *
 * @version 1.0
 * *
 * @since Wed 17/May/2017 - 11:10 PM
 */
class ThailandIDNumberStrategyTest {
    internal lateinit var strategy: IDNumberStrategy

    @Before
    @Throws(Exception::class)
    fun setUp() {
        strategy = ThailandIDNumberStrategy()
    }

    @Test
    @Throws(Exception::class)
    fun getLengthOfThaiID() {
        Assert.assertEquals(13, strategy.idLength)
    }

    @Test
    @Throws(Exception::class)
    fun shouldOK() {
        val s = strategy.checking("1100600361966")
        Assert.assertEquals(s, Status.OK)
    }

    @Test
    @Throws(Exception::class)
    fun shouldNotCreate() {
        val s = strategy.checking("")
        Assert.assertEquals(s, Status.NOT_CREATE)

        val ss = strategy.checking(null)
        Assert.assertEquals(ss, Status.NOT_CREATE)
    }

    @Test
    @Throws(Exception::class)
    fun shouldNot_create() {
        val s = strategy.checking("4839586954321")
        Assert.assertEquals(s, Status.UNCORRECTED)
    }
}