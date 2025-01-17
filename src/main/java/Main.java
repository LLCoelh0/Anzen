import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        //Initialize FlatLaf
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to Initialize Laf");
        }
        //Initialize swing and call the classes
        SwingUtilities.invokeLater(() -> {
           LoginScreen loginScreen = new LoginScreen();
           loginScreen.show();
        });
    }
}