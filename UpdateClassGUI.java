import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class UpdateClassGUI extends JPanel implements ActionListener {
    JTextField classIDField, NewDataField;
    JButton UpdateButton;
    String username;
    JComboBox<String> PickData;
    MyFrame parentFrame;

    UpdateClassGUI(String username, MyFrame parentFrame) {
        this.username = username;
        this.parentFrame = parentFrame;
        this.setLayout(new GridLayout(4, 2, 7, 10));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel classIDLabel = new JLabel("Class ID:");
        classIDField = new JTextField();
        this.add(classIDLabel);
        this.add(classIDField);

        String[] Data = {"Subject", "Level", "Charges", "schedule"};
        JLabel DataLabel = new JLabel("Pick data to update:");
        PickData = new JComboBox<>(Data);
        this.add(DataLabel);
        this.add(PickData);

        JLabel NewDataLabel = new JLabel("Enter new info:");
        NewDataField = new JTextField();
        this.add(NewDataLabel);
        this.add(NewDataField);

        UpdateButton = new JButton("Update Class");
        UpdateButton.addActionListener(this);
        this.add(new JPanel());
        this.add(UpdateButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == UpdateButton) {
            String classID = classIDField.getText();
            String newData = NewDataField.getText();
            String textFile = "class.txt";

            // Data index of wanted data is always +1 because in text file first data(data[0]) is class id
            int dataIndex = PickData.getSelectedIndex() + 1;

            if (classID.isEmpty() || newData.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                Update.updateData(dataIndex, classID, newData, textFile);
                JOptionPane.showMessageDialog(this, "Class updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                parentFrame.showWelcomePanel();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to update class: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
