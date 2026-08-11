package View;

import Model.Transaction;
import Model.User;
import Model.Income;
import Model.Expense;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.SimpleDateFormat;

/**
 * 🎓 LECTURE STEP 3: Dashboard - الشاشة الرئيسية
 *
 * المكونات الجديدة التي سنتعلمها هنا:
 *  - JTable            : جدول بيانات
 *  - DefaultTableModel : النموذج الذي يتحكم في بيانات الجدول
 *  - JScrollPane       : شريط تمرير (Scroll)
 *  - JSplitPane        : تقسيم اللوح لجزئين قابلين للتمديد
 *  - BoxLayout         : ترتيب العناصر رأسياً أو أفقياً
 */
public class DashboardFrame extends JFrame {

    // ─────────────────────────────────────────
    // 📦 مكونات الواجهة
    // ─────────────────────────────────────────
    private User user;
    private JLabel balanceLabel;       // عرض الرصيد
    private JLabel welcomeLabel;       // رسالة الترحيب
    private DefaultTableModel tableModel; // النموذج الذي يتحكم في بيانات الجدول
    private JTable transactionTable;   // الجدول نفسه

    private static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd HH:mm");

    // ─────────────────────────────────────────
    // 🏗️ Constructor
    // ─────────────────────────────────────────
    public DashboardFrame(User user) {
        this.user = user;
        initUI();
    }

    private void initUI() {
        // ── إعدادات النافذة ──
        setTitle("💰 Personal Finance Manager");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ══════════════════════════════════════
        // 🎨 اللوح الرئيسي بـ BorderLayout
        // ══════════════════════════════════════
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(new Color(240, 245, 255));

        // ── 1) الشريط العلوي (Header) ──
        mainPanel.add(buildHeader(), BorderLayout.NORTH);

        // ── 2) منطقة المحتوى الوسطى ──
        mainPanel.add(buildContent(), BorderLayout.CENTER);

        // ── 3) شريط الأزرار السفلي ──
        mainPanel.add(buildButtonBar(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    // ══════════════════════════════════════
    // 🔨 بناء الـ Header (الجزء العلوي)
    // ══════════════════════════════════════
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(25, 70, 150));
        header.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        // الاسم
        welcomeLabel = new JLabel(" Welcome, " + user.getFirstName() + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);

        // الرصيد
        balanceLabel = new JLabel("Balance: " + formatAmount(user.getWallet().getBalance()), SwingConstants.RIGHT);
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        balanceLabel.setForeground(new Color(100, 255, 150));

        header.add(welcomeLabel, BorderLayout.WEST);
        header.add(balanceLabel, BorderLayout.EAST);
        return header;
    }

    // ══════════════════════════════════════
    // 🔨 بناء منطقة المحتوى (الجزء الأوسط)
    // ══════════════════════════════════════
    private JPanel buildContent() {
        JPanel content = new JPanel(new BorderLayout(0, 10));
        content.setBackground(new Color(240, 245, 255));
        content.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        // عنوان الجدول
        JLabel historyTitle = new JLabel(" Transaction History");
        historyTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        historyTitle.setForeground(new Color(30, 70, 150));

        // ══════════════════════════════════════
        // 📊 JTable - جدول البيانات
        // DefaultTableModel هو الكلاس الذي يخزن بيانات الجدول
        // ══════════════════════════════════════

        // تعريف أسماء الأعمدة
        String[] columns = {"#", "Type", "Description", "Amount", "Date"};

        // إنشاء النموذج - false = الخلايا غير قابلة للتعديل
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // منع التعديل اليدوي
            }
        };

        // إنشاء الجدول وتمريره النموذج
        transactionTable = new JTable(tableModel);
        transactionTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        transactionTable.setRowHeight(30);
        transactionTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        transactionTable.getTableHeader().setBackground(new Color(200, 215, 245));
        transactionTable.setSelectionBackground(new Color(180, 210, 255));
        transactionTable.setGridColor(new Color(220, 230, 245));

        // ── تعيين عرض الأعمدة ──
        transactionTable.getColumnModel().getColumn(0).setMaxWidth(40);   // #
        transactionTable.getColumnModel().getColumn(1).setMaxWidth(80);   // Type
        transactionTable.getColumnModel().getColumn(3).setMaxWidth(100);  // Amount

        // ── تلوين صفوف الـ Income بالأخضر والـ Expense بالأحمر ──
        transactionTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
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

        // ══════════════════════════════════════
        // 📜 JScrollPane - شريط التمرير
        // نضع الجدول داخل JScrollPane لإظهار شريط التمرير
        // عندما تكثر البيانات
        // ══════════════════════════════════════
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 215, 245)));

        content.add(historyTitle, BorderLayout.NORTH);
        content.add(scrollPane, BorderLayout.CENTER);
        return content;
    }

    // ══════════════════════════════════════
    // 🔨 بناء شريط الأزرار (الجزء السفلي)
    // ══════════════════════════════════════
    private JPanel buildButtonBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        bar.setBackground(new Color(230, 238, 255));
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 215, 245)));

        // ── زر إضافة دخل ──
        JButton addIncomeBtn = makeButton("➕ Add Income", new Color(0, 150, 80));
        addIncomeBtn.addActionListener(e -> openAddTransactionDialog("Income"));

        // ── زر إضافة مصروف ──
        JButton addExpenseBtn = makeButton("➖ Add Expense", new Color(200, 40, 40));
        addExpenseBtn.addActionListener(e -> openAddTransactionDialog("Expense"));

        // ── زر عرض الملف الشخصي ──
        JButton profileBtn = makeButton("👤 Profile", new Color(80, 100, 180));
        profileBtn.addActionListener(e -> showProfile());

        bar.add(addIncomeBtn);
        bar.add(addExpenseBtn);
        bar.add(profileBtn);
        return bar;
    }

    // ══════════════════════════════════════
    // 🎯 فتح نافذة إضافة معاملة
    // ══════════════════════════════════════
    private void openAddTransactionDialog(String type) {
        // AddTransactionDialog هي نافذة فرعية (JDialog)
        AddTransactionDialog dialog = new AddTransactionDialog(this, type, user, this);
        dialog.setVisible(true);
    }

    // ══════════════════════════════════════
    // 🔄 تحديث الواجهة بعد إضافة معاملة جديدة
    // يُستدعى من AddTransactionDialog
    // ══════════════════════════════════════
    public void refreshUI() {
        // تحديث الرصيد
        balanceLabel.setText("Balance: " + formatAmount(user.getWallet().getBalance()));

        // إعادة بناء الجدول
        tableModel.setRowCount(0); // مسح الجدول
        int rowNum = 1;
        for (Transaction t : user.getWallet().getTransactions()) {
            String type = (t instanceof Income) ? "Income" : "Expense";
            tableModel.addRow(new Object[]{
                rowNum++,
                type,
                t.getDescription(),
                formatAmount(t.getAmount()),
                DATE_FORMAT.format(t.getDate())
            });
        }
    }

    // ══════════════════════════════════════
    // 🎯 عرض الملف الشخصي بـ JOptionPane
    // ══════════════════════════════════════
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

    // ──────────────────────────────────────
    // 🛠️ Helper Methods
    // ──────────────────────────────────────

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

    private String formatAmount(double amount) {
        return String.format("%.2f RY", amount);
    }
}
