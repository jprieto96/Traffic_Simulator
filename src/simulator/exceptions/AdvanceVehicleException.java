package simulator.exceptions;

public class AdvanceVehicleException extends Exception {

	private static final long serialVersionUID = -7043293003945599673L;
	
	public AdvanceVehicleException() { super(); }
	public AdvanceVehicleException(String message){ super(message); }
	public AdvanceVehicleException(String message, Throwable cause) { super(message, cause); }

}
