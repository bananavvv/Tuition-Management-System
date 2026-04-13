import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class receptionistOption_1 extends JPanel implements ActionListener {
    private JButton registerStudentButton;
    private JButton displayStudentsButton;
    private JButton backButton;
    private JTextArea messageArea;
    private JScrollPane scrollPane;

    private static int studentCount = 0;
    private static final List<String> VALID_SUBJECTS = Arrays.asList(
            "Bahasa Malaysia", "English", "History", "Mathematics", "Additional Mathematics",
            "Science", "Accounting", "Moral", "Biology", "Chemistry", "Physics"
    );


    private receptionistMenu receptionistMenuFrame; //to enable cardlayout back

    public receptionistOption_1(JFrame frame) { // Pass the parent frame
        this.receptionistMenuFrame = (receptionistMenu) frame;

        // Create buttons to reigster, display all the students and go back
        registerStudentButton = new JButton("Register a New Student & Enroll");
        displayStudentsButton = new JButton("Display All Students");
        backButton = new JButton("Back to Receptionist Menu");
        messageArea = new JTextArea(15, 40);
        messageArea.setEditable(false);
        scrollPane = new JScrollPane(messageArea);

        // set the action listeners
        registerStudentButton.addActionListener(this);
        displayStudentsButton.addActionListener(this);
        backButton.addActionListener(this);

        // create layout
        setLayout(new FlowLayout());

        add(registerStudentButton);
        add(displayStudentsButton);
        add(backButton);
        add(scrollPane);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerStudentButton) {
            registerNewStudentGUI();
        } else if (e.getSource() == displayStudentsButton) {
            displayAllStudentsGUI();
        } else if (e.getSource() == backButton) {
            goBackToReceptionistMenu();
        }
    }

    private void registerNewStudentGUI() {
        //create a new Jframe for registering new student
        JFrame registerFrame = new JFrame("Register New Student");
        registerFrame.setSize(500, 600);
        registerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        registerFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(14, 2));

        JLabel nameLabel = new JLabel("Student Name:");
        JTextField nameField = new JTextField(20);
        JLabel icLabel = new JLabel("Student IC Number:");
        JTextField icField = new JTextField(20);
        JLabel emailLabel = new JLabel("Student Email:");
        JTextField emailField = new JTextField(20);
        JLabel contactLabel = new JLabel("Student Contact:");
        JTextField contactField = new JTextField(20);
        JLabel levelLabel = new JLabel("Student Level (1-5):");
        JTextField levelField = new JTextField(20); // For level input can only accept integer
        JLabel cityLabel = new JLabel("City of Residence:");
        JTextField cityField = new JTextField(20);

        // Subject selection buttons and components
        JLabel subjectListLabel = new JLabel("Available Subjects:");
        JTextArea subjectListArea = new JTextArea();
        subjectListArea.setEditable(false);
        for (String subject : VALID_SUBJECTS) {
            subjectListArea.append("- " + subject + "\n");
        }
        JScrollPane subjectListScrollPane = new JScrollPane(subjectListArea);
        subjectListScrollPane.setPreferredSize(new Dimension(200, 100));

        JLabel subject1Label = new JLabel("Subject 1 (blank for none):");
        JTextField subject1Field = new JTextField(20);
        JLabel subject2Label = new JLabel("Subject 2 (blank for none):");
        JTextField subject2Field = new JTextField(20);
        JLabel subject3Label = new JLabel("Subject 3 (blank for none):");
        JTextField subject3Field = new JTextField(20);
        JLabel enrollmentDateLabel = new JLabel("Enrollment Date (MM/YYYY):");
        JTextField enrollmentDateField = new JTextField(20);
        JButton submitButton = new JButton("Submit");

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(icLabel);
        panel.add(icField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(contactLabel);
        panel.add(contactField);
        panel.add(levelLabel);
        panel.add(levelField);
        panel.add(cityLabel);
        panel.add(cityField);

        // Add Subject List
        panel.add(subjectListLabel);
        panel.add(subjectListScrollPane);

        panel.add(subject1Label);
        panel.add(subject1Field);
        panel.add(subject2Label);
        panel.add(subject2Field);
        panel.add(subject3Label);
        panel.add(subject3Field);
        panel.add(enrollmentDateLabel);
        panel.add(enrollmentDateField);
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(submitButton);

        //set Action Listener

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadStudentCount();
                try {
                    String studentName = nameField.getText();
                    String studentICNumber = icField.getText();
                    String studentEmail = emailField.getText();
                    String studentContact = contactField.getText();
                    String studentCity = cityField.getText();
                    String student_enrollment_date = enrollmentDateField.getText();
                    // validate the level input
                    int studentLevel;
                    try { //make sure the number is between 1 and 5 cuz form 1 to form 5 only
                        studentLevel = Integer.parseInt(levelField.getText());
                        if (studentLevel < 1 || studentLevel > 5) {
                            JOptionPane.showMessageDialog(registerFrame, "Invalid level. Please enter a number between 1 and 5.");
                            return; // Stop processing
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(registerFrame, "Invalid level. Please enter a number.");
                        return; // Stop processing
                    }

                    String subject_id_1 = getValidSubjectAndGenerateIDGUI(subject1Field.getText(), studentLevel);
                    String subject_id_2 = getValidSubjectAndGenerateIDGUI(subject2Field.getText(), studentLevel);
                    String subject_id_3 = getValidSubjectAndGenerateIDGUI(subject3Field.getText(), studentLevel);

                    studentCount = studentCount + 1; // Increment BEFORE using it
                    String studentID = String.format("S%02d", studentCount);

                    //have it so that the student password is the username + "0000" so that they can change it later
                    String studentUsername = studentName.replaceAll("\\s+", "");
                    //use the name for the username, remove spaces
                    String studentPassword = studentUsername + "0000"; //default password

                    // Write to students.txt
                    try (FileWriter studentsWriter = new FileWriter("students.txt", true)) {
                        studentsWriter.write(studentID + "," + studentName + "," + studentICNumber + "," + studentEmail + ","
                                + studentContact + "," + studentLevel + "," + studentCity + "\n");
                    }

                    // Write to users.txt
                    try (FileWriter usersWriter = new FileWriter("users.txt", true)) {
                        usersWriter.write(studentUsername + "," + studentPassword + "," + "student" + "\n");
                    }

                    // Prepare subject IDs.  Use a List for easier handling.
                    List<String> subjectIDsList = new ArrayList<>();
                    if (!subject_id_1.isEmpty()) subjectIDsList.add(subject_id_1);
                    else subjectIDsList.add(""); // Add empty string if blank
                    if (!subject_id_2.isEmpty()) subjectIDsList.add(subject_id_2);
                    else subjectIDsList.add(""); // Add empty string if blank
                    if (!subject_id_3.isEmpty()) subjectIDsList.add(subject_id_3);
                    else subjectIDsList.add(""); // Add empty string if blank

                    // Create a comma-separated string of subject IDs
                    String subjectIDs = String.join(",", subjectIDsList);


                    // Write to enrollments.txt
                    try (FileWriter enrollmentsWriter = new FileWriter("enrollments.txt", true)) {
                        enrollmentsWriter.write(studentID + "," + subjectIDs + "," + student_enrollment_date + "\n");
                    }
                    messageArea.append("Successfully Registered New Student with ID: " + studentID + "\n");
                    saveStudentCount();
                    registerFrame.dispose();
                } catch (IOException ex) {
                    messageArea.append("An error occurred while writing to the file: " + ex.getMessage() + "\n");

                }
            }
        });

        registerFrame.add(panel);
        registerFrame.setVisible(true);
    }

    private String getValidSubjectAndGenerateIDGUI(String subject, int level) {
        String subjectID = "";

        if (subject.isEmpty()) {
            return ""; // Return empty string if the user wants to skip
        }
        if (!VALID_SUBJECTS.contains(subject)) {
            JOptionPane.showMessageDialog(this, "Invalid subject. " +
                    "Please choose from the following:\n" + String.join("\n", VALID_SUBJECTS));
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


    // GUI Version of displayAllStudents
    private void displayAllStudentsGUI() {
        messageArea.setText(""); // Clear the message area
        try (BufferedReader reader = new BufferedReader(new FileReader("students.txt"))) {
            String line;
            messageArea.append("\n--- All Registered Students ---\n");
            while ((line = reader.readLine()) != null) {
                String[] studentData = line.split(",");
                if (studentData.length == 7) {
                    messageArea.append("Student ID: " + studentData[0] + "\n");
                    messageArea.append("Name: " + studentData[1] + "\n");
                    messageArea.append("IC Number: " + studentData[2] + "\n");
                    messageArea.append("Email: " + studentData[3] + "\n");
                    messageArea.append("Contact: " + studentData[4] + "\n");
                    messageArea.append("Level: " + studentData[5] + "\n");
                    messageArea.append("City: " + studentData[6] + "\n");
                    messageArea.append("-----------------------------\n");
                } else {
                    messageArea.append("Invalid student data format: " + line + "\n");
                }
            }
            messageArea.append("--- End of Student List ---\n");
        } catch (FileNotFoundException e) {
            messageArea.append("The students.txt file does not exist.\n");
        } catch (IOException e) {
            messageArea.append("An error occurred while reading the students.txt file: " + e.getMessage() + "\n");
        }
    }
    private void goBackToReceptionistMenu() {
        CardLayout cardLayout = (CardLayout) receptionistMenuFrame.getContentPane().getLayout();
        cardLayout.show(receptionistMenuFrame.getContentPane(), "receptionistMenu");
    }


    // Method to load the student count from a file (if it exists)
    private static void loadStudentCount() {
        try (BufferedReader reader = new BufferedReader(new FileReader("student_count.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                studentCount = Integer.parseInt(line);
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, so leave studentCount at 0
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading student count. Starting from S01. Check student_count.txt.");
            studentCount = 0; // Reset to 0 if there's an error reading the file
        }
    }


    // Method to save the student count to a file
    private static void saveStudentCount() {
        try (FileWriter writer = new FileWriter("student_count.txt")) {
            writer.write(String.valueOf(studentCount));
        } catch (IOException e) {
            System.err.println("Error saving student count: " + e.getMessage());
        }
    }


    public static receptionistOption_1 createInstance(JFrame frame) { // Pass the parent frame
        return new receptionistOption_1(frame);
    }
}