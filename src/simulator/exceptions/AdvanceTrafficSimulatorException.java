package simulator.exceptions;

public class AdvanceTrafficSimulatorException extends Exception {

	private static final long serialVersionUID = -7552740146935217855L;
	
	public AdvanceTrafficSimulatorException() { super(); }
	public AdvanceTrafficSimulatorException(String message){ super(message); }
	public AdvanceTrafficSimulatorException(String message, Throwable cause) { super(message, cause); }

}
