import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Manages all functionality related to a student's profile, including viewing and updating.
 */
public class ProfileManager {
    private final String studentID;
    private final String username;

    public ProfileManager(String studentID, String username) {
        this.studentID = studentID;
        this.username = username;
    }

    public void showProfile() {
        JFrame frame = new JFrame("Student Profile");
        frame.setSize(400, 400);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        JTextArea profileArea = new JTextArea();
        profileArea.setEditable(false);
        frame.add(new JScrollPane(profileArea), BorderLayout.CENTER);

        JButton updateBtn = new JButton("Update Profile");
        updateBtn.addActionListener(e -> showUpdateProfile());
        frame.add(updateBtn, BorderLayout.SOUTH);

        try {
            String password = "", ic = "", email = "", emergency = "", level = "", address = "";

            BufferedReader br = new BufferedReader(new FileReader("users.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] u = line.split(",");
                if (u[0].equals(username)) {
                    password = u[1];
                    break;
                }
            }
            br.close();

            BufferedReader sr = new BufferedReader(new FileReader("students.txt"));
            while ((line = sr.readLine()) != null) {
                String[] s = line.split(",");
                if (s[0].equals(studentID)) {
                    ic = s[2];
                    email = s[3];
                    emergency = s[4];
                    level = s[5];
                    address = s[6];
                    break;
                }
            }
            sr.close();

            profileArea.setText(
                    "Student ID: " + studentID + "\n" +
                            "Username: " + username + "\n" +
                            "Password: " + password + "\n" +
                            "IC / Passport: " + ic + "\n" +
                            "Email: " + email + "\n" +
                            "Emergency Contact: " + emergency + "\n" +
                            "Level: " + level + "\n" +
                            "Address: " + address
            );

        } catch (IOException e) {
            profileArea.setText("Error: " + e.getMessage());
        }

        frame.setVisible(true);
    }

    private void showUpdateProfile() {
        JFrame frame = new JFrame("Update Profile");
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));

        JTextField icField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField emergencyField = new JTextField();
        JTextArea addressArea = new JTextArea(3, 20);
        JPasswordField passwordField = new JPasswordField();

        try {
            BufferedReader br = new BufferedReader(new FileReader("users.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] u = line.split(",");
                if (u[0].equals(username)) {
                    passwordField.setText(u[1]);
                    break;
                }
            }
            br.close();

            BufferedReader sr = new BufferedReader(new FileReader("students.txt"));
            while ((line = sr.readLine()) != null) {
                String[] s = line.split(",");
                if (s[0].equals(studentID)) {
                    icField.setText(s[2]);
                    emailField.setText(s[3]);
                    emergencyField.setText(s[4]);
                    addressArea.setText(s[6]);
                    break;
                }
            }
            sr.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error loading current profile data: " + e.getMessage());
            return;
        }

        inputPanel.add(new JLabel("New Password:"));
        inputPanel.add(passwordField);
        inputPanel.add(new JLabel("New IC / Passport:"));
        inputPanel.add(icField);
        inputPanel.add(new JLabel("New Email:"));
        inputPanel.add(emailField);
        inputPanel.add(new JLabel("New Emergency Contact:"));
        inputPanel.add(emergencyField);
        inputPanel.add(new JLabel("New Address:"));
        inputPanel.add(new JScrollPane(addressArea));

        frame.add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.addActionListener(e -> {
            String newPassword = new String(passwordField.getPassword());
            String newIC = icField.getText();
            String newEmail = emailField.getText();
            String newEmergency = emergencyField.getText();
            String newAddress = addressArea.getText();

            updateDataInFile("users.txt", username, 1, newPassword);
            updateDataInFile("students.txt", studentID, 2, newIC);
            updateDataInFile("students.txt", studentID, 3, newEmail);
            updateDataInFile("students.txt", studentID, 4, newEmergency);
            updateDataInFile("students.txt", studentID, 6, newAddress);

            JOptionPane.showMessageDialog(frame, "Profile updated successfully!");
            frame.dispose();
            // Re-show profile to display updated data
            showProfile();
        });

        cancelBtn.addActionListener(e -> frame.dispose());

        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private static void updateDataInFile(String fileName, String identifier, int columnIndex, String newValue) {
        List<String> lines = new ArrayList<>();
        File file = new File(fileName);
        boolean found = false;

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",", -1); // Use -1 to keep empty trailing strings
                if (parts.length > 0 && parts[0].equals(identifier)) {
                    // Update the specified part of the line
                    // Ensure the array is large enough before updating
                    while (parts.length <= columnIndex) {
                        // Dynamically resize the array if the column index is out of bounds
                        String[] newParts = new String[parts.length + 1];
                        System.arraycopy(parts, 0, newParts, 0, parts.length);
                        newParts[parts.length] = ""; // Fill with empty string
                        parts = newParts;
                    }
                    parts[columnIndex] = newValue;
                    lines.add(String.join(",", parts));
                    found = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading file for update: " + e.getMessage());
            return;
        }

        if (found) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
                for (String line : lines) {
                    pw.println(line);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error writing to file during update: " + e.getMessage());
            }
        }
    }
}