package View;

import javax.swing.SwingUtilities;

public class MainApp {
    public static void main(String[] args) {
        // Execute By EDT : Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            SetupFrame setupFrame = new SetupFrame();
            setupFrame.setVisible(true);
        });
    }
}
