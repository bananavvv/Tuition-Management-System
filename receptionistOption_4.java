import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class receptionistOption_4 extends JPanel implements ActionListener {

    private JTextField studentIdField;
    private JButton deleteButton;
    private JTextArea messageArea;
    private JScrollPane scrollPane;
    private JButton backButton;

    private static int studentCount = 0;
    private receptionistMenu receptionistMenuFrame;

    public receptionistOption_4(JFrame frame) {
        this.receptionistMenuFrame = (receptionistMenu) frame;

        // Create components and buttons for deletion
        JLabel studentIdLabel = new JLabel("Student ID to Delete:");
        studentIdField = new JTextField(10);
        deleteButton = new JButton("Delete Student");
        backButton = new JButton("Back to Receptionist Menu");
        messageArea = new JTextArea(15, 40);
        messageArea.setEditable(false);
        scrollPane = new JScrollPane(messageArea);

        // Set action listeners so can click
        deleteButton.addActionListener(this);
        backButton.addActionListener(this);

        // Create layout
        setLayout(new FlowLayout());

        add(studentIdLabel);
        add(studentIdField);
        add(deleteButton);
        add(backButton);
        add(scrollPane);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == deleteButton) {
            deleteStudentGUI();
        } else if (e.getSource() == backButton) {
            goBackToReceptionistMenu();
        }
    }

    private void deleteStudentGUI() {
        String studentIDToDelete = studentIdField.getText();
        messageArea.setText(""); // Clear previous messages
        try {
            // Read all students into a list
            List<String> students = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader("students.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    students.add(line);
                }
            }

            // Find the student and display information
            boolean studentFound = false;
            String studentDetails = null;
            for (String student : students) {
                String[] studentData = student.split(",");
                if (studentData.length > 0 && studentData[0].equals(studentIDToDelete)) {
                    studentFound = true;
                    studentDetails = student;
                    messageArea.append("\nStudent Details:\n");
                    messageArea.append("--------------------\n");
                    messageArea.append("Student ID: " + studentData[0] + "\n");
                    messageArea.append("Name: " + studentData[1] + "\n");
                    messageArea.append("IC Number: " + studentData[2] + "\n");
                    messageArea.append("Email: " + studentData[3] + "\n");
                    messageArea.append("Contact: " + studentData[4] + "\n");
                    messageArea.append("Level: " + studentData[5] + "\n");
                    messageArea.append("City: " + studentData[6] + "\n");
                    messageArea.append("--------------------\n");
                    break;
                }
            }

            if (!studentFound) {
                messageArea.append("Student with ID " + studentIDToDelete + " not found.\n");
                return;
            }

            // Confirm deletion using JOptionPane
            int confirmation = JOptionPane.showConfirmDialog( //confirm if you actually want to delete this student account
                    this,
                    "Are you sure you want to delete this student?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION);

            if (confirmation == JOptionPane.YES_OPTION) {
                // Remove the student from the list
                students.removeIf(student -> student.startsWith(studentIDToDelete + ","));

                // Write the updated list back to the file
                try (FileWriter fw = new FileWriter("students.txt")) {
                    for (String student : students) {
                        fw.write(student + "\n");
                    }
                }

                // delete from enrollments.txt also
                List<String> enrollments = new ArrayList<>();
                try (BufferedReader br = new BufferedReader(new FileReader("enrollments.txt"))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        enrollments.add(line);
                    }
                }
                enrollments.removeIf(enrollment -> enrollment.startsWith(studentIDToDelete + ","));
                try (FileWriter fw = new FileWriter("enrollments.txt")) {
                    for (String enrollment : enrollments) {
                        fw.write(enrollment + "\n");
                    }
                }
                messageArea.append("Student with ID " + studentIDToDelete + " has been deleted.\n");

                // delete from users.txt
                removeUserFromUsers(studentIDToDelete); //Call the method to remove the student from users.txt
                // If the deleted student was the last one, update studentCount
                if (studentIDToDelete.equals(String.format("S%02d", studentCount))) {
                    studentCount--;
                }
            } else {
                messageArea.append("Deletion cancelled.\n");
            }
        } catch (IOException e) {
            messageArea.append("An error occurred: " + e.getMessage() + "\n");
        }
    }


    private void removeUserFromUsers(String studentID) {
        //remove student from user.txt
        try {
            List<String> users = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    users.add(line);
                }
            }
            // Remove the user from the list
            users.removeIf(user -> {
                String[] userData = user.split(",");
                // Check if username starts with the Student id, in case the name is the same as the id.
                return userData[0].startsWith(studentID);
            });
            // Write the updated list back to the file
            try (FileWriter fw = new FileWriter("users.txt")) {
                for (String user : users) {
                    fw.write(user + "\n");
                }
            }

        } catch (IOException e) {
            messageArea.append("An error occurred while trying to modify user.txt: " + e.getMessage() + "\n");
        }
    }

    private void goBackToReceptionistMenu() {
        CardLayout cardLayout = (CardLayout) receptionistMenuFrame.getContentPane().getLayout();
        cardLayout.show(receptionistMenuFrame.getContentPane(), "receptionistMenu");
    }

    public static receptionistOption_4 createInstance(JFrame frame) {
        return new receptionistOption_4(frame);
    }
}