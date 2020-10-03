package simulator.exceptions;

public class SetWeatherException extends Exception {

	private static final long serialVersionUID = 7154469710626064138L;
	
	public SetWeatherException() { super(); }
	public SetWeatherException(String message){ super(message); }
	public SetWeatherException(String message, Throwable cause) { super(message, cause); }

}
