package cg.almundo.callcenter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import cg.almundo.callcenter.model.*;

/**
 * Unit test Call Center.
 */
public class CallCenterTest
{

	/**
	 * create a list of employees
	 * @param operators number of operators
	 * @param supervisors number of supervisors
	 * @param directors number of directors
	 * @return list of employees
	 */
	private List<Employee> getEmployees(int operators, int supervisors, int directors) {
    	
    	List<Employee> employees = new ArrayList<Employee>();
    	
    	for (int i = 0; i < operators; i++) {

            employees.add(new Employee("Operator_"+i, EmployeeType.OPERATOR));
		}
        
        for (int i = 0; i < supervisors; i++) {

            employees.add(new Employee("Supervisor_"+i, EmployeeType.SUPERVISOR));
		}
        
        for (int i = 0; i < directors; i++) {

            employees.add(new Employee("Director_"+i, EmployeeType.DIRECTOR));
		}
        
    	return employees;
    }
    
	/**
	 * create a list of calls with random duration
	 * @param total number f calls
	 * @return list of calls
	 */
    private List<Call> getCalls(int total) {
    	
    	List<Call> calls = new ArrayList<Call>();
        
        for (int i = 0; i < total; i++) {
        	calls.add(new Call("Call_"+i));
        }
    	
        return calls; 
    }

    /**
     * Fail test, try to create a CallCenter with a null list of employees
     * Create a Call Center
     */
    @Test(expected = IllegalArgumentException.class)
    public void creationCallCenterNoEmployees()
    {
    	CallCenter callCenter = new CallCenter(null);
    }
    
    /**
     * Fail test, try to create a CallCenter with an empty list of employees
     * Create a Call Center
     */
    @Test(expected = IllegalArgumentException.class)
    public void creationCallCenterEmptyEmployees()
    {
    	CallCenter callCenter = new CallCenter(new ArrayList<Employee>());
    }
    
    /**
     * Create a Call Center, assert it is not null
     */
    @Test
    public void creationCallCenter()
    {
    	CallCenter callCenter = new CallCenter(getEmployees(1, 0, 0));
    	assertNotNull(callCenter);
    }
    
    /**
     * it tests the CallCenter process a call, and check it found attention
     */
    @Test
    public void dispatchACall()
    {
    	System.out.println("dispatchACall");
    	List<Call> calls = getCalls(1); 
    	CallCenter callCenter = new CallCenter(getEmployees(1, 0, 0));
        callCenter.initCallCenter();
        
        try {
	        TimeUnit.SECONDS.sleep(5);
	        callCenter.recieveCall(calls.get(0));
	        TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
			System.out.println(e);
			e.printStackTrace();
		}
        
        assertNotNull(calls.get(0).getAttendedBy());
    }
    
    /**
     * It tests the CallCenter process ten calls in line, check if the last one was ok
     */
    @Test
    public void dispatchTenCalls()
    {
    	System.out.println("dispatchTenCalls");
    	List<Call> calls = getCalls(10); 
    	CallCenter callCenter = new CallCenter(getEmployees(10, 5, 3));
        callCenter.initCallCenter();
        
        try {
	        TimeUnit.SECONDS.sleep(5);
	        
	        for (Iterator iterator = calls.iterator(); iterator.hasNext();) {
				Call call = (Call) iterator.next();
				TimeUnit.SECONDS.sleep(1);
		        callCenter.recieveCall(call);
			}
	        
	        TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
			System.out.println(e);
			e.printStackTrace();
		}
        
        assertNotNull(calls.get(9).getAttendedBy());
    }
    
    /**
     * It tests ten concurrent calls, it creates a services executor to call the dispatcher at the same time for the ten calls 
     */
    @Test
    public void dispatchTenConcurrentCalls()
    {
    	System.out.println("dispatchTenConcurrentCalls");
    	List<Call> calls = getCalls(10); 
    	CallCenter callCenter = new CallCenter(getEmployees(10, 5, 3));
        callCenter.initCallCenter();
        
        try {
	        TimeUnit.SECONDS.sleep(5);
	        
	        ExecutorService executor = Executors.newWorkStealingPool();

	        List<Callable<String>> callables = new ArrayList<Callable<String>>();

	        for (Iterator iterator = calls.iterator(); iterator.hasNext();) {
				Call call = (Call) iterator.next();
				callables.add(() -> { 
				callCenter.recieveCall(call);
				return "Processed "+call.getId();
				});
			}

	        executor.invokeAll(callables)
	            .stream()
	            .map(future -> {
	                try {
	                    return future.get();
	                }
	                catch (Exception e) {
	                    throw new IllegalStateException(e);
	                }
	            })
	            .forEach(System.out::println);
	        
	        TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
			System.out.println(e);
			e.printStackTrace();
		}
    }
    
    /**
     * It tests more than ten concurrent calls, it creates a services executor to call the dispatcher at the same time for twenty calls 
     */
    @Test
    public void dispatchTwentyConcurrentCalls()
    {
    	System.out.println("dispatchTwentyConcurrentCalls");
    	List<Call> calls = getCalls(20); 
    	CallCenter callCenter = new CallCenter(getEmployees(5, 5, 3));
        callCenter.initCallCenter();
        
        try {
	        TimeUnit.SECONDS.sleep(5);
	        
	        ExecutorService executor = Executors.newWorkStealingPool();

	        List<Callable<String>> callables = new ArrayList<Callable<String>>();

	        for (Iterator iterator = calls.iterator(); iterator.hasNext();) {
				Call call = (Call) iterator.next();
				callables.add(() -> { 
				callCenter.recieveCall(call);
				return "Processed "+call.getId();
				});
			}

	        executor.invokeAll(callables)
	            .stream()
	            .map(future -> {
	                try {
	                    return future.get();
	                }
	                catch (Exception e) {
	                    throw new IllegalStateException(e);
	                }
	            })
	            .forEach(System.out::println);
	        
	        TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
			System.out.println(e);
			e.printStackTrace();
		}
    }
    
    /**
     * It tests that the call was responded by an operator
     */
    @Test
    public void levelOperatorAttentionCall()
    {
    	System.out.println("levelOperatorAttentionCall");
    	List<Call> calls = getCalls(5); 
    	CallCenter callCenter = new CallCenter(getEmployees(6, 1, 1));
        callCenter.initCallCenter();
        
        try {
	        TimeUnit.SECONDS.sleep(5);
	        
	        for (Iterator iterator = calls.iterator(); iterator.hasNext();) {
				Call call = (Call) iterator.next();
		        callCenter.recieveCall(call);
			}
	        
	        TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
			System.out.println(e);
			e.printStackTrace();
		}
        
        assertTrue(calls.get(4).getAttendedBy().getType().equals(EmployeeType.OPERATOR));
    }
    
    /**
     * It tests that the call was responded by a supervisor
     * it makes calls to occupy the operator
     */
    @Test
    public void levelSupervisorAttentionCall()
    {
    	System.out.println("levelSupervisorAttentionCall");
    	List<Call> calls = getCalls(5); 
    	CallCenter callCenter = new CallCenter(getEmployees(4, 1, 1));
        callCenter.initCallCenter();
        
        try {
	        TimeUnit.SECONDS.sleep(5);
	        
	        for (Iterator iterator = calls.iterator(); iterator.hasNext();) {
				Call call = (Call) iterator.next();
		        callCenter.recieveCall(call);
			}
	        
	        TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
			System.out.println(e);
			e.printStackTrace();
		}
        
        assertTrue(calls.get(4).getAttendedBy().getType().equals(EmployeeType.SUPERVISOR));
    }
    
    /**
     * It tests that the call was responded by a director
     * it makes calls to occupy the operators
     * it makes calls to occupy the supervisors 
     */
    @Test
    public void levelDirectorAttentionCall()
    {
    	System.out.println("levelDirectorAttentionCall");
    	List<Call> calls = getCalls(5); 
    	CallCenter callCenter = new CallCenter(getEmployees(3, 1, 1));
        callCenter.initCallCenter();
        
        try {
	        TimeUnit.SECONDS.sleep(5);
	        
	        for (Iterator iterator = calls.iterator(); iterator.hasNext();) {
				Call call = (Call) iterator.next();
		        callCenter.recieveCall(call);
			}
	        
	        TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
			System.out.println(e);
			e.printStackTrace();
		}
        
        assertTrue(calls.get(4).getAttendedBy().getType().equals(EmployeeType.DIRECTOR));
    }
}
