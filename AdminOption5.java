import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AdminOption5 extends JFrame implements ActionListener {

    // --- GUI Components ---
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JTextField emailField;
    private final JTextField contactField;
    private final JButton updateButton;
    private final JButton cancelButton;

    // --- Admin Data (state variables, same as in the console version) ---
    private final String adminId;
    private final String currentUsername;
    private final String currentPassword;
    private final List<String> userFileLines;


    // Private constructor to build the main update GUI.
    // This is called only after the password verification in promptForPasswordAndShowGUI() succeeds.

    public AdminOption5(String adminId, String username, String password, String email, String contact, List<String> userLines) {
        this.adminId = adminId;
        this.currentUsername = username;
        this.currentPassword = password;
        this.userFileLines = userLines;

        setTitle("--- Update Admin Profile ---");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        mainPanel.add(new JLabel("Enter new information (leave password blank to keep it unchanged):", SwingConstants.CENTER), BorderLayout.NORTH);

        // --- Form Fields ---
        formPanel.add(new JLabel("New Username:"));
        usernameField = new JTextField(username);
        formPanel.add(usernameField);

        formPanel.add(new JLabel("New Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        formPanel.add(new JLabel("New Email Address:"));
        emailField = new JTextField(email);
        formPanel.add(emailField);

        formPanel.add(new JLabel("New Contact Number:"));
        contactField = new JTextField(contact);
        formPanel.add(contactField);

        // --- Buttons ---
        updateButton = new JButton("Update Profile");
        updateButton.addActionListener(this);
        formPanel.add(updateButton);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        formPanel.add(cancelButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == updateButton) {
            handleUpdate();
        } else if (e.getSource() == cancelButton) {
            this.dispose();
        }
    }


    // This method handles the core update logic

    private void handleUpdate() {
        // Get new values from the fields
        String newUsername = usernameField.getText().trim();
        String newPassword = new String(passwordField.getPassword());
        String newEmail = emailField.getText().trim();
        String newContact = contactField.getText().trim();

        // If a field is empty, keep the current value.
        if (newUsername.isEmpty()) newUsername = this.currentUsername;
        if (newEmail.isEmpty()) newEmail = "N/A";
        if (newContact.isEmpty()) newContact = "N/A";
        String finalPassword = newPassword.isEmpty() ? this.currentPassword : newPassword;

        try {
            // 1. Update admins.txt
            // It assumes a single admin entry that gets overwritten.
            try (FileWriter fw = new FileWriter("admins.txt", false)) { // false to overwrite
                fw.write(adminId + "," + newUsername + "," + newEmail + "," + newContact);
            }

            // 2. Update users.txt - This logic is corrected to find the user by their old username and write the line in the "username,password,role" format.
            for (int i = 0; i < userFileLines.size(); i++) {
                String[] parts = userFileLines.get(i).split(",");
                if (parts.length == 3 && parts[0].equals(this.currentUsername) && parts[2].equalsIgnoreCase("admin")) {
                    userFileLines.set(i, newUsername + "," + finalPassword + ",admin");
                    break;
                }
            }
            try (FileWriter fw = new FileWriter("users.txt", false)) { // false to overwrite
                for (String line : userFileLines) {
                    fw.write(line + System.lineSeparator());
                }
            }

            JOptionPane.showMessageDialog(this, "Admin profile updated successfully!\nNew credentials will be required for the next login.", "Success", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "An error occurred while saving the profile: " + ex.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    // This static method handles the entire process.
    // The logic has been corrected to handle the "username,password,role" format in users.txt.

    public static void promptForPasswordAndShowGUI() {
        // --- Security Check: Verify current password ---
        JPasswordField passwordField = new JPasswordField();
        Object[] message = {"Please enter your current password to proceed:", passwordField};
        int option = JOptionPane.showConfirmDialog(null, message, "Security Verification", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (option != JOptionPane.OK_OPTION) {
            return; // User cancelled
        }
        String enteredPassword = new String(passwordField.getPassword());
        if (enteredPassword.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- Data Retrieval ---
        String adminId = null, storedUsername = null, storedPassword = null;
        String storedEmail = "N/A", storedContact = "N/A";
        boolean adminFoundInUsers = false;
        List<String> userLines = new ArrayList<>();

        try {
            // 1. Read users.txt to find the admin, verify password, and get username.
            // The file format is username,password,role.
            File usersFile = new File("users.txt");
            if (!usersFile.exists()) throw new FileNotFoundException("users.txt not found.");

            try (BufferedReader br = new BufferedReader(new FileReader(usersFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    userLines.add(line);
                    String[] parts = line.split(",");
                    if (parts.length == 3 && parts[2].equalsIgnoreCase("admin") && parts[1].equals(enteredPassword)) {
                        adminFoundInUsers = true;
                        storedUsername = parts[0];
                        storedPassword = parts[1];
                        // Don't break, need to read all lines for the userFileLines list
                    }
                }
            }

            if (!adminFoundInUsers) {
                JOptionPane.showMessageDialog(null, "Incorrect password or admin user not found.", "Verification Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 2. Read admins.txt to get adminId, email, and contact using the username found above.
            // The file format is adminId,username,email,contact.
            boolean adminFoundInAdmins = false;
            File adminsFile = new File("admins.txt");
            if (adminsFile.exists()) {
                try (BufferedReader br = new BufferedReader(new FileReader(adminsFile))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length == 4 && parts[1].equals(storedUsername)) {
                            adminId = parts[0];
                            storedEmail = parts[2];
                            storedContact = parts[3];
                            adminFoundInAdmins = true;
                            break;
                        }
                    }
                }
            }

            if (!adminFoundInAdmins) {
                JOptionPane.showMessageDialog(null, "Admin data is inconsistent. Could not find details in admins.txt.", "Data Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 3. If verification is successful, create and show the main GUI.
            final String finalAdminId = adminId;
            final String finalUsername = storedUsername;
            final String finalPassword = storedPassword;
            final String finalEmail = storedEmail;
            final String finalContact = storedContact;

            SwingUtilities.invokeLater(() -> {
                AdminOption5 gui = new AdminOption5(finalAdminId, finalUsername, finalPassword, finalEmail, finalContact, userLines);
                gui.setVisible(true);
            });

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occurred while reading files: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void updateAdminProfile() {
        promptForPasswordAndShowGUI();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminOption5::promptForPasswordAndShowGUI);
    }
}