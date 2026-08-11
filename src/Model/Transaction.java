package Model;

import java.util.Date;

public abstract class Transaction {
    protected int id;
    protected String description;
    protected double amount;
    protected Date date;

    // constructors
    public Transaction(int id, String description, double amount, Date date) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    public Transaction() {
        this.date = new Date();
    }

    public Transaction(double amount, String description) {
        this.amount = amount;
        this.description = description;
        this.date = new Date();
    }

    // setters and getters
    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    // methods
    public void printDetails() {
        System.out.println("Model.Transaction [ID=" + id + ", Description='"
                + description + "', Amount=" + amount + ", Date=" + date + "]");
    }

    public abstract void execute(Wallet wallet);

    @Override
    public String toString() {
        return "Model.Transaction{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                '}';
    }
}

