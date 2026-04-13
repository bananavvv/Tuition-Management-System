import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DeleteClassGUI extends JPanel implements ActionListener {

    JTable classTable;
    DefaultTableModel tableModel;
    JButton deleteButton;
    String username;
    MyFrame parentFrame;

    // Headers for all the column of table
    String[] columnNames = {"Class ID", "Subject", "Level", "Charges", "Schedule", "Tutor"};

    DeleteClassGUI(String username, MyFrame parentFrame) {
        this.username = username;
        this.parentFrame = parentFrame;
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        tableModel = new DefaultTableModel(columnNames, 0) {
            // Make cells non-editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        classTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(classTable);
        loadClassData();

        // Add the table to the panel
        this.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        deleteButton = new JButton("Delete Selected Class");
        deleteButton.addActionListener(this);
        buttonPanel.add(deleteButton);

        // Add the button panel to the main panel
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadClassData() {
        // Clear existing data before loading
        tableModel.setRowCount(0);

        // Load all classes from the file
        try (BufferedReader reader = new BufferedReader(new FileReader("class.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] dataArray = line.split(",");
                // Make sure program only shows the tutors classes and not all classes
                if (dataArray.length == 6 && dataArray[5].equals(username)) {
                    tableModel.addRow(dataArray);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading class data.", "File Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == deleteButton) {
            int selectedRow = classTable.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a class to delete.", "Selection Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get the Class ID from the selected row
            String classID = (String) tableModel.getValueAt(selectedRow, 0);


             //Get confirmation
            int response = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete the class with ID: " + classID + "?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (response == JOptionPane.YES_OPTION) {
                Class.deleteClass(classID,username);
                JOptionPane.showMessageDialog(this, "Class with ID " + classID + " deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Refresh the table to show the updated data
                loadClassData();
            }
        }}
}

