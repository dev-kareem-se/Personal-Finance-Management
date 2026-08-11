package Model;

import java.util.Scanner;
public class User {
    Scanner in = new Scanner(System.in);
    private Wallet wallet;
    private String firstName;
    private String lastName;
    private String gender;
    private int age;

    public User(String firstName, String lastName, String gender, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
        this.wallet = new Wallet();
    }

    public User() {
        this.wallet = new Wallet();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void inputUserName() {
        System.out.print("First Name: ");
        this.firstName = in.nextLine();
        System.out.print("Last Name: ");
        this.lastName = in.nextLine();
    }

    public void inputUserGender() {
        System.out.println("Gender: ");
        System.out.println("1. Male");
        System.out.println("2. Female");
        System.out.println("3. Other");
        System.out.print("Choose (1-3): ");
        String choice = in.nextLine();
        switch (choice) {
            case "1":
                this.gender = "Male";
                break;
            case "2":
                this.gender = "Female";
                break;
            case "3":
                this.gender = "Other";
                break;
            default:
                this.gender = "Unspecified";
                System.out.println("Invalid choice. Gender set to Unspecified.");
        }
    }

    public void inputUserAge() {
        System.out.print("Age: ");
        this.age = in.nextInt();
        in.nextLine();
    }

    public void profileDisplay() {
        System.out.println("--- Model.User Profile ---");
        System.out.println("Name: " + firstName + " " + lastName);
        System.out.println("Gender: " + gender);
        System.out.println("Age: " + age);
        if (wallet != null) {
            System.out.println("Model.Wallet Balance: " + wallet.getBalance() + "RY");
        }
    }
}
