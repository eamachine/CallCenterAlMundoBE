package cg.almundo.callcenter.model;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

import cg.almundo.callcenter.model.Call;

public class CallTest {

	/**
	 * It test the creation of a call with the time between the parameters
	 */
	@Test
	public void createCall() {
		new Call("1", 7);
	}
	
	/**
	 * It test the creation a call with a random time between the min and max time
	 */
	@Test
	public void createCallRandomDuration() {
		Call call = new Call("1");
		assertTrue(Call.MAX_DURATION_TIME >= call.getDuration() && Call.MIN_DURATION_TIME <= call.getDuration());
	}
	
	/**
	 * Fail test, try to create a call with a time duration less than the min
	 */
	@Test(expected = IllegalArgumentException.class)
	public void lessThanMinimunTimeDuration() {
		new Call("1", 0);
	}
	
	/**
	 * Fail Test, try to create a call with the time duration higher than max
	 */
	@Test(expected = IllegalArgumentException.class)
	public void moreThanMaximunTimeDuration() {
		new Call("1", 15);
	}
}
