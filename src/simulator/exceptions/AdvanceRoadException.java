package simulator.exceptions;

public class AdvanceRoadException extends Exception {

	private static final long serialVersionUID = 2736314222701933405L;
	
	public AdvanceRoadException() { super(); }
	public AdvanceRoadException(String message){ super(message); }
	public AdvanceRoadException(String message, Throwable cause) { super(message, cause); }

}
