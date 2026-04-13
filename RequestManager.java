import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class RequestManager {
    private final String studentID;
    private DefaultTableModel tableModel;
    private JTable requestTable;

    public RequestManager(String studentID) {
        this.studentID = studentID;
    }

    public void requestMenu() {
        JFrame frame = new JFrame("Student Requests");
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        // Define the columns for the table
        String[] columnNames = {"Request", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make all cells non-editable
                return false;
            }
        };

        requestTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(requestTable);

        // Load initial data into the table
        loadTableData();

        // Panel for buttons
        JPanel btnPanel = new JPanel();
        JButton makeBtn = new JButton("Make Request");
        JButton deleteBtn = new JButton("Delete Selected Request");

        // Make new request action
        makeBtn.addActionListener(e -> {
            String req = JOptionPane.showInputDialog(frame, "Enter your new request:");
            if (req != null && !req.isEmpty()) {
                // Now we call a new helper method to add to the file
                if (addRequestToFile(req)) {
                    // And we update the table directly
                    tableModel.addRow(new Object[]{req, "Pending"});
                    JOptionPane.showMessageDialog(frame, "Request submitted successfully.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Error submitting request to file.");
                }
            }
        });

        // Delete request action
        deleteBtn.addActionListener(e -> {
            int selectedRow = requestTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame, "Please select a request to delete.");
                return;
            }

            // Get the request message from the selected row
            String requestMessage = (String) tableModel.getValueAt(selectedRow, 0);
            String requestStatus = (String) tableModel.getValueAt(selectedRow, 1);

            // Check if the request is pending before deletion
            if (!"Pending".equalsIgnoreCase(requestStatus)) {
                JOptionPane.showMessageDialog(frame, "You can only delete pending requests.");
                return;
            }

            // Confirm deletion
            int confirmation = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to delete the selected request?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmation == JOptionPane.YES_OPTION) {
                if (deleteRequestFromFile(requestMessage)) {
                    // Only update the table model directly if the file write was successful
                    tableModel.removeRow(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(frame, "Error deleting request from file.");
                }
            }
        });

        btnPanel.add(makeBtn);
        btnPanel.add(deleteBtn);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(btnPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }


    private boolean addRequestToFile(String request) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("requests.txt", true))) {
            pw.println(studentID + "," + request + ",Pending");
            return true;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error writing to file: " + ex.getMessage());
            return false;
        }
    }

    private void loadTableData() {
        // Clear existing data
        tableModel.setRowCount(0);

        try (BufferedReader br = new BufferedReader(new FileReader("requests.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[0].equals(studentID)) {
                    // Add a new row to the table model
                    tableModel.addRow(new Object[]{parts[1], parts[2]});
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error reading requests file: " + ex.getMessage());
        }
    }


    private boolean deleteRequestFromFile(String requestMessage) {
        List<String> lines = new ArrayList<>();
        File file = new File("requests.txt");
        boolean requestFoundAndDeleted = false;

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                // Keep all lines except the one to be deleted
                if (parts.length >= 2 && parts[0].equals(studentID) && parts[1].equals(requestMessage)) {
                    requestFoundAndDeleted = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error reading file for deletion: " + ex.getMessage());
            return false;
        }

        if (requestFoundAndDeleted) {
            try (PrintWriter pw = new PrintWriter(new FileWriter("requests.txt"))) {
                for (String line : lines) {
                    pw.println(line);
                }
                return true;
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error writing to file during deletion: " + ex.getMessage());
            }
        }
        return false;
    }
}