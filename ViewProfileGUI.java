import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ViewProfileGUI extends JPanel {

    private JLabel idLabel;
    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel phoneLabel;
    private JLabel formLabel;
    private JLabel subjectLabel;

    public ViewProfileGUI(String username, MyFrame parentFrame) {
        super(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel profilePanel = new JPanel(new GridLayout(6, 2, 10, 10));

        tutor tutor = loadTutorFromFile("tutors.txt", username);

        if (tutor != null) {
            profilePanel.add(new JLabel("Tutor ID:"));
            idLabel = new JLabel(tutor.getId());
            profilePanel.add(idLabel);

            profilePanel.add(new JLabel("Name:"));
            nameLabel = new JLabel(tutor.getName());
            profilePanel.add(nameLabel);


            profilePanel.add(new JLabel("email:"));
            phoneLabel = new JLabel(tutor.getPhone());
            profilePanel.add(phoneLabel);

            profilePanel.add(new JLabel("phone:"));
            emailLabel = new JLabel(tutor.getEmail());
            profilePanel.add(emailLabel);

            profilePanel.add(new JLabel("Form Level:"));
            formLabel = new JLabel(tutor.getFormLevel());
            profilePanel.add(formLabel);

            profilePanel.add(new JLabel("Subject:"));
            subjectLabel = new JLabel(tutor.getSubject());
            profilePanel.add(subjectLabel);

        } else { //Display if cannot find ID
            profilePanel.add(new JLabel("Error: Tutor with username '" + username + "' not found."));
        }

        this.add(new JLabel("Tutor Profile", SwingConstants.CENTER), BorderLayout.NORTH);
        this.add(profilePanel, BorderLayout.CENTER);
    }

    // Get tutor info from the tutors text file
    private tutor loadTutorFromFile(String fileName, String username) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 1 && parts[1].trim().equals(username)) {
                    if (parts.length == 6) {
                        return new tutor(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(), parts[4].trim(), parts[5].trim());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading file: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
}