package Model;

import java.util.Date;
import java.util.Scanner;

public class Income extends Transaction {

    public Income(int id, String description, double amount, Date date) {
        super(id, description, amount, date);
    }

    public Income(double amount, String description) {
        super(amount, description);
    }

    public Income() {
        super();
    }

    public void inputIncomeDetails(Scanner in) {
        System.out.print("Enter amount: ");
        this.amount = in.nextDouble();
        in.nextLine();
        System.out.print("Enter description: ");
        this.description = in.nextLine();
    }

    @Override
    public void execute(Wallet wallet) {
        wallet.deposit(getAmount());
        System.out.println("Model.Income of " + getAmount() + "RY" + " (" + getDescription() + "RY" + ") added successfully.");
        System.out.println("Current Balance: " + wallet.getBalance() +  "RY");
    }
}
