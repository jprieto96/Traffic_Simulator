package simulator.exceptions;

public class EventExecuteException extends Exception {

	private static final long serialVersionUID = -3712536688243922706L;
	
	public EventExecuteException() { super(); }
	public EventExecuteException(String message){ super(message); }
	public EventExecuteException(String message, Throwable cause) { super(message, cause); }

}
