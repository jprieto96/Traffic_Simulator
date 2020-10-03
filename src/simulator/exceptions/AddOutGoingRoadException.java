package simulator.exceptions;

public class AddOutGoingRoadException extends Exception {

	private static final long serialVersionUID = 9077534245774347726L;
	
	public AddOutGoingRoadException() { super(); }
	public AddOutGoingRoadException(String message){ super(message); }
	public AddOutGoingRoadException(String message, Throwable cause) { super(message, cause); }

}
