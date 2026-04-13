import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AdminOption2 extends JFrame implements ActionListener {

    private final JTextField tutorIdField;
    private final JButton findTutorButton;
    private final JTextArea tutorDetailsArea;
    private final JComboBox<String> formComboBox;
    private final JComboBox<String> subjectComboBox;
    private final JButton assignButton;
    private final JButton cancelButton;
    private final JPanel assignmentPanel;

    // These variables will hold the state, similar to the console application
    private List<String> fileLines; // Holds all lines from tutors.txt
    private int tutorLineIndex = -1; // The line number of the tutor to be updated
    private String[] tutorDetails; // The details of the found tutor


    public AdminOption2() {
        setTitle("---Assign Tutor to Subject and Level---");
        setSize(550, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- Top Panel for finding the tutor (replaces Scanner input for Tutor ID) ---
        JPanel findPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        findPanel.setBorder(BorderFactory.createTitledBorder("Step 1: Find Tutor"));
        findPanel.add(new JLabel("Input Tutor ID (e.g., T01):"));
        tutorIdField = new JTextField(10);
        findPanel.add(tutorIdField);
        findTutorButton = new JButton("Find Tutor");
        findTutorButton.addActionListener(this);
        findPanel.add(findTutorButton);

        // --- Center Panel for displaying tutor details and assignment ---
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        tutorDetailsArea = new JTextArea(5, 40);
        tutorDetailsArea.setEditable(false);
        tutorDetailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(tutorDetailsArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Tutor Credentials"));
        centerPanel.add(scrollPane);

        // --- Assignment Panel (initially hidden) ---
        assignmentPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        assignmentPanel.setBorder(BorderFactory.createTitledBorder("Step 2: Assign Form and Subject"));

        // Form selection (replaces Scanner input for form, provides validation)
        assignmentPanel.add(new JLabel("Select Form (1-5):"));
        String[] forms = {"1", "2", "3", "4", "5"};
        formComboBox = new JComboBox<>(forms);
        assignmentPanel.add(formComboBox);

        // Subject selection (replaces Scanner input for subject, provides validation)
        assignmentPanel.add(new JLabel("Select Subject:"));
        String[] subjects = {"Bahasa Melayu", "English", "History", "Mathematics", "Additional Mathematics" , "Science" , "Accounting" , "Moral" , "Biology" , "Chemistry", "Physics"};
        subjectComboBox = new JComboBox<>(subjects);
        assignmentPanel.add(subjectComboBox);

        assignButton = new JButton("Assign");
        cancelButton = new JButton("Cancel");
        assignButton.addActionListener(this);
        cancelButton.addActionListener(this);
        assignmentPanel.add(assignButton);
        assignmentPanel.add(cancelButton);

        assignmentPanel.setVisible(false); // Hide until a tutor is found
        centerPanel.add(assignmentPanel);

        // Add panels to frame
        add(findPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == findTutorButton) {
            handleFindTutor();
        } else if (e.getSource() == assignButton) {
            handleAssignTutor();
        } else if (e.getSource() == cancelButton) {
            this.dispose(); // Close the window
        }
    }


    // This method mirrors the first part of your console app's logic:
    // 1. Get Tutor ID.
    // 2. Read "tutors.txt" into a list.
    // 3. Find the tutor and their line number.

    private void handleFindTutor() {
        String tutorID = tutorIdField.getText().trim();
        if (tutorID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tutor ID cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        File inputFile = new File("tutors.txt");
        if (!inputFile.exists()) {
            JOptionPane.showMessageDialog(this, "tutors.txt not found. Cannot assign tutor.", "File Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Read all lines from the file, same as the console version
            fileLines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                fileLines.add(currentLine);
            }
            reader.close();

            // Find the tutor in the list, same as the console version
            boolean tutorFound = false;
            for (int i = 0; i < fileLines.size(); i++) {
                String[] parts = fileLines.get(i).split(",");
                if (parts.length > 0 && parts[0].equalsIgnoreCase(tutorID)) {
                    tutorDetails = parts;
                    tutorLineIndex = i;
                    tutorFound = true;
                    break;
                }
            }

            if (!tutorFound) {
                JOptionPane.showMessageDialog(this, "Tutor with ID '" + tutorID + "' not found.", "Not Found", JOptionPane.WARNING_MESSAGE);
                assignmentPanel.setVisible(false);
                tutorDetailsArea.setText("");
            } else {
                displayTutorDetails();
                confirmAndProceed(); // Proceed to confirmation steps
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "An error occurred while reading the file.", "IO Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }


    // Displays tutor details in the JTextArea, the GUI equivalent of printing to the console.

    private void displayTutorDetails() {
        StringBuilder detailsText = new StringBuilder();
        detailsText.append("--- Tutor Credentials ---\n");
        detailsText.append(String.format("ID: \t\t%s\n", tutorDetails[0]));
        detailsText.append(String.format("Name: \t\t%s\n", tutorDetails[1]));
        detailsText.append(String.format("Email: \t\t%s\n", tutorDetails[2]));
        detailsText.append(String.format("Contact: \t%s\n", tutorDetails[3]));
        detailsText.append("-------------------------\n");
        tutorDetailsArea.setText(detailsText.toString());
    }


    // This method mirrors the confirmation logic from the console app using dialog boxes.
    // 1. Asks "Is this the correct tutor?"
    // 2. Checks for existing assignments and asks to overwrite.

    private void confirmAndProceed() {
        // Replaces the "yes/no" console input
        int confirmation = JOptionPane.showConfirmDialog(this, "Is this the correct tutor?", "Confirm Tutor", JOptionPane.YES_NO_OPTION);

        if (confirmation != JOptionPane.YES_OPTION) {
            tutorDetailsArea.setText(""); // Clear details if user says no
            return;
        }

        // Checks for existing assignment (tutorDetails.length > 4), same as the console app
        if (tutorDetails.length > 4) {
            int overwrite = JOptionPane.showConfirmDialog(this, "This tutor is already assigned. Overwrite?", "Overwrite Confirmation", JOptionPane.YES_NO_OPTION);
            if (overwrite != JOptionPane.YES_OPTION) {
                assignmentPanel.setVisible(false);
                return;
            }
        }

        // If all checks pass, show the assignment panel
        assignmentPanel.setVisible(true);
    }


    // This method mirrors the final part of your console app's logic:
    // 1. Get the chosen Form and Subject from the UI.
    // 2. Construct the updated record string.
    // 3. Write the modified list back to "tutors.txt".

    private void handleAssignTutor() {
        String selectedForm = (String) formComboBox.getSelectedItem();
        String selectedSubject = (String) subjectComboBox.getSelectedItem();

        // Construct the new line for the file, same logic as the console app
        String updatedTutorRecord = String.join(",",
                tutorDetails[0],
                tutorDetails[1],
                tutorDetails[2],
                tutorDetails[3],
                "Form " + selectedForm,
                selectedSubject);

        // Update the specific line in our list
        fileLines.set(tutorLineIndex, updatedTutorRecord);

        // Write the entire updated list back to the file, same as the console app
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("tutors.txt"))) {
            for (String line : fileLines) {
                writer.write(line);
                writer.newLine();
            }
            JOptionPane.showMessageDialog(this, "Assignment Successful!\nTutor " + tutorDetails[1] + " assigned to Form " + selectedForm + " for " + selectedSubject + ".", "Success", JOptionPane.INFORMATION_MESSAGE);
            this.dispose(); // Close window on success

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "An error occurred while saving the assignment.", "IO Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // This ensures the GUI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(AdminOption2::new);
    }
}