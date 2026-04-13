import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DeleteTutor extends JFrame implements ActionListener {

    // GUI Components
    private JTextArea tutorsTextArea;
    private JTextField tutorIdField;
    private JButton deleteButton, backButton;

    public DeleteTutor() {
        // --- Frame Setup ---
        setTitle("Delete Tutor Records");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
        setLocationRelativeTo(null); // Center the window

        // --- Main Panel ---
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(10, 10));
        setContentPane(contentPane);

        // --- Title ---
        JLabel titleLabel = new JLabel("Manage Tutors", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        contentPane.add(titleLabel, BorderLayout.NORTH);

        // --- Center Panel for Displaying Tutors ---
        tutorsTextArea = new JTextArea();
        tutorsTextArea.setEditable(false);
        tutorsTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(tutorsTextArea);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // --- Bottom Panel for Input and Actions ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JLabel idLabel = new JLabel("Enter Tutor ID to Delete:");
        bottomPanel.add(idLabel);

        tutorIdField = new JTextField(10);
        bottomPanel.add(tutorIdField);

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(this);
        bottomPanel.add(deleteButton);

        backButton = new JButton("Back");
        backButton.addActionListener(this);
        bottomPanel.add(backButton);

        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        // Initial load of tutors
        loadAndDisplayTutors();

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == deleteButton) {
            performDeletion();
        } else if (e.getSource() == backButton) {
            this.dispose(); // Close this window
        }
    }

    //Reads tutors from tutors.txt and displays them in the text area.

    private void loadAndDisplayTutors() {
        File tutorFile = new File("tutors.txt");
        if (!tutorFile.exists()) {
            tutorsTextArea.setText("Tutor file not found. No tutors to display.");
            return;
        }

        StringBuilder tutorsText = new StringBuilder();
        // Create a header for the display
        tutorsText.append(String.format("%-10s %-20s %-25s %-15s\n", "ID", "Name", "Email", "Contact"));
        tutorsText.append("------------------------------------------------------------------\n");

        try (BufferedReader br = new BufferedReader(new FileReader(tutorFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                if (data.length >= 4) {
                    tutorsText.append(String.format("%-10s %-20s %-25s %-15s\n", data[0], data[1], data[2], data[3]));
                }
            }
            tutorsTextArea.setText(tutorsText.toString());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading tutor file: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Handles the logic of deleting a tutor record.

    private void performDeletion() {
        String tutorIDToDelete = tutorIdField.getText().trim();

        if (tutorIDToDelete.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Tutor ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        File tutorFile = new File("tutors.txt");
        if (!tutorFile.exists()) {
            JOptionPane.showMessageDialog(this, "Tutor file not found. Nothing to delete.", "File Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Read all tutors into a list
            List<String> tutors = new ArrayList<>();
            String tutorToDeleteRecord = null;
            try (BufferedReader br = new BufferedReader(new FileReader(tutorFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    tutors.add(line);
                    if (line.startsWith(tutorIDToDelete + ",")) {
                        tutorToDeleteRecord = line;
                    }
                }
            }

            // Check if tutor was found
            if (tutorToDeleteRecord == null) {
                JOptionPane.showMessageDialog(this, "Tutor with ID " + tutorIDToDelete + " not found.", "Not Found", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Confirm deletion with a dialog
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this tutor?\n" + tutorToDeleteRecord.replace(",", "\n"),
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                // Remove the tutor from the list
                tutors.removeIf(line -> line.startsWith(tutorIDToDelete + ","));
                // Rewrite the tutors.txt file
                try (FileWriter writer = new FileWriter(tutorFile, false)) {
                    for (String tutor : tutors) {
                        writer.write(tutor + "\n");
                    }
                }

                // Delete the corresponding user from users.txt
                deleteUserRecord(tutorIDToDelete);

                JOptionPane.showMessageDialog(this, "Tutor with ID " + tutorIDToDelete + " has been deleted from all records.", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Refresh the display and clear the input field
                loadAndDisplayTutors();
                tutorIdField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Deletion cancelled.", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Deletes the user record associated with a given ID from users.txt.

    private void deleteUserRecord(String idToDelete) throws IOException {
        File userFile = new File("users.txt");
        if (!userFile.exists()) return; // Nothing to do if the file doesn't exist

        List<String> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
            users = br.lines().collect(Collectors.toList());
        }

        users.removeIf(userLine -> userLine.startsWith(idToDelete + ","));

        try (FileWriter writer = new FileWriter(userFile, false)) {
            for (String user : users) {
                writer.write(user + "\n");
            }
        }
    }

    public static void main(String[] args) {
        // Makes the GUI runnable on its own for testing purposes.
        SwingUtilities.invokeLater(DeleteTutor::new);
    }
}