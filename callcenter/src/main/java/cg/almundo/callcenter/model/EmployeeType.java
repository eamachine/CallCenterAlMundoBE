package cg.almundo.callcenter.model;

/**
 * EmployeeType represents the role of the {@link Employee}, the int value represents the level
 * @author CarlosGonzalez
 *
 */
public enum EmployeeType {
	
	/** Operator type, level 1 of attention  */
    OPERATOR(1),
    
    /** Supervisor Type, level 2 of attention */
    SUPERVISOR(2),
    
    /** Director Type, level 3 of attention */
    DIRECTOR(3);

    public final int Value;
    
    /**
     * Value represents the level of service, level 1 Operator, 2 Supervisor, 3 Director
     * @param value
     */
    private EmployeeType(int value)
    {
        Value = value;
    }
}