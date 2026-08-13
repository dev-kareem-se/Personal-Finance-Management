package View;

import Model.Expense;
import Model.Income;
import Model.User;

import javax.swing.*;
import java.awt.*;

/**
 * Subpage : JDialog
*/
public class AddTransactionDialog extends JDialog {

    private JTextField amountField;
    private JTextField descriptionField;
    private JLabel typeLabel;
    private String transactionType;  // "Income" Or "Expense"
    private User user;
    private DashboardFrame dashboard;

    // Constructor : Specific To JDialog
    public AddTransactionDialog(JFrame parent, String type,
                                 User user, DashboardFrame dashboard) {
        // Super(parent, title, modal)
        // Modal = True > Prevent Interaction With Main Page
        super(parent, (type.equals("Income") ? "➕ Add Income" : "➖ Add Expense"),true);

        this.transactionType = type;
        this.user = user;
        this.dashboard = dashboard;

        initUI();
    }

    private void initUI() {
        setSize(380, 280);
        setLocationRelativeTo(getOwner()); // In the middle for Main Page : Dashboard
        setResizable(false);

        Color themeColor = transactionType.equals("Income")
                ? new Color(0, 150, 80)   //  Green for Income
                : new Color(200, 40, 40); //  Red for Expense

        // Subpage Panel
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 25));
        panel.setBackground(Color.WHITE);

        // Colorful Bar To Specify Type Of Transaction
        JPanel typeBar = new JPanel();
        typeBar.setBackground(themeColor);
        typeBar.setPreferredSize(new Dimension(0, 8));
        panel.add(typeBar, BorderLayout.NORTH);

        // Income layout
        // We Use GridBagLayout Because Work With Rows & Columns
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        // Allow Expansion Component
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // Spaces Around Component
        gbc.insets = new Insets(7, 5, 7, 5);

        // gridx : column , gridy : row , widthx : lable size
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.35;
        form.add(makeLabel("Amount (RY):"), gbc);

        //widthx : text field > 0.35 + 0.65 = 1
        gbc.gridx = 1; gbc.weightx = 0.65;
        amountField = new JTextField();
        styleField(amountField);
        form.add(amountField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.35;
        form.add(makeLabel("Description:"), gbc);

        gbc.gridx = 1; gbc.weightx = 0.65;
        descriptionField = new JTextField();
        styleField(descriptionField);
        form.add(descriptionField, gbc);

        // Buttons bar
        // FlowLayout : Make All Components (Buttons) Side By Side
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(Color.WHITE);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cancelBtn.setFocusPainted(false);
        // close JDialog without save
        cancelBtn.addActionListener(e -> dispose());

        JButton saveBtn = new JButton("Save");
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        saveBtn.setBackground(themeColor);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setBorderPainted(false);
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveBtn.addActionListener(e -> handleSave());

        btnPanel.add(cancelBtn);
        btnPanel.add(saveBtn);

        panel.add(form, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        add(panel);
    }

    // HandleSave : Save Transaction Logic
    private void handleSave() {
        // reading values
        String amountText = amountField.getText().trim();
        String description = descriptionField.getText().trim();

        // Validation
        if (amountText.isEmpty() || description.isEmpty()) {
            // Show Error Massage
            JOptionPane.showMessageDialog(this,
                "Please fill in all fields.",
                "Missing Data",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Exception : If User Enter String Not Double
        double amount;
        try {
            amount = Double.parseDouble(amountText);
            if (amount <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid positive number for amount.",
                "Invalid Amount",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create & Add Transaction To Wallet Class
        if (transactionType.equals("Income")) {
            Income income = new Income(amount, description);
            user.getWallet().addTransaction(income);

            JOptionPane.showMessageDialog(this,
                "Income of " + String.format("%.2f RY", amount) + " added!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Validate The Balance Is Lower Than Amount
            if (amount > user.getWallet().getBalance()) {
                JOptionPane.showMessageDialog(this,
                    "Insufficient balance!\n" +
                    "Available: " + String.format("%.2f RY", user.getWallet().getBalance()),
                    "Transaction Failed",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            Expense expense = new Expense(amount, description);
            user.getWallet().addTransaction(expense);

            JOptionPane.showMessageDialog(this,
                "Expense of " + String.format("%.2f RY", amount) + " recorded!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        }

        // Refresh Dashboard Page
        dashboard.refreshUI();

        //  Close JDialog
        dispose();
    }

    // Helper Methods
    // Label Style Method
    private JLabel makeLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(60, 60, 80));
        return label;
    }

    // Field Style Method
    private void styleField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 230), 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
    }
}
