package tnsif.c2tc.java.polymorphism;
import java.util.Scanner;

class TicketBooking {

    private String stageEvent;
    private String customer;
    private int noOfSeats;

    public TicketBooking() {
    }

    public TicketBooking(String stageEvent, String customer, int noOfSeats) {
        this.stageEvent = stageEvent;
        this.customer = customer;
        this.noOfSeats = noOfSeats;
    }

    public String getStageEvent() {
		return stageEvent;
	}

	public void setStageEvent(String stageEvent) {
		this.stageEvent = stageEvent;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public int getNoOfSeats() {
		return noOfSeats;
	}

	public void setNoOfSeats(int noOfSeats) {
		this.noOfSeats = noOfSeats;
	}

	// Cash Payment
    public void makePayment(Double amount) {
        System.out.println("Stage event:" + stageEvent);
        System.out.println("Customer:" + customer);
        System.out.println("Number of seats:" + noOfSeats);
        System.out.printf("Amount %.1f paid in cash", amount);
    }

    // Wallet Payment
    public void makePayment(String walletNumber, Double amount) {
        System.out.println("Stage event:" + stageEvent);
        System.out.println("Customer:" + customer);
        System.out.println("Number of seats:" + noOfSeats);
        System.out.printf("Amount %.1f paid using wallet number %s", amount, walletNumber);
    }

    // Credit Card Payment
    public void makePayment(String creditCard, String ccv, String name, Double amount) {
        System.out.println("Stage event:" + stageEvent);
        System.out.println("Customer:" + customer);
        System.out.println("Number of seats:" + noOfSeats);
        System.out.println("Holder name:" + name);
        System.out.printf("Amount %.1f paid using %s card%n", amount, creditCard);
        System.out.println("CCV:" + ccv);
    }
}

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        String input = sc.nextLine();
        String[] details = input.split(",");

        TicketBooking tb = new TicketBooking(details[0],details[1],Integer.parseInt(details[2]));

        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {

            case 1:
                double amount = sc.nextDouble();
                tb.makePayment(amount);
                break;

            case 2:
                double walletAmount = sc.nextDouble();
                sc.nextLine();
                String walletNumber = sc.nextLine();
                tb.makePayment(walletNumber, walletAmount);
                break;

            case 3:
                String holderName = sc.nextLine();
                double cardAmount = sc.nextDouble();
                sc.nextLine();
                String cardType = sc.nextLine();
                String ccv = sc.nextLine();
                tb.makePayment(cardType, ccv, holderName, cardAmount);
                break;

            default:
                System.out.println("Invalid choice");
        }

        sc.close();
    }
}