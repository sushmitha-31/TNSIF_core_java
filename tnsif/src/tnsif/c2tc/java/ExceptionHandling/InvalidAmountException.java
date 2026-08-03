package tnsif.c2tc.java.ExceptionHandling;

//Thrown for invalid deposit or withdrawal amounts.
public class InvalidAmountException extends Exception {

	// Constructor
	public InvalidAmountException(String message) {
		super(message);
	}
}
