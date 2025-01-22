import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class DownloadWindow {
    private final JFrame frame;
    private final GridBagConstraints gbc;
    private JPanel filePanel;
    private final Drive driveService;

    public DownloadWindow(Drive driveService) {
        if (driveService == null) {
            throw new IllegalArgumentException("Drive service cannot be null");
        }
        this.driveService = driveService;
        //Window setup
        frame = new JFrame("D"); //change this*************
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
        filePanel = new JPanel();
        rightGbc.gridx = 0;
        rightGbc.gridy = 0;
        rightGbc.weightx = 0.0;
        rightGbc.weighty = 0.0;
        rightGbc.insets = new Insets(10, 20, 10, 10);
        rightPanel.add(filePanel, rightGbc);


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

        downloadButton.addActionListener(_ -> loadFilesFromDrive());
        return downloadButton;
    }
    //Upload Button Method
    private JButton createUploadButton() {
        JButton uploadButton = new JButton("Upload");
        uploadButton.setPreferredSize(new Dimension(200, 50));
        uploadButton.putClientProperty("JButton.buttonType", "Rect");
        uploadButton.setBackground(new Color(66, 135, 245));
        uploadButton.setForeground(Color.white);

        uploadButton.addActionListener(_ -> {
            frame.dispose();
            new UploadWindow().show();
        });
        return uploadButton;
    }

    private void loadFilesFromDrive() {
        try {
            //Get the files list from the GDrive
            List<File> files = listFiles();

            filePanel.removeAll();
            filePanel.setLayout(new BorderLayout());

            DefaultListModel<String> fileListModel = new DefaultListModel<>();
            JList<String> fileList = new JList<>(fileListModel);
            fileList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

            //Add the files in the list
            for (File file : files) {
                fileListModel.addElement(file.getName() + " (ID " + file.getId() + ")");
            }

            JButton downloadSelectedButton = new JButton("Download Selected");
            downloadSelectedButton.addActionListener(_ -> {
                List<String> selectedValues = fileList.getSelectedValuesList();

                if (!selectedValues.isEmpty()) {
                    String downloadDirectory = "downloads";
                    new java.io.File(downloadDirectory).mkdirs();

                    for (String selectedValue : selectedValues) {
                        try {
                            String fileId = extractFileId(selectedValue);
                            String fileName = extractFileName(selectedValue);

                            String filePath = downloadDirectory + java.io.File.separator + fileName;

                            downloadFile(fileId, filePath);
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(frame, "Error downloading file: " + ex.getMessage(),
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    JOptionPane.showMessageDialog(frame, "Files downloaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "No files selected!", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            });

            JScrollPane scrollPane = new JScrollPane(fileList);
            filePanel.add(scrollPane, BorderLayout.CENTER);
            filePanel.add(downloadSelectedButton, BorderLayout.SOUTH);

            filePanel.revalidate();
            filePanel.repaint();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error while loading files: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private List<File> listFiles() throws IOException {
        FileList result = driveService.files().list().setFields("files(id, name, mimeType)").execute();
        return result.getFiles();
    }

    private void downloadFile(String fileId, String filePath) throws IOException {
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            driveService.files().get(fileId).executeMediaAndDownloadTo(outputStream);
        } catch (IOException ex) {
            throw new IOException("Error while downloading file " + fileId + ": " + ex.getMessage(), ex);
        }
    }

    private String extractFileName(String selectedValue) {
        return selectedValue.substring(selectedValue.indexOf("(ID: ") + 5, selectedValue.length() - 1);
    }

    private String extractFileId(String selectedValue) {
        return selectedValue.substring(0, selectedValue.indexOf("(ID: "));
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