package tnsif.c2tc.java.ExceptionHandling;

//Thrown when withdrawal amount exceeds balance
public class InsufficientFundsException extends Exception {

	// Constructor
	public InsufficientFundsException(String message) {
		super(message);
	}
}
