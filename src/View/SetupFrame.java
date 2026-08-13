package View;
//call User class from Model package
import Model.User;
//call swing & awt
import javax.swing.*;
import java.awt.*;

//this class inherits from JFrame
public class SetupFrame extends JFrame {

    //  initialize the components (Instance Variables)
    // data type = component type, according to the variables at User class
    private JTextField firstNameField;   //text field
    private JTextField lastNameField;    //text field
    private JComboBox<String> genderBox; //compo box
    private JSpinner ageSpinner;         //numbers field with + to increment and - to decrement
    private JButton startButton;         //button

    //  Constructor
    public SetupFrame() {
        initUI(); // call initUI method
    }

    private void initUI() {

        // settings of Main Frame
        setTitle("Personal Finance Manager - Setup");   // create title for page
        setSize(450, 400);                 // set the size of page
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// create x button to exit page
        setLocationRelativeTo(null);                  // make page's location in the middle
        setResizable(false);                         // prevent resizing

        // (Main Panel)
        // BorderLayout:NORTH / SOUTH / EAST / WEST / CENTER
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10)); //distance between BorderLayout
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // Padding
        mainPanel.setBackground(new Color(245, 248, 255)); // RGB color of background

        // upper part
        JLabel titleLabel = new JLabel(" Create Your Profile", SwingConstants.CENTER);// create title for main page
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));// set the font settings
        titleLabel.setForeground(new Color(30, 80, 160));// set the color of font
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));// set border

        //  (Form Panel)
        // GridBagLayout: layout
        //  "GridBagConstraints"
        JPanel formPanel = new JPanel(new GridBagLayout());// using GridBagLayout : rows & cols
        formPanel.setBackground(new Color(245, 248, 255));// set color background
        GridBagConstraints gbc = new GridBagConstraints();// obj to set constraints
        gbc.fill = GridBagConstraints.HORIZONTAL; // 1. fill horizontal
        gbc.insets = new Insets(8, 5, 8, 5);      // distance around each component

        //firstName
        // gridx = col، gridy = row
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        formPanel.add(makeLabel("First Name:"), gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        firstNameField = new JTextField(15);//text field distance = 15 col
        styleTextField(firstNameField);
        formPanel.add(firstNameField, gbc);

        // lastName
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        formPanel.add(makeLabel("Last Name:"), gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        lastNameField = new JTextField(15);
        styleTextField(lastNameField);
        formPanel.add(lastNameField, gbc);

        // gender (JComboBox)
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        formPanel.add(makeLabel("Gender:"), gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        // JComboBox<String>
        genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        genderBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(genderBox, gbc);

        //  age (JSpinner)
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.3;
        formPanel.add(makeLabel("Age:"), gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        // SpinnerNumberModel(initial value, max, min, stepSize)
        ageSpinner = new JSpinner(new SpinnerNumberModel(20, 1, 120, 1));
        ageSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(ageSpinner, gbc);

        // ── bottom part ──
        startButton = new JButton(" Start Managing Finances");
        startButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        startButton.setBackground(new Color(30, 80, 160));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);// prevent focus
        startButton.setBorderPainted(false);// prevent border
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));// change cursor
        startButton.setPreferredSize(new Dimension(0, 45));// set button size

        //  ActionListener
        //  Event Handling
        startButton.addActionListener(e -> handleStart());

        // collect all components
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(startButton, BorderLayout.SOUTH);

        // add JPanel to JFrame
        add(mainPanel);
    }

    // logic of Start button
    private void handleStart() {
        // get inputs
        // trim() : delete spaces
        String firstName = firstNameField.getText().trim();
        String lastName  = lastNameField.getText().trim();
        // getSelectedItem() return Object >> casting to change Object to String
        String gender    = (String) genderBox.getSelectedItem();
        // getValue() return Object >> casting to change Object to Integer
        int age          = (Integer) ageSpinner.getValue();

        // Validation
        if (firstName.isEmpty() || lastName.isEmpty()) {
            // JOptionPane: show massage
            JOptionPane.showMessageDialog(
                this,// point to setUpFrame
                "Please enter your full name!", //massage
                "Validation Error",                // address
                JOptionPane.WARNING_MESSAGE        // icon type
            );
            return; //stop execution
        }

        // create user from Model
        User user = new User(firstName, lastName, gender, age);

        // create DashboardFrame to user object
        DashboardFrame dashboard = new DashboardFrame(user);
        dashboard.setVisible(true);// show DashboardFrame
        this.dispose(); // close setUpFrame
    }
    // supporter methods
    // create Label method
    private JLabel makeLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(60, 60, 80));
        return label;
    }

    // design for text field
    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 200, 230), 1),// outside border
            BorderFactory.createEmptyBorder(5, 8, 5, 8)// inside border
        ));
    }
}