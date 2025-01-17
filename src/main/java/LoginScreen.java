import com.google.api.client.http.javanet.NetHttpTransport;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class LoginScreen {
    private final JFrame frame;
    private final GridBagConstraints gbc;
    private final ImageIcon logoIcon;
    private final JLabel logo;

    public LoginScreen() {
        //Window setup
        frame = new JFrame("Anzen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null);
        gbc = new GridBagConstraints();
        //Logo setup
        logoIcon = new ImageIcon("C:\\Users\\Leo\\Documents\\Anzen\\src\\main\\resources\\anzen_logo.png");
        logo = new JLabel();
        initializeComponents();
        addResizeListener();
    }
    //Method to initialize all components
    private void initializeComponents() {
        Image scaledImage = logoIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        logo.setIcon(new ImageIcon(scaledImage));
        //Logo Position
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty =1.0;
        gbc.fill = GridBagConstraints.CENTER;
        gbc.insets = new Insets(150, 10, 10, 10);
        frame.add(logo, gbc);
        //Login Button position
        JButton loginButton = createLoginButton();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(10, 10, 180, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        frame.add(loginButton, gbc);
    }
    //Method to create the Login button
    private JButton createLoginButton() {
        //Login button setup
        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(120, 40));
        loginButton.putClientProperty("JButton.buttonType", "roundRect");
        loginButton.setBackground(new Color(66, 135, 245));
        loginButton.setForeground(Color.WHITE);
        //Action to authenticate using the login button
        loginButton.addActionListener(_ -> {
            try {
                authenticateUser();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Authentication failed: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        return loginButton;
    }
    //Method to authenticate
    private void authenticateUser() {
        try {
            NetHttpTransport httpTransport = new NetHttpTransport();
            GoogleAuthenticator authenticator = new GoogleAuthenticator(httpTransport);
            authenticator.getDriveService();

            JOptionPane.showMessageDialog(frame, "Authentication successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
            new DownloadWindow().show();
        } catch (Exception ex){
            JOptionPane.showMessageDialog(frame, "Authentication failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    //Method to resize the logo with the size of the window
    private void addResizeListener() {
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int frameWidth = frame.getWidth();
                int logoSize = Math.min(frameWidth / 3, 300);
                Image scaledImage = logoIcon.getImage().getScaledInstance(logoSize, logoSize, Image.SCALE_SMOOTH);
                logo.setIcon(new ImageIcon(scaledImage));
            }
        });
    }
    //Self-Explanatory
    public void show() {
        frame.setVisible(true);
    }
}