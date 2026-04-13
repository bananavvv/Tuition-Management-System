import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class RegisterReceptionist extends JFrame implements ActionListener {

    // --- GUI Components ---
    private final JTextField nameField;
    private final JPasswordField passwordField;
    private final JTextField emailField;
    private final JTextField contactField;
    private final JButton registerButton;
    private final JButton cancelButton;


    // Constructor to build the registration GUI.

    public RegisterReceptionist() {
        setTitle("--- Receptionist Registration ---");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- Main Panel with Padding ---
        // This highlights that a border is being added to create empty space (padding) around the edges of the window for better visual appearance.
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- Form Panel ---
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        formPanel.add(new JLabel("Enter Receptionist Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Enter Receptionist Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        formPanel.add(new JLabel("Enter Receptionist Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Enter Receptionist Contact:"));
        contactField = new JTextField();
        formPanel.add(contactField);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        registerButton = new JButton("Register");
        registerButton.addActionListener(this);
        buttonPanel.add(registerButton);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            handleRegistration();
        } else if (e.getSource() == cancelButton) {
            this.dispose();
        }
    }

    // This method contains the core registration logic, adapted from the original console method.

    private void handleRegistration() {
        // Retrieve input from GUI fields
        String receptionistName = nameField.getText().trim();
        String receptionistPassword = new String(passwordField.getPassword()).trim();
        String receptionistEmail = emailField.getText().trim();
        String receptionistContact = contactField.getText().trim();

        // Basic validation
        if (receptionistName.isEmpty() || receptionistPassword.isEmpty() || receptionistEmail.isEmpty() || receptionistContact.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Generate the Receptionist ID using the same logic as the console app
            String receptionistId = generateNewReceptionistId();

            // Write credentials to users.txt
            try (FileWriter writer = new FileWriter("users.txt", true);
                 BufferedWriter bw = new BufferedWriter(writer);
                 PrintWriter out = new PrintWriter(bw)) {
                out.println(receptionistName + "," + receptionistPassword + ",receptionist");
            }

            // Write details to receptionists.txt
            try (FileWriter writer = new FileWriter("receptionists.txt", true);
                 BufferedWriter bw = new BufferedWriter(writer);
                 PrintWriter out = new PrintWriter(bw)) {
                out.println(receptionistId + "," + receptionistName + "," + receptionistEmail + "," + receptionistContact);
            }

            // Display success message
            JOptionPane.showMessageDialog(this, "Receptionist has been successfully created with ID: " + receptionistId, "Success", JOptionPane.INFORMATION_MESSAGE);

            // Clear fields for next entry
            nameField.setText("");
            passwordField.setText("");
            emailField.setText("");
            contactField.setText("");

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "An error occurred while saving the data: " + ex.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Generates a new unique Receptionist ID by checking the receptionists.txt file.
    // This logic is identical to the original method.

    private String generateNewReceptionistId() throws IOException {
        File receptionistFile = new File("receptionists.txt");
        int maxId = 0;

        if (receptionistFile.exists() && receptionistFile.length() > 0) {
            try (BufferedReader reader = new BufferedReader(new FileReader(receptionistFile))) {
                String currentLine;
                while ((currentLine = reader.readLine()) != null) {
                    String[] receptionistData = currentLine.split(",");
                    if (receptionistData.length > 0 && receptionistData[0].matches("R\\d+")) {
                        try {
                            int idNum = Integer.parseInt(receptionistData[0].substring(1));
                            if (idNum > maxId) {
                                maxId = idNum;
                            }
                        } catch (NumberFormatException e) {
                            // This would have printed to console; in a GUI, we could log it or ignore it.
                            System.err.println("Skipping malformed receptionist ID: " + receptionistData[0]);
                        }
                    }
                }
            }
        }
        return String.format("R%02d", maxId + 1);
    }


    // The public entry point to create and show the registration GUI.

    public static void showRegistrationGUI() {
        SwingUtilities.invokeLater(() -> {
            RegisterReceptionist gui = new RegisterReceptionist();
            gui.setVisible(true);
        });
    }


    // Main method for independent testing.

    public static void main(String[] args) {
        // This is how you would launch the GUI.
        showRegistrationGUI();
    }
}