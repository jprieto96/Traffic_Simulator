package simulator.exceptions;

public class AdvanceJunctionException extends Exception {

	private static final long serialVersionUID = 3854035541714822100L;
	
	public AdvanceJunctionException() { super(); }
	public AdvanceJunctionException(String message){ super(message); }
	public AdvanceJunctionException(String message, Throwable cause) { super(message, cause); }


}
