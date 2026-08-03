package tnsif.c2tc.java.ExceptionHandling;

//Represents a bank account
public class BankAccount {

	private int accountNumber;
	private double balance;

	// Initialize account details.
	public BankAccount(int accountNumber, double balance) {
		this.accountNumber = accountNumber;
		this.balance = balance;
	}

	// Deposits money into the account.
	public void deposit(double amount) throws InvalidAmountException {
		if (amount <= 0) {
			throw new InvalidAmountException("Deposit amount must be greater than zero.");
		}

		balance += amount;
		System.out.println("Amount Deposited: " + amount);
	}

	// Withdraws money from the account.
	public void withdraw(double amount) throws InvalidAmountException, InsufficientFundsException {
		if (amount <= 0) {
			throw new InvalidAmountException("Withdrawal amount must be greater than zero.");
		}

		if (amount > balance) {
			throw new InsufficientFundsException("Insufficient balance.");
		}

		balance -= amount;
		System.out.println("Amount Withdrawn: " + amount);
	}

	// Displays account details.
	public void displayBalance() {
		System.out.println("Account Number: " + accountNumber);
		System.out.println("Current Balance: " + balance);
	}
}
