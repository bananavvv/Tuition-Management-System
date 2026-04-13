import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ViewStudentGUI extends JPanel {

    private String username;
    private JTable studentTable;
    private JScrollPane scrollPane;
    private DefaultTableModel tableModel;
    private MyFrame parentFrame;

    public ViewStudentGUI(String username, MyFrame parentFrame) {
        this.username = username;
        this.parentFrame = parentFrame;
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Set up the table model
        String[] columnNames = {"Student ID"};
        tableModel = new DefaultTableModel(columnNames, 0); // 0 rows initially
        studentTable = new JTable(tableModel);
        scrollPane = new JScrollPane(studentTable);

        loadStudentData();

        this.add(new JLabel("Enrolled Students for your classes:", SwingConstants.CENTER), BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    private void loadStudentData() {
        tableModel.setRowCount(0);
        List<String> studentIDsInTutorClasses = new ArrayList<>();
        List<String> classIDsForTutor = new ArrayList<>();

        // get class id taught by the user(tutor)
        try (BufferedReader classReader = new BufferedReader(new FileReader("class.txt"))) {
            String line;
            while ((line = classReader.readLine()) != null) {
                String[] dataArray = line.split(",");
                if (dataArray.length > 5 && dataArray[5].trim().equals(username)) {
                    classIDsForTutor.add(dataArray[0].trim());
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading class file: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (classIDsForTutor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No classes found for this tutor.", "No Data", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Read enrollment file check if any students in tutor class
        try (BufferedReader enrollmentReader = new BufferedReader(new FileReader("enrollments.txt"))) {
            String line;
            while ((line = enrollmentReader.readLine()) != null) {
                String[] dataArray = line.split(",");
                if (dataArray.length > 0) {
                    String studentId = dataArray[0].trim();

                    // Iterate through the rest of the elements (class IDs) for this student
                    for (int i = 1; i < dataArray.length; i++) {
                        String studentClassId = dataArray[i].trim();
                        if (classIDsForTutor.contains(studentClassId)) {
                            if (!studentIDsInTutorClasses.contains(studentId)) {
                                studentIDsInTutorClasses.add(studentId);
                            }}
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading enrollments file: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (studentIDsInTutorClasses.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No students enrolled in your classes.", "No Data", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (String studentID : studentIDsInTutorClasses) {
                tableModel.addRow(new Object[]{studentID});
            }
        }
    }
}