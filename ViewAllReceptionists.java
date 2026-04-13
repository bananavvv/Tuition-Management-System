import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ViewAllReceptionists extends JFrame {

    // --- GUI Components ---
    private final JTextArea displayArea;


    // Constructor that builds the GUI and loads the data.

    public ViewAllReceptionists() {
        setTitle("--- All Registered Receptionists ---");
        setSize(550, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Display Area ---
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        displayArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add a scroll pane in case the list is long
        JScrollPane scrollPane = new JScrollPane(displayArea);
        add(scrollPane, BorderLayout.CENTER);

        // --- Close Button ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> this.dispose()); // Lambda for simple action
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load the receptionist data into the text area
        loadReceptionistData();
    }


    // Reads the receptionist data from the file and populates the JTextArea.
    // This method contains the core logic from the original console application.

    private void loadReceptionistData() {
        StringBuilder sb = new StringBuilder();
        boolean foundReceptionists = false;

        try (BufferedReader br = new BufferedReader(new FileReader("receptionists.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) { // Skip empty lines
                    foundReceptionists = true;
                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        sb.append("ID: ").append(parts[0])
                                .append(", Name: ").append(parts[1])
                                .append(", Email: ").append(parts[2])
                                .append(", Contact: ").append(parts[3])
                                .append("\n");
                    } else {
                        sb.append("Malformed receptionist entry: ").append(line).append("\n");
                    }
                }
            }

            if (!foundReceptionists) {
                sb.append("No receptionists have been registered yet.");
            }

        } catch (FileNotFoundException e) {
            sb.append("No receptionists have been registered yet (receptionist.txt not found).");
        } catch (IOException e) {
            sb.append("Error reading from receptionist file: ").append(e.getMessage());
        }

        // Set the final text to the display area
        displayArea.setText(sb.toString());
        // Move caret to the top
        displayArea.setCaretPosition(0);
    }


    // A static method to create and show the GUI.

    public static void showViewGUI() {
        SwingUtilities.invokeLater(() -> {
            ViewAllReceptionists viewer = new ViewAllReceptionists();
            viewer.setVisible(true);
        });
    }


    // Main method for independent testing.

    public static void main(String[] args) {
        // To launch the GUI, call the static method.
        showViewGUI();
    }
}