import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class receptionistOption_2 extends JPanel implements ActionListener {

    private JButton enrollButton;
    private JButton unenrollButton;
    private JButton replaceButton;
    private JButton viewEnrollmentButton;
    private JButton browseSubjectsButton;
    private JButton backButton;
    private JTextArea messageArea;
    private JScrollPane scrollPane;

    private static final List<String> VALID_SUBJECTS = Arrays.asList( //array for the list of subjects there's 11 total
            "Bahasa Malaysia", "English", "History", "Mathematics", "Additional Mathematics",
            "Science", "Accounting", "Moral", "Biology", "Chemistry", "Physics"
    );

    private receptionistMenu receptionistMenuFrame; //to enable cardlayout back

    public receptionistOption_2(JFrame frame) {
        this.receptionistMenuFrame = (receptionistMenu) frame;

        // Create components and buttons for enrolling student (3 subjects max)
        enrollButton = new JButton("Enroll Student in Subject");
        unenrollButton = new JButton("Unenroll Student from Subject");
        replaceButton = new JButton("Replace a Subject");
        viewEnrollmentButton = new JButton("View Student's Enrollment");
        browseSubjectsButton = new JButton("Browse All Subjects");
        backButton = new JButton("Back to Receptionist Menu");
        messageArea = new JTextArea(15, 40);
        messageArea.setEditable(false);
        scrollPane = new JScrollPane(messageArea);

        // Set action listeners
        enrollButton.addActionListener(this);
        unenrollButton.addActionListener(this);
        replaceButton.addActionListener(this);
        viewEnrollmentButton.addActionListener(this);
        browseSubjectsButton.addActionListener(this);
        backButton.addActionListener(this);

        // Create layout
        setLayout(new FlowLayout());

        add(enrollButton);
        add(unenrollButton);
        add(replaceButton);
        add(viewEnrollmentButton);
        add(browseSubjectsButton);
        add(backButton);
        add(scrollPane);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == enrollButton) { //will call the GUI for enrolling students (if subjects less than 3)
            enrollStudentInSubjectGUI();
        } else if (e.getSource() == unenrollButton) { //will call GUI to unenroll student from specific subject
            unenrollStudentFromSubjectGUI();
        } else if (e.getSource() == replaceButton) { //to change a students existing subject to something else
            replaceSubjectGUI();
        } else if (e.getSource() == viewEnrollmentButton) { //displays a students enrollment from enrollments.txt
            viewStudentEnrollmentGUI();
        } else if (e.getSource() == browseSubjectsButton) { //displays a hardcoded set of subjects available (total 11)
            browseAllSubjectsGUI();
        } else if (e.getSource() == backButton) { //button to back
            goBackToReceptionistMenu();
        }
    }

    private void enrollStudentInSubjectGUI() {
        JFrame enrollFrame = new JFrame("Enroll Student in Subject");
        enrollFrame.setSize(400, 300);
        enrollFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        enrollFrame.setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        JLabel studentIdLabel = new JLabel("Student ID:");
        JTextField studentIdField = new JTextField(20);
        JButton submitButton = new JButton("Submit");

        //panels to hold the input
        panel.add(studentIdLabel);
        panel.add(studentIdField);
        panel.add(new JLabel());
        panel.add(new JLabel());
        panel.add(new JLabel());
        panel.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentID = studentIdField.getText();
                int studentLevel = getStudentLevel(studentID);
                if (studentLevel == -1) {
                    messageArea.append("Student level not found, please register the student again.\n");
                    return;
                }
                // Display available subjects
                displayValidSubjectsGUI();
                String subjectID = getValidSubjectAndGenerateIDGUI(studentLevel, enrollFrame);

                try {
                    // Read the enrollments file
                    List<String> enrollments = new ArrayList<>();
                    try (BufferedReader br = new BufferedReader(new FileReader("enrollments.txt"))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            enrollments.add(line);
                        }
                    }
                    // find the student and update their enrollment
                    boolean studentFound = false;
                    for (int i = 0; i < enrollments.size(); i++) {
                        String[] enrollmentData = enrollments.get(i).split(",");
                        if (enrollmentData[0].equals(studentID)) {
                            studentFound = true;
                            // Check if the student is already enrolled in 3 subjects
                            int enrolledSubjects = 0;
                            for (int j = 1; j < enrollmentData.length - 1; j++) { // -1 to exclude enrollment date
                                if (enrollmentData[j] != null && !enrollmentData[j].isEmpty()) {
                                    enrolledSubjects++;
                                }
                            }
                            if (enrolledSubjects < 3) {
                                // find the first empty subject slot and enroll the student
                                // better just in case any of the slots have an empty string and not in order
                                boolean enrolled = false;
                                for (int j = 1; j < enrollmentData.length - 1; j++) {  // -1 to exclude enrollment date
                                    if (enrollmentData[j] == null || enrollmentData[j].isEmpty()) {
                                        enrollmentData[j] = subjectID;
                                        enrollments.set(i, String.join(",", enrollmentData));
                                        messageArea.append("Enrolled student " + studentID + " in subject " + subjectID + "\n");
                                        enrolled = true;
                                        break;
                                    }
                                }
                                if (!enrolled) {
                                    messageArea.append("Student " + studentID + " is already enrolled in subjects in all available slots.\n");
                                }
                            } else {
                                messageArea.append("Student " + studentID + " is already enrolled in the maximum number of subjects.\n");
                            }
                            break;
                        }
                    }
                    if (!studentFound) {
                        messageArea.append("Student with ID " + studentID + " not found.\n");
                    }
                    // Write the updated list back to the file
                    try (FileWriter fw = new FileWriter("enrollments.txt")) {
                        for (String enrollment : enrollments) {
                            fw.write(enrollment + "\n");
                        }
                    }
                } catch (IOException ex) {
                    messageArea.append("Error reading/writing enrollment file: " + ex.getMessage() + "\n");
                }
                enrollFrame.dispose();
            }
        });

        enrollFrame.add(panel);
        enrollFrame.setVisible(true);
    }

    private void unenrollStudentFromSubjectGUI() {
        JFrame unenrollFrame = new JFrame("Unenroll Student from Subject");
        unenrollFrame.setSize(400, 300);
        unenrollFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        unenrollFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        JLabel studentIdLabel = new JLabel("Student ID:");
        JTextField studentIdField = new JTextField(20);
        JLabel subjectIdLabel = new JLabel("Subject ID to unenroll:");
        JTextField subjectIdField = new JTextField(20);

        JButton submitButton = new JButton("Submit");

        panel.add(studentIdLabel);
        panel.add(studentIdField);
        panel.add(subjectIdLabel);
        panel.add(subjectIdField);
        panel.add(new JLabel());
        panel.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentID = studentIdField.getText();
                String subjectID = subjectIdField.getText();

                try {
                    // Read the enrollments file
                    List<String> enrollments = new ArrayList<>();
                    try (BufferedReader br = new BufferedReader(new FileReader("enrollments.txt"))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            enrollments.add(line);
                        }
                    }

                    // find the student and update their enrollment
                    boolean studentFound = false;
                    boolean subjectFound = false;
                    for (int i = 0; i < enrollments.size(); i++) {
                        String[] enrollmentData = enrollments.get(i).split(",");
                        if (enrollmentData[0].equals(studentID)) {
                            studentFound = true;
                            // Find the subject ID and unenroll the student
                            for (int j = 1; j < enrollmentData.length - 1; j++) {  // -1 to exclude enrollment date
                                if (subjectID.equals(enrollmentData[j])) {
                                    enrollmentData[j] = ""; // Set the subject ID to empty to unenroll
                                    enrollments.set(i, String.join(",", enrollmentData));
                                    messageArea.append("Unenrolled student " + studentID + " from subject " + subjectID + "\n");
                                    subjectFound = true;
                                    break;
                                }
                            }
                            if (!subjectFound) {
                                messageArea.append("Subject " + subjectID + " not found for student " + studentID + "\n");
                            }
                            break;
                        }
                    }

                    if (!studentFound) {
                        messageArea.append("Student with ID " + studentID + " not found.\n");
                    }

                    // Write the updated list back to the file
                    try (FileWriter fw = new FileWriter("enrollments.txt")) {
                        for (String enrollment : enrollments) {
                            fw.write(enrollment + "\n");
                        }
                    }

                } catch (IOException ex) {
                    messageArea.append("Error reading/writing enrollment file: " + ex.getMessage() + "\n");
                }
                unenrollFrame.dispose();
            }
        });

        unenrollFrame.add(panel);
        unenrollFrame.setVisible(true);
    }

    private void replaceSubjectGUI() { // function to replace a string in the enrollments file
        JFrame replaceFrame = new JFrame("Replace Subject");
        replaceFrame.setSize(400, 300);
        replaceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        replaceFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        JLabel studentIdLabel = new JLabel("Student ID:");
        JTextField studentIdField = new JTextField(20);
        JLabel oldSubjectIdLabel = new JLabel("Old Subject ID:");
        JTextField oldSubjectIdField = new JTextField(20);

        JButton submitButton = new JButton("Submit");

        panel.add(studentIdLabel);
        panel.add(studentIdField);
        panel.add(oldSubjectIdLabel);
        panel.add(oldSubjectIdField);
        panel.add(new JLabel());
        panel.add(new JLabel());
        panel.add(new JLabel());
        panel.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentID = studentIdField.getText();

                int studentLevel = getStudentLevel(studentID);
                if (studentLevel == -1) {
                    messageArea.append("Student level not found, please register the student again.\n");
                    return;
                }
                String oldSubjectID = oldSubjectIdField.getText();
                displayValidSubjectsGUI();
                JFrame frame = new JFrame();
                String newSubjectID = getValidSubjectAndGenerateIDGUI(studentLevel, frame);

                try {
                    // Read the enrollments file
                    List<String> enrollments = new ArrayList<>();
                    try (BufferedReader br = new BufferedReader(new FileReader("enrollments.txt"))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            enrollments.add(line);
                        }
                    }

                    // Find the student and update their enrollment
                    boolean studentFound = false;
                    boolean oldSubjectFound = false;
                    for (int i = 0; i < enrollments.size(); i++) {
                        String[] enrollmentData = enrollments.get(i).split(",");
                        if (enrollmentData[0].equals(studentID)) {
                            studentFound = true;
                            // Find the old subject ID and replace it with the new subject ID
                            for (int j = 1; j < enrollmentData.length - 1; j++) {  // -1 to exclude enrollment date
                                if (oldSubjectID.equals(enrollmentData[j])) {
                                    enrollmentData[j] = newSubjectID;
                                    enrollments.set(i, String.join(",", enrollmentData));
                                    messageArea.append("Replaced subject " + oldSubjectID + " with " + newSubjectID + " for student " + studentID + "\n");
                                    oldSubjectFound = true;
                                    break;
                                }
                            }
                            if (!oldSubjectFound) {
                                messageArea.append("Old subject " + oldSubjectID + " not found for student " + studentID + "\n");
                            }
                            break;
                        }
                    }

                    if (!studentFound) {
                        messageArea.append("Student with ID " + studentID + " not found.\n");
                    }

                    // Write the updated list back to the file
                    try (FileWriter fw = new FileWriter("enrollments.txt")) {
                        for (String enrollment : enrollments) {
                            fw.write(enrollment + "\n");
                        }
                    }

                } catch (IOException ex) {
                    messageArea.append("Error reading/writing enrollment file: " + ex.getMessage() + "\n");
                }
                replaceFrame.dispose();
            }
        });

        replaceFrame.add(panel);
        replaceFrame.setVisible(true);
    }

    private void viewStudentEnrollmentGUI() { //to view studentt enrollmen by entering student ID
        JFrame viewFrame = new JFrame("View Student Enrollment");
        viewFrame.setSize(400, 200);
        viewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        viewFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));

        JLabel studentIdLabel = new JLabel("Student ID:");
        JTextField studentIdField = new JTextField(20);

        JButton submitButton = new JButton("Submit");

        panel.add(studentIdLabel);
        panel.add(studentIdField);
        panel.add(new JLabel());
        panel.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentID = studentIdField.getText();

                try (BufferedReader br = new BufferedReader(new FileReader("enrollments.txt"))) {
                    String line;
                    boolean studentFound = false;
                    messageArea.setText("");
                    while ((line = br.readLine()) != null) {
                        String[] enrollmentData = line.split(",");
                        if (enrollmentData[0].equals(studentID)) {
                            studentFound = true;
                            messageArea.append("Enrollment details for student " + studentID + ":\n");
                            // Loop through the subject IDs to find the right one
                            for (int j = 1; j < enrollmentData.length - 1; j++) {  // Exclude the last element (enrollment date) because not needed
                                if (enrollmentData[j] != null && !enrollmentData[j].isEmpty()) {
                                    messageArea.append("Subject ID: " + enrollmentData[j] + "\n");
                                }
                            }
                            // Display Enrollment date
                            messageArea.append("Enrollment Date: " + enrollmentData[enrollmentData.length - 1] + "\n"); //Get the last part for enrollment date
                            break;
                        }
                    }
                    if (!studentFound) {
                        messageArea.append("No enrollment records found for student ID: " + studentID + "\n"); //if the id entered isnt in enrollments.txt
                    }
                } catch (IOException ex) {
                    messageArea.append("Error reading enrollment file: " + ex.getMessage() + "\n");
                }
                viewFrame.dispose(); //close the frame
            }
        });
        viewFrame.add(panel);
        viewFrame.setVisible(true);
    }

    private void browseAllSubjectsGUI() { //subject name and their description
        messageArea.setText("");
        messageArea.append("\n--- Available Subjects with Descriptions ---\n");
        messageArea.append("Bahasa Malaysia - National language of Malaysia.\n");
        messageArea.append("English - International language for communication.\n");
        messageArea.append("History - Study of past events.\n");
        messageArea.append("Mathematics - Foundation for quantitative reasoning.\n");
        messageArea.append("Additional Mathematics - Advanced mathematical concepts.\n");
        messageArea.append("Science - Exploration of the natural world.\n");
        messageArea.append("Accounting - Principles of financial record-keeping.\n");
        messageArea.append("Moral - Ethical principles and values.\n");
        messageArea.append("Biology - Study of living organisms.\n");
        messageArea.append("Chemistry - Study of matter and its properties.\n");
        messageArea.append("Physics - Study of the fundamental laws of the universe.\n");
        messageArea.append("--------------------------------------------------\n");
    }

    private void goBackToReceptionistMenu() {
        CardLayout cardLayout = (CardLayout) receptionistMenuFrame.getContentPane().getLayout();
        cardLayout.show(receptionistMenuFrame.getContentPane(), "receptionistMenu");
    }

    //helper methods
    private int getStudentLevel(String studentID) {
        try (BufferedReader br = new BufferedReader(new FileReader("students.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] studentData = line.split(",");
                if (studentData[0].equals(studentID)) {
                    try {
                        return Integer.parseInt(studentData[5]); // Student level is at index 5
                    } catch (NumberFormatException e) {
                        messageArea.append("Invalid student level format in students.txt\n");
                        return -1;
                    }
                }
            }
            messageArea.append("Student " + studentID + " not found in student.txt\n");
            return -1; // Student not found
        } catch (IOException e) {
            messageArea.append("Error reading students.txt: " + e.getMessage() + "\n");
            return -1;
        }
    }

    private String getValidSubjectAndGenerateIDGUI(int level, JFrame frame) {
        String subjectID = "";
        String subject = (String) JOptionPane.showInputDialog(
                frame,
                "Enter Subject to Enroll (blank if empty):",
                "Subject Selection",
                JOptionPane.QUESTION_MESSAGE,
                null,
                VALID_SUBJECTS.toArray(),
                null);

        if (subject == null || subject.isEmpty()) {
            return "";
        }

        if (!VALID_SUBJECTS.contains(subject)) {
            JOptionPane.showMessageDialog(this, "Invalid subject. Please choose from the following:\n" + String.join("\n", VALID_SUBJECTS));
            return "";
        }
        //If subject is valid, assign it a subject ID

        if (subject.equals("Bahasa Malaysia")) {
            subjectID = "BM" + level;
        } else if (subject.equals("English")) {
            subjectID = "E" + level;
        } else if (subject.equals("History")) {
            subjectID = "H" + level;
        } else if (subject.equals("Mathematics")) {
            subjectID = "M" + level;
        } else if (subject.equals("Additional Mathematics")) {
            subjectID = "AM" + level;
        } else if (subject.equals("Science")) {
            subjectID = "S" + level;
        } else if (subject.equals("Accounting")) {
            subjectID = "A" + level;
        } else if (subject.equals("Moral")) {
            subjectID = "MOR" + level;
        } else if (subject.equals("Biology")) {
            subjectID = "B" + level;
        } else if (subject.equals("Chemistry")) {
            subjectID = "C" + level;
        } else if (subject.equals("Physics")) {
            subjectID = "P" + level;
        }
        return subjectID;
    }

    private void displayValidSubjectsGUI() {
        String message = "\n--- Available Subjects ---\n";
        for (String subject : VALID_SUBJECTS) {
            message += "- " + subject + "\n";
        }
        message += "--------------------------\n";
        messageArea.append(message);
    }

    public static receptionistOption_2 createInstance(JFrame frame) { // Pass the parent frame
        return new receptionistOption_2(frame);
    }
}