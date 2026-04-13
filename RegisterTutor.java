import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class RegisterTutor extends JFrame implements ActionListener {

    // GUI Components
    private JTextField nameField, emailField, contactField;
    private JPasswordField passwordField;
    private JButton registerButton, backButton;

    public RegisterTutor() {
        // --- Frame Setup ---
        setTitle("Tutor Registration");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
        setLocationRelativeTo(null); // Center the window
        setLayout(null); // Use absolute positioning

        // --- Title ---
        JLabel titleLabel = new JLabel("Tutor Registration Form");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBounds(100, 20, 300, 25);
        add(titleLabel);

        // --- Form Fields and Labels ---
        // Name
        JLabel nameLabel = new JLabel("Tutor Name:");
        nameLabel.setBounds(50, 80, 100, 25);
        add(nameLabel);
        nameField = new JTextField();
        nameField.setBounds(160, 80, 200, 25);
        add(nameField);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 120, 100, 25);
        add(passwordLabel);
        passwordField = new JPasswordField();
        passwordField.setBounds(160, 120, 200, 25);
        add(passwordField);

        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 160, 100, 25);
        add(emailLabel);
        emailField = new JTextField();
        emailField.setBounds(160, 160, 200, 25);
        add(emailField);

        // Contact
        JLabel contactLabel = new JLabel("Contact No:");
        contactLabel.setBounds(50, 200, 100, 25);
        add(contactLabel);
        contactField = new JTextField();
        contactField.setBounds(160, 200, 200, 25);
        add(contactField);

        // --- Buttons ---
        // Register Button
        registerButton = new JButton("Register");
        registerButton.setBounds(100, 260, 100, 30);
        registerButton.addActionListener(this); // Add listener
        add(registerButton);

        // Back Button
        backButton = new JButton("Back");
        backButton.setBounds(240, 260, 100, 30);
        backButton.addActionListener(this); // Add listener
        add(backButton);

        setVisible(true);
    }


    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            performRegistration();
        } else if (e.getSource() == backButton) {
            // Simply close this window
            this.dispose();
        }
    }

    private void performRegistration() {
        // 1. Get data from GUI fields
        String tutorName = nameField.getText();
        String tutorPassword = new String(passwordField.getPassword());
        String tutorEmail = emailField.getText();
        String tutorContact = contactField.getText();

        // 2. Basic Validation
        if (tutorName.isEmpty() || tutorPassword.isEmpty() || tutorEmail.isEmpty() || tutorContact.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // 3. Generate a new unique ID
            String tutorId = generateNewTutorId();

            // 4. Write credentials to users.txt
            try (FileWriter userFileWriter = new FileWriter("users.txt", true);
                 BufferedWriter userBufferedWriter = new BufferedWriter(userFileWriter);
                 PrintWriter userPrintWriter = new PrintWriter(userBufferedWriter)) {
                userPrintWriter.println(tutorName + "," + tutorPassword + ",tutor");
            }

            // 5. Write details to tutors.txt
            try (FileWriter tutorFileWriter = new FileWriter("tutors.txt", true);
                 BufferedWriter tutorBufferedWriter = new BufferedWriter(tutorFileWriter);
                 PrintWriter tutorPrintWriter = new PrintWriter(tutorBufferedWriter)) {
                tutorPrintWriter.println(tutorId + "," + tutorName + "," + tutorEmail + "," + tutorContact);
            }

            // 6. Show success message
            JOptionPane.showMessageDialog(this, "Tutor has been successfully created with ID: " + tutorId, "Registration Successful", JOptionPane.INFORMATION_MESSAGE);

            // 7. Clear fields and close window
            clearFields();
            this.dispose();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "An error occurred while saving the data: " + ex.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        nameField.setText("");
        passwordField.setText("");
        emailField.setText("");
        contactField.setText("");
    }

    // Generates a new unique Tutor ID by checking the tutors.txt file.
    // It finds the highest existing ID (e.g., T05) and returns the next one (e.g., T06).
    // If the file doesn't exist, it starts with "T01".

    private String generateNewTutorId() throws IOException {
        File tutorFile = new File("tutors.txt");
        int maxId = 0;

        if (tutorFile.exists() && tutorFile.length() > 0) {
            try (BufferedReader reader = new BufferedReader(new FileReader(tutorFile))) {
                String currentLine;
                while ((currentLine = reader.readLine()) != null) {
                    if(currentLine.trim().isEmpty()) continue; // Skip empty lines
                    String[] tutorData = currentLine.split(",");
                    if (tutorData.length > 0 && tutorData[0].matches("T\\d+")) {
                        try {
                            int idNum = Integer.parseInt(tutorData[0].substring(1));
                            if (idNum > maxId) {
                                maxId = idNum;
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Skipping malformed tutor ID: " + tutorData[0]);
                        }
                    }
                }
            }
        }
        // The new ID is the max existing ID plus one, formatted with a leading zero if needed.
        return String.format("T%02d", maxId + 1);
    }

    public static void main(String[] args) {
        // Makes the GUI runnable on its own for testing purposes.
        SwingUtilities.invokeLater(RegisterTutor::new);
    }
}