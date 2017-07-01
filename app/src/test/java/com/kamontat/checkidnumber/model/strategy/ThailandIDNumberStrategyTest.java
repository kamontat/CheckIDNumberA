package com.kamontat.checkidnumber.model.strategy;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author kamontat
 * @version 1.0
 * @since Wed 17/May/2017 - 11:10 PM
 */
public class ThailandIDNumberStrategyTest {
	IDNumberStrategy strategy;
	
	@Before
	public void setUp() throws Exception {
		strategy = new ThailandIDNumberStrategy();
	}
	
	@Test
	public void getLengthOfThaiID() throws Exception {
		Assert.assertEquals(13, strategy.getIDLength());
	}
	
	@Test
	public void shouldOK() throws Exception {
		Status s = strategy.checking("1100600361966");
		Assert.assertEquals(s, Status.OK);
	}
	
	@Test
	public void shouldNotCreate() throws Exception {
		Status s = strategy.checking("");
		Assert.assertEquals(s, Status.NOT_CREATE);
		
		Status ss = strategy.checking(null);
		Assert.assertEquals(s, Status.NOT_CREATE);
	}
	
	@Test
	public void shouldNot_create() throws Exception {
		Status s = strategy.checking("4839586954321");
		Assert.assertEquals(s, Status.UNCORRECTED);
	}
}