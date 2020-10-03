package simulator.exceptions;

public class VehicleEnterException extends Exception {

	private static final long serialVersionUID = 7142047976763638280L;
	
	public VehicleEnterException() { super(); }
	public VehicleEnterException(String message){ super(message); }
	public VehicleEnterException(String message, Throwable cause) { super(message, cause); }

}
