import javax.swing.*;
import java.awt.*;

public class DownloadWindow {
    private final JFrame frame;
    private final GridBagConstraints gbc;

    public DownloadWindow() {
        //Window setup
        frame = new JFrame("Anzen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null);

        gbc = new GridBagConstraints();
        initComponents();
    }
    //Method to initialize all components
    private void initComponents() {
        //Setup of the left panel
        JPanel leftPanel = new JPanel(new GridBagLayout());
        GridBagConstraints leftGbc = new GridBagConstraints();
        leftPanel.setBackground(Color.WHITE);
        //Position of the download button
        JButton downloadButton = createDownloadButton();
        leftGbc.gridx = 0;
        leftGbc.gridy = 0;
        leftGbc.insets = new Insets(10, 10, 10, 10);
        leftGbc.anchor = GridBagConstraints.NORTH;
        leftPanel.add(downloadButton, leftGbc);
        //Position of the Upload Button
        JButton createUploadButton = createUploadButton();
        leftGbc.gridx = 0;
        leftGbc.gridy = 1;
        leftGbc.insets = new Insets(10, 10, 10, 10);
        leftGbc.anchor = GridBagConstraints.NORTH;
        leftPanel.add(createUploadButton, leftGbc);

        //Setup of the right panel
        JPanel rightPanel = new JPanel(new GridBagLayout());
        GridBagConstraints rightGbc = new GridBagConstraints();
        leftPanel.setBackground(Color.GRAY);
        //Text box *position only*
        JTextArea textAreaPrimary = createTextArea();
        rightGbc.gridx = 0;
        rightGbc.gridy = 0;
        rightGbc.weightx = 0.0;
        rightGbc.weighty = 0.0;
        rightGbc.insets = new Insets(10, 20, 10, 10);
        rightPanel.add(textAreaPrimary, rightGbc);
        //Text box *position only*
        JTextArea textAreaSecondary = createTextArea();
        rightGbc.gridx = 0;
        rightGbc.gridy = 1;
        rightGbc.weightx = 1.0;
        rightGbc.weighty = 0.0;
        rightGbc.insets = new Insets(10, 20, 10, 10);
        rightPanel.add(textAreaSecondary, rightGbc);
        //Position of the Left Panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        frame.add(leftPanel, gbc);
        //Position of the Right Panel
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.8;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        frame.add(rightPanel, gbc);
    }

    //Download Button Method
    private JButton createDownloadButton() {
        JButton downloadButton = new JButton("Download");
        downloadButton.setPreferredSize(new Dimension(200, 50));
        downloadButton.putClientProperty("JButton.buttonType", "Rect");
        downloadButton.setBackground(new Color(66, 135, 245));
        downloadButton.setForeground(Color.white);
        return downloadButton;
    }
    //Upload Button Method
    private JButton createUploadButton() {
        JButton uploadButton = new JButton("Upload");
        uploadButton.setPreferredSize(new Dimension(200, 50));
        uploadButton.putClientProperty("JButton.buttonType", "Rect");
        uploadButton.setBackground(new Color(66, 135, 245));
        uploadButton.setForeground(Color.white);
        return uploadButton;
    }
    //Text Area Method
    private JTextArea createTextArea() {
        return new JTextArea();
    }
    //Self-Explanatory
    public void show() {
        frame.setVisible(true);
    }
}