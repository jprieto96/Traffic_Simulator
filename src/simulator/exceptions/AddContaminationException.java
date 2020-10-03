package simulator.exceptions;

public class AddContaminationException extends Exception {

	private static final long serialVersionUID = -6914106678673505635L;
	
	public AddContaminationException() { super(); }
	public AddContaminationException(String message){ super(message); }
	public AddContaminationException(String message, Throwable cause) { super(message, cause); }

}
