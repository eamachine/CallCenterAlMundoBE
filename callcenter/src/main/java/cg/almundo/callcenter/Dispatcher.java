package cg.almundo.callcenter;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import cg.almundo.callcenter.model.Call;
import cg.almundo.callcenter.model.Employee;

/**
 * Dispatcher is the class responsible to manage the list of {@link Employee} and receive {@link Calls}
 * it manages the incoming calls, save it for attention in a Queue, keep them to process according to the employees availability
 * it assigns a call to an employee to respond it
 * @author CarlosGonzalez
 *
 */
public class Dispatcher implements Runnable {
	
	/** list of {@link Employee}, who receive the calls  */
	private List<Employee> employees;
	  
	/** Queue for calls, it lets keep the calls when there is not an available employee  */
	private ConcurrentLinkedQueue<Call> calls;
	
	
	/**
	 * The dispatcher is created with the list of employees to manage 
	 * @param employees
	 */
	public Dispatcher(List<Employee> employees) {
		this.employees = employees;
		calls = new ConcurrentLinkedQueue<Call>();
	}

	/**
	 * Method of Runnable, it lets dispatch call be called for more than a thread  
	 */
	public void run() {
		do {
			dispatchCall();	
		} while(true);
	}
	
	/**
	 * Method which process a call
	 * first the queue of calls is checked 
	 * then it looks for an employee ready to take the call, it is executed by the stream bellow, 
	 * it orders the list of employees by {@link EmployeeType}, setting Operators, Supervisor and Director, in order
	 * so, it filters the free employees and get first available 
	 * if an employee was found the employee is call to attend(respond) the call
	 */
	public synchronized void dispatchCall() {
		if(!calls.isEmpty()) {
			Employee employee = employees.stream().sorted(Comparator.comparing(Employee::getType)).filter(e -> !e.isInCall()).findFirst().orElse(null);
			
			if(employee != null) {
				System.out.println("runinng: " + Thread.currentThread().getName());
				employee.attendCall(calls.poll());
			}
		}
	}
	
	/**
	 * this method add a new call to be processed 
	 * @param call new brand call
	 */
	public void recieveCall(Call call) {
		System.out.println("Incoming call " + call.getId() + " " + call.getDuration());
		calls.add(call);
	}
}
