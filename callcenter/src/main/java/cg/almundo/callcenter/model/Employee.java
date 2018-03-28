package cg.almundo.callcenter.model;

import java.util.concurrent.TimeUnit;

/**
 * Employee represents an employee person in the call center, people who attends the {@link Call}
 * @author CarlosGonzalez
 *
 */
public class Employee {
	
	/**
	 * Identifier for any Employee
	 */
	private String id;
	
	/**
	 * State of the Employee InCall (attending a {@link Call}) or Free
	 */
	private boolean InCall;
	
	/**
	 * Employee Type/Role in the Company {@link EmployeeType}
	 */
	private EmployeeType Type;

	
	/**
	 * Create an Employee
	 * @param id Employee identifier
	 * @param type Role/Type {@link EmployeeType} 
	 */
	public Employee(String id, EmployeeType type) {
		super();
		this.id = id;
		InCall = false;
		Type = type;
	}

	/**
	 * Getter id
	 * @return id for the employee
	 */
	public String getId() {
		return id;
	}

	/**
	 * Setter id
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Getter Employee state, is in a call 
	 * @return
	 */
	public synchronized boolean isInCall() {
		return InCall;
	}

	/**
	 * Setter Employee state
	 * @param inCall
	 */
	public synchronized void setInCall(boolean inCall) {
		InCall = inCall;
	}
	
	/**
	 * Getter type {@link EmployeeType}
	 * @return
	 */
	public EmployeeType getType() {
		return Type;
	}

	/**
	 * Setter type {@link EmployeeType}
	 * @param type
	 */
	public void setType(EmployeeType type) {
		Type = type;
	}
	
	/**
	 * The Employee attends a {@link call}, 
	 * He/She locks the state for another call, get the call in a new thread of process and finally returns to a free state
	 * @param call {@link Call}
	 */
	public void attendCall(Call call) {
		setInCall(true);
		Runnable attention = () -> {
			try {
				call.setAttendedBy(this);
				System.out.println("Attending call "+ call.getId() + " for " + call.getDuration() + " secs, by " +  getId() + " / " + getType());
				TimeUnit.SECONDS.sleep(call.getDuration());
			} catch (InterruptedException e) {
				System.out.println(e);
			} finally {
				setInCall(false);
			}	
		};
		
		Thread thread = new Thread(attention);
		thread.start();
	}
}
