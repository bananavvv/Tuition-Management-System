import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.io.FileWriter;

public class receptionistOption_5 extends JPanel implements ActionListener {

    private JTextField receptionistIdField;
    private JButton updateInfoButton;
    private JButton viewRequestsButton;
    private JButton viewMyInfoButton;
    private JTextArea messageArea;
    private JScrollPane scrollPane;
    private JButton backButton;
    private receptionistMenu receptionistMenuFrame;

    public receptionistOption_5(JFrame frame) {
        this.receptionistMenuFrame = (receptionistMenu) frame;
        // Create components and buttons
        JLabel receptionistIdLabel = new JLabel("Receptionist ID:");
        receptionistIdField = new JTextField(10);
        updateInfoButton = new JButton("Update Information");
        viewRequestsButton = new JButton("View Student Requests");
        viewMyInfoButton = new JButton("View My Information");
        backButton = new JButton("Back to Receptionist Menu");
        messageArea = new JTextArea(20, 60);
        messageArea.setEditable(false);
        scrollPane = new JScrollPane(messageArea);

        // Set action listeners
        updateInfoButton.addActionListener(this);
        viewRequestsButton.addActionListener(this);
        viewMyInfoButton.addActionListener(this);
        backButton.addActionListener(this);

        // Create layout
        setLayout(new FlowLayout());

        add(receptionistIdLabel);
        add(receptionistIdField);
        add(updateInfoButton);
        add(viewRequestsButton);
        add(viewMyInfoButton);
        add(backButton);
        add(scrollPane);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == updateInfoButton) {
            updateReceptionistInfoGUI();
        } else if (e.getSource() == viewRequestsButton) {
            viewStudentRequestsGUI();
        } else if (e.getSource() == viewMyInfoButton) { // action for the new button
            viewMyInformationGUI(); // Call the function
        } else if (e.getSource() == backButton) {
            goBackToReceptionistMenu();
        }
    }

    private void updateReceptionistInfoGUI() { //to change password,username,email,phone num
        String receptionistID = receptionistIdField.getText();
        messageArea.setText(""); // Clear previous messages

        // Check if receptionist ID exists
        boolean receptionistExists = false;
        String matchedReceptionistLine = null;
        try (BufferedReader br = new BufferedReader(new FileReader("receptionists.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] receptionistData = line.split(",");
                if (receptionistData.length > 0 && receptionistData[0].equals(receptionistID)) { //ID is in part 0
                    receptionistExists = true;
                    matchedReceptionistLine = line;
                    break;
                }
            }
        } catch (IOException e) {
            messageArea.append("Error reading receptionists.txt: " + e.getMessage() + "\n");
            return;
        }

        if (!receptionistExists) {
            messageArea.append("Receptionist with ID " + receptionistID + " not found.\n");
            return;
        }

        // Create a new JDialog for updating information
        JDialog updateDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Update Receptionist Information", true);
        updateDialog.setSize(400, 300);
        updateDialog.setLocationRelativeTo(this);
        updateDialog.setLayout(new GridLayout(0, 1)); // Use GridLayout for this example

        JTextField newUsernameField = new JTextField(20);
        JTextField newPasswordField = new JTextField(20);
        JTextField newNameField = new JTextField(20);
        JTextField newEmailField = new JTextField(20);
        JTextField newContactField = new JTextField(20);

        updateDialog.add(new JLabel("New Username (leave blank to keep current):"));
        updateDialog.add(newUsernameField);
        updateDialog.add(new JLabel("New Password (leave blank to keep current):"));
        updateDialog.add(newPasswordField);
        updateDialog.add(new JLabel("New Name (leave blank to keep current):"));
        updateDialog.add(newNameField);
        updateDialog.add(new JLabel("New Email (leave blank to keep current):"));
        updateDialog.add(newEmailField);
        updateDialog.add(new JLabel("New Contact (leave blank to keep current):"));
        updateDialog.add(newContactField);

        JButton submitButton = new JButton("Submit Updates");
        updateDialog.add(submitButton);


        final String finalMatchedReceptionistLine = matchedReceptionistLine;

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newUsername = newUsernameField.getText();
                String newPassword = newPasswordField.getText();

                try {
                    // Update User information
                    String currentUsername = "";
                    String[] receptionistDataForUsername = finalMatchedReceptionistLine.split(",");
                    if (receptionistDataForUsername.length >= 2) {
                        currentUsername = receptionistDataForUsername[1];
                    }

                    if (!currentUsername.isEmpty()) {
                        if (!newUsername.isEmpty() || !newPassword.isEmpty()) {
                            // Read all users
                            List<String> users = new ArrayList<>();
                            try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
                                String line;
                                while ((line = br.readLine()) != null) {
                                    users.add(line);
                                }
                            }

                            // Find the receptionist user and update if necessary
                            boolean userFound = false;
                            for (int i = 0; i < users.size(); i++) {
                                String[] userData = users.get(i).split(",");
                                if (userData.length == 3 && userData[0].equals(currentUsername) && userData[2].equals("receptionist")) {
                                    userFound = true;
                                    if (!newUsername.isEmpty()) {
                                        userData[0] = newUsername;
                                    }
                                    if (!newPassword.isEmpty()) {
                                        userData[1] = newPassword;
                                    }
                                    messageArea.append("Updated username/password\n");
                                    // Write the updated list back to the file
                                    break;
                                }
                            }

                            if (!userFound) {
                                messageArea.append("Receptionist user not found in user file.\n");
                            }
                        }
                    }

                } catch (IOException ex) {
                    messageArea.append("An error occurred: " + ex.getMessage() + "\n");
                } finally {
                    updateDialog.dispose();
                }
                updateDialog.dispose();
            }
        });

        updateDialog.setVisible(true);
    }

    private void viewStudentRequestsGUI() { //displays the student request in the request file and can change the status
        messageArea.setText(""); // Clear the message area
        JFrame requestFrame = new JFrame("Student Requests");
        requestFrame.setSize(600, 400);
        requestFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        requestFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(); //new panel for the request screen
        panel.setLayout(new BorderLayout());

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> requestList = new JList<>(listModel);
        JScrollPane listScrollPane = new JScrollPane(requestList);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"pending", "complete", "rejected"});
        JButton updateStatusButton = new JButton("Update Status");

        buttonPanel.add(new JLabel("Status:"));
        buttonPanel.add(statusComboBox);
        buttonPanel.add(updateStatusButton);

        panel.add(listScrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Load requests from file
        try (BufferedReader br = new BufferedReader(new FileReader("requests.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                listModel.addElement(line); // Add each request to the list
            }
        } catch (IOException e) {
            messageArea.append("Error reading requests.txt: " + e.getMessage() + "\n");
        }

        // Update Status Button Action
        updateStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = requestList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String selectedRequest = listModel.getElementAt(selectedIndex);
                    String newStatus = (String) statusComboBox.getSelectedItem();

                    // Update the request in the list model
                    String[] requestData = selectedRequest.split(",");
                    if (requestData.length == 3) {
                        requestData[2] = newStatus;
                        String updatedRequest = String.join(",", requestData);
                        listModel.setElementAt(updatedRequest, selectedIndex);

                        // Update the request in the file
                        try {
                            List<String> requests = new ArrayList<>();
                            try (BufferedReader br = new BufferedReader(new FileReader("requests.txt"))) {
                                String line;
                                while ((line = br.readLine()) != null) {
                                    requests.add(line);
                                }
                            }

                            requests.set(selectedIndex, updatedRequest);

                            try (FileWriter fw = new FileWriter("requests.txt")) {
                                for (String request : requests) {
                                    fw.write(request + "\n");
                                }
                            }

                            messageArea.append("Request status updated successfully.\n");
                        } catch (IOException ex) {
                            messageArea.append("Error updating requests.txt: " + ex.getMessage() + "\n");
                        }
                    } else {
                        messageArea.append("Invalid request data format.\n");
                    }
                } else {
                    messageArea.append("Please select a request to update.\n");
                }
            }
        });

        requestFrame.add(panel);
        requestFrame.setVisible(true);
    }

    private void viewMyInformationGUI() {
        // Create the popup window
        JDialog popup = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Receptionist Information", true);
        popup.setSize(400, 400);
        popup.setLayout(new BorderLayout());
        popup.setLocationRelativeTo(this); // Center relative to the panel

        JPanel inputPanel = new JPanel(new FlowLayout());
        JLabel usernameLabel = new JLabel("Enter Receptionist Username:");
        JTextField usernameField = new JTextField(20);
        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);

        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        JScrollPane scrollPaneInfo = new JScrollPane(infoArea);
        popup.add(inputPanel, BorderLayout.NORTH);
        popup.add(scrollPaneInfo, BorderLayout.CENTER);

        JButton submitButton = new JButton("Submit");
        inputPanel.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                infoArea.setText(""); // Clear previous information
                String receptionistID = null; // Variable to store Receptionist ID

                // First, get the ID from the user file
                try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] userData = line.split(",");
                        if (userData.length == 3 && userData[0].equals(username) && userData[2].equals("receptionist")) {
                            // Username found and role is receptionist
                            // Now retrieve receptionist ID from the receptionists file
                            try (BufferedReader brRec = new BufferedReader(new FileReader("receptionists.txt"))) {
                                String lineRec;
                                while ((lineRec = brRec.readLine()) != null) {
                                    String[] receptionistData = lineRec.split(",");
                                    if(receptionistData.length == 4 && receptionistData[1].equals(username)){
                                        receptionistID = receptionistData[0]; //save the receptionist ID.
                                        break;
                                    }
                                }

                            } catch (IOException ioException) {
                                infoArea.append("Error reading receptionists.txt " + ioException.getMessage());
                                return; //stop the process
                            }
                            if(receptionistID == null) {
                                infoArea.append("Receptionist user not found in receptionist.txt. Check the usernames.\n");
                                return;
                            }
                            break;
                        }
                    }
                } catch (IOException ex) {
                    infoArea.append("Error reading users.txt: " + ex.getMessage() + "\n");
                    return;
                }
                if (receptionistID == null) {
                    infoArea.append("Receptionist with username " + username + " not found.\n");
                    return; // Stop if receptionist not found
                }

                try (BufferedReader br = new BufferedReader(new FileReader("receptionists.txt"))) {
                    String line;
                    boolean found = false;

                    while ((line = br.readLine()) != null) {
                        String[] receptionistData = line.split(",");
                        if (receptionistData.length == 4 && receptionistData[0].equals(receptionistID)) {
                            infoArea.append("\nReceptionist Information:\n");
                            infoArea.append("----------------------\n");
                            infoArea.append("ID: " + receptionistData[0] + "\n");
                            infoArea.append("Name: " + receptionistData[1] + "\n");
                            infoArea.append("Email: " + receptionistData[2] + "\n");
                            infoArea.append("Contact: " + receptionistData[3] + "\n");
                            infoArea.append("----------------------\n");
                            found = true;
                            break; // Exit the loop since we found the information
                        }
                    }
                    if (!found) {
                        infoArea.append("Receptionist information not found.\n");
                    }
                } catch (IOException e2) {
                    infoArea.append("Error reading receptionists.txt: " + e2.getMessage() + "\n");
                }
            }
        });

        popup.setVisible(true);
    }

    private void goBackToReceptionistMenu() {
        CardLayout cardLayout = (CardLayout) receptionistMenuFrame.getContentPane().getLayout();
        cardLayout.show(receptionistMenuFrame.getContentPane(), "receptionistMenu");
    }

    public static receptionistOption_5 createInstance(JFrame frame) {
        return new receptionistOption_5(frame);
    }
}