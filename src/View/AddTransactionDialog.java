package View;

import Model.Expense;
import Model.Income;
import Model.User;

import javax.swing.*;
import java.awt.*;

/**
 * 🎓 LECTURE STEP 4: JDialog - النافذة الفرعية
 *
 * الفرق بين JFrame و JDialog:
 *  - JFrame  : نافذة مستقلة رئيسية
 *  - JDialog : نافذة فرعية تابعة لـ JFrame آخر
 *
 * Modal Dialog = الـ Dialog يمنع التفاعل مع النافذة الأب
 * حتى يُغلق (true = modal)
 *
 * هنا سنتعلم:
 *  - JDialog       : النافذة الفرعية
 *  - ButtonGroup   : تجميع أزرار الراديو لاختيار واحد فقط
 *  - JRadioButton  : زر راديو (اختيار واحد من مجموعة)
 *  - JTextArea     : حقل نص متعدد الأسطر
 */
public class AddTransactionDialog extends JDialog {

    private JTextField amountField;
    private JTextField descriptionField;
    private JLabel typeLabel;
    private String transactionType;  // "Income" أو "Expense"
    private User user;
    private DashboardFrame dashboard;

    /**
     * @param parent    النافذة الأب
     * @param type      نوع المعاملة ("Income" أو "Expense")
     * @param user      المستخدم الحالي
     * @param dashboard الشاشة الرئيسية (لتحديثها بعد الإضافة)
     */
    public AddTransactionDialog(JFrame parent, String type,
                                 User user, DashboardFrame dashboard) {
        // super(parent, title, modal)
        // modal = true → تمنع التفاعل مع النافذة الأب
        super(parent, (type.equals("Income") ? "➕ Add Income" : "➖ Add Expense"), true);

        this.transactionType = type;
        this.user = user;
        this.dashboard = dashboard;

        initUI();
    }

    private void initUI() {
        setSize(380, 280);
        setLocationRelativeTo(getOwner()); // وسّط بالنسبة للنافذة الأب
        setResizable(false);

        Color themeColor = transactionType.equals("Income")
                ? new Color(0, 150, 80)   // أخضر للدخل
                : new Color(200, 40, 40); // أحمر للمصروف

        // ══════════════════════════════════════
        // 🎨 اللوح الرئيسي
        // ══════════════════════════════════════
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 25));
        panel.setBackground(Color.WHITE);

        // ── شريط ملون في الأعلى يوضح نوع المعاملة ──
        JPanel typeBar = new JPanel();
        typeBar.setBackground(themeColor);
        typeBar.setPreferredSize(new Dimension(0, 8));
        panel.add(typeBar, BorderLayout.NORTH);

        // ── نموذج الإدخال ──
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(7, 5, 7, 5);

        // حقل المبلغ
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.35;
        form.add(makeLabel("Amount (RY):"), gbc);

        gbc.gridx = 1; gbc.weightx = 0.65;
        amountField = new JTextField();
        styleField(amountField);
        form.add(amountField, gbc);

        // حقل الوصف
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.35;
        form.add(makeLabel("Description:"), gbc);

        gbc.gridx = 1; gbc.weightx = 0.65;
        descriptionField = new JTextField();
        styleField(descriptionField);
        form.add(descriptionField, gbc);

        // ── شريط الأزرار ──
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(Color.WHITE);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cancelBtn.setFocusPainted(false);
        // إغلاق الـ Dialog بدون حفظ
        cancelBtn.addActionListener(e -> dispose());

        JButton saveBtn = new JButton("Save ✓");
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

    // ══════════════════════════════════════
    // 🎯 منطق الحفظ
    // ══════════════════════════════════════
    private void handleSave() {
        // ── قراءة القيم ──
        String amountText = amountField.getText().trim();
        String description = descriptionField.getText().trim();

        // ── Validation ──
        if (amountText.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all fields.",
                "Missing Data",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

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

        // ── إنشاء المعاملة وإضافتها للـ Wallet ──
        if (transactionType.equals("Income")) {
            Income income = new Income(amount, description);
            user.getWallet().addTransaction(income);

            JOptionPane.showMessageDialog(this,
                "✅ Income of " + String.format("%.2f RY", amount) + " added!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            // تحقق من أن الرصيد كافٍ قبل الإضافة
            if (amount > user.getWallet().getBalance()) {
                JOptionPane.showMessageDialog(this,
                    "❌ Insufficient balance!\n" +
                    "Available: " + String.format("%.2f RY", user.getWallet().getBalance()),
                    "Transaction Failed",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            Expense expense = new Expense(amount, description);
            user.getWallet().addTransaction(expense);

            JOptionPane.showMessageDialog(this,
                "✅ Expense of " + String.format("%.2f RY", amount) + " recorded!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        }

        // ── تحديث الشاشة الرئيسية ──
        dashboard.refreshUI();

        // ── إغلاق الـ Dialog ──
        dispose();
    }

    // ── Helper Methods ──
    private JLabel makeLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(60, 60, 80));
        return label;
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 230), 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
    }
}
