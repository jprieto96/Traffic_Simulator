package simulator.exceptions;

public class VehicleStatusException extends Exception {

	private static final long serialVersionUID = -5933067093001154556L;
	
	public VehicleStatusException() { super(); }
	public VehicleStatusException(String message){ super(message); }
	public VehicleStatusException(String message, Throwable cause) { super(message, cause); }

}
