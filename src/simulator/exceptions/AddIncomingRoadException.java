package simulator.exceptions;

public class AddIncomingRoadException extends Exception {

	private static final long serialVersionUID = 2371674214212045234L;
	
	public AddIncomingRoadException() { super(); }
	public AddIncomingRoadException(String message){ super(message); }
	public AddIncomingRoadException(String message, Throwable cause) { super(message, cause); }

}
