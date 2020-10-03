package simulator.exceptions;

public class ArgumentsException extends Exception {

	private static final long serialVersionUID = -2669133645661943584L;
	
	public ArgumentsException() { super(); }
	public ArgumentsException(String message){ super(message); }
	public ArgumentsException(String message, Throwable cause) { super(message, cause); }

}
