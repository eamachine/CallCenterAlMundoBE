package cg.almundo.callcenter.model;

import java.util.Random;

/**
 * Call object represents a call received in the call center
 * @author CarlosGonzalez
 *
 */
public class Call {
	
	/** Minimum time in a call  */
	public static final int MIN_DURATION_TIME = 5;
	
	/** Maximum time in a call  */
	public static final int MAX_DURATION_TIME = 10;
	
	/** Identifier for a Call*/
	private String id;
	
	/** Time duration in seconds*/
	private int duration;
	
	/** Employee Responsible for the attention*/
	private Employee attendedBy;
	
	/**
	 * Call constructor, setting duration
	 * Basic constructor 
	 * @param id
	 * @param durationSec
	 */
	public Call(String id, int duration) {
		if(duration < MIN_DURATION_TIME || duration > MAX_DURATION_TIME) 
			throw new IllegalArgumentException();
		this.id = id;
		this.duration = duration;
	}
	
	/**
	 * Call constructor, setting a random duration between the min and max limits
	 * @param id
	 */
	public Call(String id) {
		Random r = new Random();
		this.id = id;
		this.duration = r.nextInt(MAX_DURATION_TIME-MIN_DURATION_TIME) + MIN_DURATION_TIME;
	}

	/**
	 * Getter Id
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * Setter Id
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Getter Duration
	 * @return int seconds
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * Setter duration
	 * @param duration int seconds 
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * Getter {@link Employee} responsible for the attention
	 * @return
	 */
	public Employee getAttendedBy() {
		return attendedBy;
	}

	/**
	 * Setter {@link Employee} responsible for the attention
	 * @param attendedBy
	 */
	public void setAttendedBy(Employee attendedBy) {
		this.attendedBy = attendedBy;
	}
}
