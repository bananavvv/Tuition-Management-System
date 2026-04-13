import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class UpdateTutorGUI extends JPanel implements ActionListener {
    JTextField TutorIDField, NewDataField;
    JButton UpdateButton;
    String username;
    JComboBox<String> PickData;
    MyFrame parentFrame;

    UpdateTutorGUI(String username, MyFrame parentFrame) {
        this.username = username;
        this.parentFrame = parentFrame;
        this.setLayout(new GridLayout(4, 2, 7, 10));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel TutorIDLabel = new JLabel("Tutor ID:");
        TutorIDField = new JTextField();
        this.add(TutorIDLabel);
        this.add(TutorIDField);

        String[] Data = {"email", "phone number", "level"};
        JLabel DataLabel = new JLabel("Pick data to update:");
        PickData = new JComboBox<>(Data);
        this.add(DataLabel);
        this.add(PickData);

        JLabel NewDataLabel = new JLabel("Enter new info:");
        NewDataField = new JTextField();
        this.add(NewDataLabel);
        this.add(NewDataField);
        UpdateButton = new JButton("Update Tutor");
        UpdateButton.addActionListener(this);
        this.add(new JPanel());
        this.add(UpdateButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == UpdateButton) {
            String tutorID = TutorIDField.getText();
            String newData = NewDataField.getText();
            String textFile = "tutors.txt";

            // data index is plus 2 because data[o] and data[1] in tutors text file is tutor id and name
            int dataIndex = PickData.getSelectedIndex() + 2;

            //check to make sure box is not left empty
            if (tutorID.isEmpty() || newData.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                Update.updateData(dataIndex, tutorID, newData, textFile);
                JOptionPane.showMessageDialog(this, "Tutor updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                parentFrame.showWelcomePanel();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to update tutor: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
 }
}
}
