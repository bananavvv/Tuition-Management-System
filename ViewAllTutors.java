import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ViewAllTutors extends JFrame {

    // GUI Components
    private JTextArea displayArea;

    public ViewAllTutors() {
        // --- Frame Setup ---
        setTitle("All Registered Tutors");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
        setLocationRelativeTo(null); // Center the window

        // --- Main Panel Setup ---
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(10, 10));
        setContentPane(contentPane);

        // --- Title ---
        JLabel titleLabel = new JLabel("All Registered Tutors", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        contentPane.add(titleLabel, BorderLayout.NORTH);

        // --- Text Area for Displaying Tutors ---
        displayArea = new JTextArea();
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Use a monospaced font for alignment
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea); // Make it scrollable
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // --- Bottom Panel with a Back Button ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> this.dispose()); // Lambda to close the window
        buttonPanel.add(backButton);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        // --- Load data from file ---
        loadAndDisplayTutors();

        setVisible(true);
    }


    // Reads tutor data from tutors.txt and formats it for display in the JTextArea.

    private void loadAndDisplayTutors() {
        File tutorFile = new File("tutors.txt");

        // Check if the file exists before trying to read
        if (!tutorFile.exists()) {
            displayArea.setText("\n   No tutors have been registered yet (tutors.txt not found).");
            displayArea.setForeground(Color.RED);
            return;
        }

        StringBuilder displayText = new StringBuilder();
        boolean foundTutors = false;

        // Add a formatted header to the text
        displayText.append(String.format("%-10s | %-25s | %-30s | %-15s\n", "ID", "Name", "Email", "Contact"));
        displayText.append("----------------------------------------------------------------------------------------\n");

        try (BufferedReader br = new BufferedReader(new FileReader(tutorFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Skip blank lines

                foundTutors = true;
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    // Append each tutor's data in a formatted way
                    displayText.append(String.format("%-10s | %-25s | %-30s | %-15s\n", parts[0], parts[1], parts[2], parts[3]));
                } else {
                    displayText.append("Malformed tutor entry: " + line + "\n");
                }
            }

            if (!foundTutors) {
                displayArea.setText("\n   No tutors have been registered yet.");
            } else {
                displayArea.setText(displayText.toString());
            }

            // Ensure the text area scrolls to the top
            displayArea.setCaretPosition(0);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading from tutor file: " + e.getMessage(), "File Read Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // This allows the GUI to be run on its own for testing purposes.
        SwingUtilities.invokeLater(ViewAllTutors::new);
    }
}