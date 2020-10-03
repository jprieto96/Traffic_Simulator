package simulator.exceptions;

public class MoveToNextRoadException extends Exception {

	private static final long serialVersionUID = -5297237741865155906L;
	
	public MoveToNextRoadException() { super(); }
	public MoveToNextRoadException(String message){ super(message); }
	public MoveToNextRoadException(String message, Throwable cause) { super(message, cause); }


}
