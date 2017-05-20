package com.kamontat.checkidnumber.model;

import com.kamontat.checkidnumber.MainJunitTest;
import com.kamontat.checkidnumber.api.constants.Status;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author kamontat
 * @version 1.0
 * @since Mon 15/May/2017 - 1:55 AM
 */
public class IDNumberTest {
	@Test
	public void shouldOK() {
		IDNumber id = new IDNumber(MainJunitTest.VALIDATE_ID);
		Assert.assertEquals(Status.OK, id.getStatus());
	}
	
	@Test
	public void shouldUnmatched() {
		IDNumber id1 = new IDNumber(MainJunitTest.INVALIDATE_ID_UNMATCHED_LENGTH_UPPER_BOUND);
		Assert.assertEquals(Status.UNMATCHED_LENGTH, id1.getStatus());
		IDNumber id2 = new IDNumber(MainJunitTest.INVALIDATE_ID_UNMATCHED_LENGTH_LOWER_BOUND);
		Assert.assertEquals(Status.UNMATCHED_LENGTH, id2.getStatus());
	}
	
	@Test
	public void shouldNotCreate() {
		IDNumber id = new IDNumber();
		Assert.assertEquals(Status.NOT_CREATE, id.getStatus());
	}
	
	@Test
	public void firstCharCannotBeNine() {
		IDNumber id1 = new IDNumber(MainJunitTest.INVALIDATE_ID_NOT_NINE_LOWER_BOUND);
		Assert.assertEquals(Status.NOT_NINE, id1.getStatus());
		
		IDNumber id2 = new IDNumber(MainJunitTest.INVALIDATE_ID_NOT_NINE_NORMAL_BOUND);
		Assert.assertEquals(Status.NOT_NINE, id2.getStatus());
		
		IDNumber id3 = new IDNumber(MainJunitTest.INVALIDATE_ID_NOT_NINE_UPPER_BOUND);
		Assert.assertEquals(Status.NOT_NINE, id3.getStatus());
	}
	
	@Test
	public void shouldUnCorrect() {
		IDNumber id = new IDNumber(MainJunitTest.INVALIDATE_ID_UNCORRECTED);
		Assert.assertEquals(Status.UNCORRECTED, id.getStatus());
	}
	
	@Test
	public void shouldCheckWhenSetNewID() {
		IDNumber id = new IDNumber(MainJunitTest.VALIDATE_ID);
		Assert.assertEquals(Status.OK, id.getStatus());
		id.setId("1000");
		Assert.assertEquals(Status.UNMATCHED_LENGTH, id.getStatus());
		id.setId("9291823347812");
		Assert.assertEquals(Status.NOT_NINE, id.getStatus());
	}
	
	@Test
	public void shouldEqual() {
		IDNumber id1 = new IDNumber("1");
		IDNumber id2 = new IDNumber("1");
		IDNumber id3 = new IDNumber("2");
		
		Assert.assertTrue(id1.equals(id2));
		Assert.assertFalse(id1.equals(id3));
	}
}