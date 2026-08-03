package tnsif.c2tc.java.ExceptionHandling;

//Custom exception improve reliability by handling invalid transactions separately.
public class BankingSystem {
	public static void main(String[] args) {

		// Create a bank account object.
		BankAccount account = new BankAccount(101, 5000);

		try {
			account.displayBalance();
			account.deposit(2000);
			account.withdraw(1000);

			// This will throw an exception.
			account.withdraw(8000);

		} catch (InvalidAmountException e) {
			System.out.println("Invalid amount: " + e.getMessage());
		} catch (InsufficientFundsException e) {
			System.out.println("Error: " + e.getMessage());
		} finally {
			// Executes regardless of exception.
			System.out.println("Transcation Completed.");
			account.displayBalance();
		}
	}

}
