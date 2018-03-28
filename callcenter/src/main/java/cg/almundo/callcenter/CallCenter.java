package cg.almundo.callcenter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import cg.almundo.callcenter.model.*;

/**
 * CallCenter class represents the manage of the call center, it creates and initialize the instances to process calls
 * @author CarlosGonzalez
 *
 */
public class CallCenter 
{

	/** Call capacity thread minimum*/
	private static final int CONCURRENT_CALLS = 10;
	
	/** Dispatcher object to process call*/
	private Dispatcher dispatcher;
	
	/** ExecutorService manages the threads for dispatcher and supports the concurrency*/
	private ExecutorService executorService;
	
	/**
	 * It creates the dispatcher element base on employees
	 * @param employees
	 */
	public CallCenter(List<Employee> employees) {
		if(employees == null || employees.isEmpty())
			throw new IllegalArgumentException();
		
		dispatcher = new Dispatcher(employees);
	}
	
	/**
	 * it controls the creation for the dispatch thread instances
	 * the creation of a new theradPool with a minimum of threads guarantees a pool of at least CONCURRENT_CALLS threads
	 * the class newScheduledThreadPool also lets the creation for more threads if it is necessary, it gives a solution for a higher concurrency
	 */
	public void initCallCenter() {
		try {
			executorService = Executors.newScheduledThreadPool(CONCURRENT_CALLS);
	
			for (int i = 0; i < CONCURRENT_CALLS; i++) {
				executorService.submit(dispatcher);
			}
			
			/*wait for initialization*/
			TimeUnit.SECONDS.sleep(1);
			
		} catch (InterruptedException e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
	
	/**
	 * it exposes a method to receive a call
	 * @param call
	 */
	public void recieveCall(Call call) {
		dispatcher.recieveCall(call);
	}
	
	/**
	 * Basic execution
	 * @param args
	 */
	public static void main( String[] args )
    {
        System.out.println( "Al Mundo Call Center" );
        
        List<Employee> employees = new ArrayList<Employee>();
        
        for (int i = 0; i < 3; i++) {
            employees.add(new Employee("Operator_"+i, EmployeeType.OPERATOR));
		}
        
        for (int i = 0; i < 2; i++) {
            employees.add(new Employee("Supervisor_"+i, EmployeeType.SUPERVISOR));
		}
        
        for (int i = 0; i < 1; i++) {
            employees.add(new Employee("Director_"+i, EmployeeType.DIRECTOR));
		}
        
        List<Call> calls = new ArrayList<Call>();
        
        for (int i = 0; i < 1000; i++) {
        	calls.add(new Call("Call_"+i));
        }
        
        CallCenter callcenter = new CallCenter(employees);
        
        callcenter.initCallCenter();
        
        try {
	        for (Iterator iterator = calls.iterator(); iterator.hasNext();) {
				Call call = (Call) iterator.next();	
				TimeUnit.SECONDS.sleep(1);				
				callcenter.recieveCall(call);
			}
        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
