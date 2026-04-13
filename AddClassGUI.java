import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class AddClassGUI extends JPanel implements ActionListener {
    JTextField classIDField, chargesField, scheduleField;
    JButton addButton;
    String username;
    JComboBox<String> PickSubject;
    JComboBox<String> PickLevel;
    MyFrame parentFrame;

    String[] Subject = {"Mathematics", "English", "Science", "Bahasa Malaysia", "Accounting", "Moral", "History", "Physics", "Biology", "Chemistry", "Additional Mathematics"};
    String[] Level = {"Form 1", "Form 2", "Form 3", "Form 4", "Form 5"};

    AddClassGUI(String username, MyFrame parentFrame) {
        this.username = username;
        this.parentFrame = parentFrame;
        this.setLayout(new GridLayout(6, 2, 10, 10)); // Grid layout for form fields
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel classIDLabel = new JLabel("Class ID:");
        classIDField = new JTextField();
        this.add(classIDLabel);
        this.add(classIDField);

        JLabel subjectLabel = new JLabel("Subject:");
        PickSubject = new JComboBox<>(Subject);
        this.add(subjectLabel);
        this.add(PickSubject);

        JLabel levelLabel = new JLabel("Level:");
        PickLevel = new JComboBox<>(Level);
        this.add(levelLabel);
        this.add(PickLevel);

        JLabel chargesLabel = new JLabel("Charges:");
        chargesField = new JTextField();
        this.add(chargesLabel);
        this.add(chargesField);

        JLabel scheduleLabel = new JLabel("Schedule:");
        scheduleField = new JTextField();
        this.add(scheduleLabel);
        this.add(scheduleField);

        addButton = new JButton("Add Class");
        addButton.addActionListener(this);
        this.add(new JPanel()); // Empty panel for spacing
        this.add(addButton);
    }

    private boolean isTutorQualified(String tutorUsername, String level, String subject) {
        try (BufferedReader reader = new BufferedReader(new FileReader("tutors.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                // Ensure the line has enough data and the username matches
                if (data.length >= 6 && data[1].trim().equals(tutorUsername)) {
                    String qualifiedLevel = data[4].trim(); // Get the qualified level from the file
                    String qualifiedSubject = data[5].trim(); // Get the qualified subject from the file

                    // Compare the trimmed strings.
                    if (qualifiedLevel.equals(level) && qualifiedSubject.equals(subject)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading tutor file: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            String classID = classIDField.getText();
            String subject = (String) PickSubject.getSelectedItem();
            String level = (String) PickLevel.getSelectedItem();
            String charges = chargesField.getText();
            String schedule = scheduleField.getText();


            // Check to make sure boxes are not empty
            if (classID.isEmpty() || subject.isEmpty() || level.isEmpty() ||charges.isEmpty() ||schedule.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;}

            if (!isTutorQualified(username, level, subject)) {
                JOptionPane.showMessageDialog(this, "You are not qualified to teach this subject and level.", "Qualification Error", JOptionPane.ERROR_MESSAGE);
                return;}

            try {
                Class.addClass(classID, subject, level, charges, schedule, username);
                JOptionPane.showMessageDialog(this, "Class added successfully!");
                parentFrame.showWelcomePanel(); // Go back to the welcome screen
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}