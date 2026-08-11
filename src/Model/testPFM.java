package Model;

import java.util.Scanner;

public class testPFM {
    public static void main(String... args) {
        Scanner in = new Scanner(System.in);
        
        User user = new User();
        user.inputUserName();
        user.inputUserGender();
        user.inputUserAge();

        Wallet wallet = user.getWallet();

        System.out.println("=== Personal Finance Manager ===");
        user.profileDisplay();
        System.out.println("--------------------------------");

        boolean running = true;
        while (running) {
            System.out.println("\nSelect Operation:");
            System.out.println("1. Add Model.Income");
            System.out.println("2. Add Model.Expense");
            System.out.println("3. View Balance & History");
            System.out.println("4. Exit");
            System.out.print("Choice: ");

            int operation = in.nextInt();

            if (operation == 1 || operation == 2) {
                System.out.print("Enter amount: ");
                double amount = in.nextDouble();
                in.nextLine();

                System.out.print("Enter description: ");
                String description = in.nextLine();

                if (operation == 1) {
                    Income income = new Income(amount, description);
                    wallet.addTransaction(income);
                } else {
                    Expense expense = new Expense(amount, description);
                    wallet.addTransaction(expense);
                }
            } else if (operation == 3) {
                System.out.println("\n--- Model.Wallet Summary ---");
                System.out.println("Current Balance: " + wallet.getBalance() + "RY");
                System.out.println("Model.Transaction History:");
                if (wallet.getTransactions().isEmpty()) {
                    System.out.println("  No transactions recorded.");
                } else {
                    for (Transaction t : wallet.getTransactions()) {
                        System.out.println("  " + t);
                    }
                }
            } else if (operation == 4) {
                running = false;
                System.out.println("Exiting Personal Finance Manager. Goodbye!");
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}