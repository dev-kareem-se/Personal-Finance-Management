package View;

import Model.Transaction;
import Model.User;
import Model.Income;
import Model.Expense;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;// --
import javax.swing.table.DefaultTableCellRenderer;// --
import java.awt.*;
import java.text.SimpleDateFormat;

/**
 * Main Page : JFrame
 * */
public class DashboardFrame extends JFrame {

    // UI component
    private User user;
    private JLabel balanceLabel;       // show balance
    private JLabel welcomeLabel;       // welcome massage
    private DefaultTableModel tableModel; // control the table: Table > Date
    private JTable transactionTable;   // transaction table
    // initialize Date variable
    private static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd HH:mm");


    // Constructor
    public DashboardFrame(User user) {
        this.user = user;
        initUI(); //call bellow method
    }

    private void initUI() {
        // page components
        setTitle("Personal Finance Manager");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //  Layout Manager :> BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(new Color(240, 245, 255));

        //JPanel components
        // (Header)
        mainPanel.add(buildHeader(), BorderLayout.NORTH);

        // (Content)
        mainPanel.add(buildContent(), BorderLayout.CENTER);

        // (Button bar)
        mainPanel.add(buildButtonBar(), BorderLayout.SOUTH);
        // add mainPanel to JFrame
        add(mainPanel);
    }

    // building header method
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(25, 70, 150));
        header.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        // Name
        welcomeLabel = new JLabel(" Welcome, " + user.getFirstName() + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);

        // Balance                                                                               //text algin : RIGHT
        balanceLabel = new JLabel("Balance: " + formatAmount(user.getWallet().getBalance()), SwingConstants.RIGHT);
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        balanceLabel.setForeground(new Color(100, 255, 150));

        header.add(welcomeLabel, BorderLayout.WEST);
        header.add(balanceLabel, BorderLayout.EAST);
        return header;
    }

    // building content method
    private JPanel buildContent() {
        JPanel content = new JPanel(new BorderLayout(0, 10));
        content.setBackground(new Color(240, 245, 255));
        content.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        // header
        JLabel historyTitle = new JLabel(" Transaction History");
        historyTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        historyTitle.setForeground(new Color(30, 70, 150));

        // JTable
        // DefaultTableModel : store data of table
        // initialize name of columns
        String[] columns = {"#", "Type", "Description", "Amount", "Date"};

        // creation tableModel with []columns and start row from 0
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            // user cannot edit the content of cell
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // creation the table
        transactionTable = new JTable(tableModel);
        transactionTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        //set each row height
        transactionTable.setRowHeight(30);
        // styling table header
        transactionTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        transactionTable.getTableHeader().setBackground(new Color(200, 215, 245));
        transactionTable.setSelectionBackground(new Color(180, 210, 255));
        transactionTable.setGridColor(new Color(220, 230, 245));

        // set columns width
        transactionTable.getColumnModel().getColumn(0).setMaxWidth(40);   // #
        transactionTable.getColumnModel().getColumn(1).setMaxWidth(80);   // Type
        transactionTable.getColumnModel().getColumn(3).setMaxWidth(100);  // Amount

        // Render & Colors : Render is how to show the cell ( Red, Green )
        transactionTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            // this method called by Swing while drawing cell
            // this method return a component c
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    // return the type of transaction
                    String type = (String) table.getValueAt(row, 1);
                    if ("Income".equals(type)) {
                        c.setBackground(new Color(230, 255, 235));
                        c.setForeground(new Color(0, 130, 50));
                    } else {
                        c.setBackground(new Color(255, 235, 235));
                        c.setForeground(new Color(180, 20, 20));
                    }
                }
                return c;
            }
        });

        // JScrollPane
        // without JScrollPane user cannot see the content beyond the available
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 215, 245)));

        content.add(historyTitle, BorderLayout.NORTH);
        content.add(scrollPane, BorderLayout.CENTER);
        return content;
    }

    // building buttons : bar
    private JPanel buildButtonBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        bar.setBackground(new Color(230, 238, 255));
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 215, 245)));

        // Income button
        JButton addIncomeBtn = makeButton("➕ Add Income", new Color(0, 150, 80));
        addIncomeBtn.addActionListener(e -> openAddTransactionDialog("Income"));

        // Expense button
        JButton addExpenseBtn = makeButton("➖ Add Expense", new Color(200, 40, 40));
        addExpenseBtn.addActionListener(e -> openAddTransactionDialog("Expense"));

        // Profile button
        JButton profileBtn = makeButton("👤 Profile", new Color(80, 100, 180));
        profileBtn.addActionListener(e -> showProfile());

        bar.add(addIncomeBtn);
        bar.add(addExpenseBtn);
        bar.add(profileBtn);
        return bar;
    }

    //
    private void openAddTransactionDialog(String type) {
        // AddTransactionDialog (JDialog) :> subpages
        // (this) :> return to DashboardFrame
        AddTransactionDialog dialog = new AddTransactionDialog(this, type, user, this);
        dialog.setVisible(true);
    }

    // refresh SetupFrame after each transaction
    // call from :> AddTransactionDialog
    public void refreshUI() {
        // refresh balance
        balanceLabel.setText("Balance: " + formatAmount(user.getWallet().getBalance()));

        // rebuild table
        tableModel.setRowCount(0); // clear table
        int rowNum = 1;
        for (Transaction t : user.getWallet().getTransactions()) {
            String type = (t instanceof Income) ? "Income" : "Expense";
            //Object[] :> rows
            tableModel.addRow(new Object[]{
                rowNum++,
                type,
                t.getDescription(),
                formatAmount(t.getAmount()),
                DATE_FORMAT.format(t.getDate())
            });
        }
    }

    // show profile :> JOptionPane
    private void showProfile() {
        String info = String.format(
            " Name   : %s %s\n" +
            " Gender : %s\n" +
            " Age    : %d\n" +
            " Balance: %s",
            user.getFirstName(), user.getLastName(),
            user.getGender(),
            user.getAge(),
            formatAmount(user.getWallet().getBalance())
        );
        JOptionPane.showMessageDialog(this, info, "User Profile", JOptionPane.INFORMATION_MESSAGE);
    }

    // Helper Methods :> structure of buttons
    private JButton makeButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(145, 38));
        return btn;
    }

    //currency method
    private String formatAmount(double amount) {
        return String.format("%.2f RY", amount);
    }
}
