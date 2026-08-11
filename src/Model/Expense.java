package Model;

import java.util.Date;
import java.util.Scanner;

public class Expense extends Transaction {

    public Expense(int id, String description, double amount, Date date) {
        super(id, description, amount, date);
    }

    public Expense(double amount, String description) {
        super(amount, description);
    }

    public Expense() {
        super();
    }

    public void inputExpenseDetails(Scanner in) {
        System.out.print("Enter amount: ");
        this.amount = in.nextDouble();
        in.nextLine();
        System.out.print("Enter description: ");
        this.description = in.nextLine();
    }

    @Override
    public void execute(Wallet wallet) {
        if (wallet.withdraw(getAmount())) {
            System.out.println("Model.Expense of " + getAmount() + "RY"
                    + " (" + getDescription() + ") added successfully.");
        } else {
            System.out.println("Model.Transaction Failed: Low balance. Required "
                    + getAmount() + "RY" + " but available is " + wallet.getBalance() + "RY");
        }
    }
}
