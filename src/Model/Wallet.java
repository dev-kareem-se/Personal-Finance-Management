package Model;

import java.util.ArrayList;

public class Wallet {
    private double balance;
    private ArrayList<Transaction> transactions = new ArrayList<>();

    public Wallet(double balance, ArrayList<Transaction> transactions) {
        this.balance = balance;
        this.transactions = transactions;
    }

    public Wallet(double balance) {
        this.balance = balance;
    }

    public Wallet() {
        this.balance = 0.0;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        } else {
            return false;
        }
    }

    public double showBalance() {
        System.out.println("Current Balance: " + balance + "RY");
        return balance;
    }

    public void addTransaction(Transaction transaction) {
        double balanceBefore = balance;
        transaction.execute(this);
        
        if (transaction instanceof Expense && balance == balanceBefore) {
            return;
        }
        transactions.add(transaction);
    }
}
