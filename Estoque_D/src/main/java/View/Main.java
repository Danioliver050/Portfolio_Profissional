package View;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EstoqueDanteApp app = new EstoqueDanteApp();
            app.setVisible(true);
        });
    }
}