import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ScheduleViewer {
    private final String studentID;

    public ScheduleViewer(String studentID) {
        this.studentID = studentID;
    }

    public void showSchedule() {
        JFrame frame = new JFrame("Class Schedule");
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
        JTextArea display = new JTextArea();
        display.setEditable(false);

        List<String> classIDs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("enrollments.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                for (int i = 0; i < parts.length; i++) {
                    if (parts[i].equals(studentID)) {
                        classIDs.add(parts[1]);
                    }
                }
            }
        } catch (IOException e) {
            display.setText("Error reading enrollments.txt: " + e.getMessage());
        }

        try (BufferedReader reader = new BufferedReader(new FileReader("class.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (classIDs.contains(parts[0])) {
                    display.append("Subject: " + parts[1] + "\nLevel: " + parts[2] + "\nSchedule: " + parts[4] + "\nTutor: " + parts[5] + "\n\n");
                }
            }
        } catch (IOException e) {
            display.append("Error reading class.txt: " + e.getMessage());
        }

        frame.add(new JScrollPane(display));
        frame.setVisible(true);
    }
}