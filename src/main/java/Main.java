public class Main {
    public static void main(String[] args) {
        System.out.println("project working...");
    }
}

// this is for tesingimport javax.swing.SwingUtilities;
import view.auth.AuthFrame;

public class Main {
    public static void main(String[] args) {
        try {
            // Set System L&F
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            AuthFrame authFrame = new AuthFrame();
            authFrame.setVisible(true);
        });
    }
}