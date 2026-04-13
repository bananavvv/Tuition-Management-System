import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DeleteReceptionist extends JFrame implements ActionListener {

    // --- GUI Components ---
    private final JTextArea receptionistDisplayArea;
    private final JTextField receptionistIdField;
    private final JButton findButton;
    private final JButton cancelButton;

    /**
     * Constructor to build the GUI.
     */
    public DeleteReceptionist() {
        setTitle("--- Delete Receptionist Records ---");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Display Area for Receptionists ---
        receptionistDisplayArea = new JTextArea();
        receptionistDisplayArea.setEditable(false);
        receptionistDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(receptionistDisplayArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("All Receptionists"));
        add(scrollPane, BorderLayout.CENTER);

        // --- Input Panel for Deletion ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.add(new JLabel("Enter Receptionist ID to delete (e.g., R01):"));
        receptionistIdField = new JTextField(10);
        bottomPanel.add(receptionistIdField);

        findButton = new JButton("Find and Delete");
        findButton.addActionListener(this);
        bottomPanel.add(findButton);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        bottomPanel.add(cancelButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // Initial load of receptionist data
        loadReceptionistData();
    }


    // This part handles button click events.
    // the actionPerformed method is the central place where all button clicks are managed, It checks which button was pressed and calls the appropriate function in response.

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == findButton) {
            handleDelete();
        } else if (e.getSource() == cancelButton) {
            this.dispose();
        }
    }


    // Loads and displays all receptionist records in the JTextArea.

    private void loadReceptionistData() {
        StringBuilder sb = new StringBuilder();
        File receptionistFile = new File("receptionists.txt");
        if (!receptionistFile.exists()) {
            receptionistDisplayArea.setText("Receptionist file not found. Nothing to display.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(receptionistFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            receptionistDisplayArea.setText(sb.toString());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading receptionist file: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Handles the core deletion logic, from finding the receptionist to confirming and deleting.
    // This method explains that this is where the main process of deleting a receptionist occurs.
    // Steps: getting the ID, finding the record, asking for user confirmation, and then proceeding with the deletion.

    private void handleDelete() {
        String receptionistIDToDelete = receptionistIdField.getText().trim();
        if (receptionistIDToDelete.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Receptionist ID.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        File receptionistFile = new File("receptionists.txt");
        if (!receptionistFile.exists()) {
            JOptionPane.showMessageDialog(this, "Receptionist file not found. Nothing to delete.", "File Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // --- Find the receptionist and their details ---
            // This code segment responsible for reading the "receptionists.txt" file to locate the specific receptionist that the user wants to delete.
            String[] receptionistData = null;
            List<String> allReceptionists = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(receptionistFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    allReceptionists.add(line);
                    String[] parts = line.split(",");
                    if (parts.length > 0 && parts[0].equalsIgnoreCase(receptionistIDToDelete)) {
                        receptionistData = parts;
                    }
                }
            }

            if (receptionistData == null) {
                JOptionPane.showMessageDialog(this, "Receptionist with ID " + receptionistIDToDelete + " not found.", "Not Found", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // --- Confirm Deletion (Replaces "Are you sure? Yes/No") ---
            // This part shows that the following code block creates a pop-up confirmation dialog box.
            String confirmationMessage = "Are you sure you want to delete this receptionist?\n\n" +
                    "ID: " + receptionistData[0] + "\n" +
                    "Name: " + receptionistData[1] + "\n" +
                    "Email: " + receptionistData[2] + "\n" +
                    "Contact: " + receptionistData[3];
            int choice = JOptionPane.showConfirmDialog(this, confirmationMessage, "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (choice == JOptionPane.YES_OPTION) {
                // --- Perform Deletion (Identical logic to console version) ---
                performDeletion(receptionistIDToDelete, allReceptionists);
                JOptionPane.showMessageDialog(this, "Receptionist with ID " + receptionistIDToDelete + " has been deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Refresh the display and clear the input field
                loadReceptionistData();
                receptionistIdField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Deletion cancelled.", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "An error occurred during deletion: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    // This method contains the file writing logic, which is identical to the original code.
    // receptionistIDToDelete The ID to remove.
    // allReceptionists The list of all receptionists read from the file.

    private void performDeletion(String receptionistIDToDelete, List<String> allReceptionists) throws IOException {
        // 1. Remove from receptionist list and rewrite receptionists.txt
        List<String> remainingReceptionists = allReceptionists.stream()
                .filter(r -> !r.toLowerCase().startsWith(receptionistIDToDelete.toLowerCase() + ","))
                .collect(Collectors.toList());

        try (FileWriter receptionistWriter = new FileWriter("receptionists.txt", false)) {
            for (String r : remainingReceptionists) {
                receptionistWriter.write(r + System.lineSeparator());
            }
        }

        // 2. Remove from users list and rewrite users.txt
        File userFile = new File("users.txt");
        if (userFile.exists()) {
            List<String> users = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
                String line;
                while((line = br.readLine()) != null) {
                    users.add(line);
                }
            }

            users.removeIf(user -> user.toLowerCase().startsWith(receptionistIDToDelete.toLowerCase() + ","));

            try (FileWriter userWriter = new FileWriter("users.txt", false)) {
                for(String user : users) {
                    userWriter.write(user + System.lineSeparator());
                }
            }
        }
    }


    // Static method to create and show the GUI. This is the entry point.

    public static void showDeleteGUI() {
        SwingUtilities.invokeLater(() -> {
            DeleteReceptionist gui = new DeleteReceptionist();
            gui.setVisible(true);
        });
    }


    // Main method for testing this component independently.

    public static void main(String[] args) {
        // To test this GUI, you would call this static method.
        showDeleteGUI();
    }
}