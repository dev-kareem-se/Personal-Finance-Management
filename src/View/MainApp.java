package View;

import javax.swing.SwingUtilities;

/**
 * 🎓 LECTURE STEP 1: Entry Point
 *
 * SwingUtilities.invokeLater() → هذا السطر مهم جداً!
 * Swing يعمل على thread خاص يسمى EDT (Event Dispatch Thread)
 * يجب دائماً تشغيل أي كود Swing داخل هذا الـ thread
 * لتجنب مشاكل التزامن (Thread Safety)
 */
public class MainApp {
    public static void main(String[] args) {
        // تشغيل الواجهة على EDT - الـ Thread الخاص بـ Swing
        SwingUtilities.invokeLater(() -> {
            SetupFrame setupFrame = new SetupFrame();
            setupFrame.setVisible(true); // إظهار النافذة
        });
    }
}
