import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class receptionistOption_3 extends JPanel implements ActionListener {

    private JButton recordPaymentButton;
    private JButton generateReceiptButton;
    private JButton backButton;
    private JTextArea messageArea;
    private JScrollPane scrollPane;
    private receptionistMenu receptionistMenuFrame; // to enable cardlayout back
    public receptionistOption_3(JFrame frame) {
        this.receptionistMenuFrame = (receptionistMenu) frame;
        recordPaymentButton = new JButton("Record Payment Details");
        generateReceiptButton = new JButton("Generate Receipt for Student");
        backButton = new JButton("Back to Receptionist Menu");
        messageArea = new JTextArea(15, 40);
        messageArea.setEditable(false);
        scrollPane = new JScrollPane(messageArea);

        recordPaymentButton.addActionListener(this);
        generateReceiptButton.addActionListener(this);
        backButton.addActionListener(this);

        setLayout(new FlowLayout());

        add(recordPaymentButton);
        add(generateReceiptButton);
        add(backButton);
        add(scrollPane);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == recordPaymentButton) {
            recordPaymentGUI();
        } else if (e.getSource() == generateReceiptButton) {
            generateReceiptGUI();
        } else if (e.getSource() == backButton) {
            goBackToReceptionistMenu();
        }
    }

    private void recordPaymentGUI() { //method to record payment details (amount and date)
        JFrame recordFrame = new JFrame("Record Payment Details");
        recordFrame.setSize(400, 300);
        recordFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        recordFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        JLabel studentIdLabel = new JLabel("Student ID:"); //input the student ID here
        JTextField studentIdField = new JTextField(20);
        JLabel amountLabel = new JLabel("Amount (Number Only):"); //no characters allowed only decimal and integer
        JTextField amountField = new JTextField(20);
        JLabel dateLabel = new JLabel("Payment Date MM/YYYY:");
        JTextField dateField = new JTextField(20);
        JButton submitButton = new JButton("Submit");

        panel.add(studentIdLabel);
        panel.add(studentIdField);
        panel.add(amountLabel);
        panel.add(amountField);
        panel.add(dateLabel);
        panel.add(dateField);
        panel.add(new JLabel());
        panel.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentID_2 = studentIdField.getText();
                String payment_amount_str = amountField.getText();
                String payment_date = dateField.getText();
                double payment_amount;

                try { //check if the student ID entered exists in the students.txt file
                    boolean studentExists = false;
                    try (BufferedReader brStudent = new BufferedReader(new FileReader("students.txt"))) {
                        String line;
                        while ((line = brStudent.readLine()) != null) {
                            String[] studentData = line.split(",");
                            if (studentData.length > 0 && studentData[0].equals(studentID_2)) { //part 0 cuz student ID is part 0 in the file
                                studentExists = true;
                                break;
                            }
                        }
                    }

                    if (!studentExists) {
                        messageArea.append("Student with ID " + studentID_2 + " not found.\n"); //if doesnt exist print not found
                        recordFrame.dispose();
                        return;
                    }

                    try {
                        payment_amount = Double.parseDouble(payment_amount_str);
                        if (payment_amount <= 0) {
                            messageArea.append("Payment amount must be greater than zero.\n");
                            return;
                        }
                    } catch (NumberFormatException ex) {
                        messageArea.append("Invalid payment amount. Please enter a number.\n"); //only numbers can be entered
                        return;
                    }

                    int enrolledSubjects = 0;
                    try (BufferedReader brEnrollment = new BufferedReader(new FileReader("enrollments.txt"))) {
                        String line;
                        while ((line = brEnrollment.readLine()) != null) {
                            String[] enrollmentData = line.split(",");
                            if (enrollmentData.length > 0 && enrollmentData[0].equals(studentID_2)) {
                                for (int i = 1; i < enrollmentData.length; i++) {
                                    if (enrollmentData[i] != null && !enrollmentData[i].trim().isEmpty()) {
                                        enrolledSubjects++;
                                    }
                                }
                                break;
                            }
                        }
                    } catch (FileNotFoundException ex) {
                        messageArea.append("Enrollment file not found: " + ex.getMessage() + "\n");
                        return;
                    }

                    double expectedPayment = enrolledSubjects * 100.0; //each subject costs rm100
                    double outstandingPayment = expectedPayment - payment_amount;

                    if (outstandingPayment < 0) {
                        outstandingPayment = 0;
                    }

                    try (FileWriter paymentsWriter = new FileWriter("receipts.txt", true)) {
                        //pads the payment entered with "RM"
                        paymentsWriter.write(studentID_2 + ",RM" + payment_amount + "," + payment_date + ",RM" + String.format("%.2f", outstandingPayment) + "\n");
                        messageArea.append("Payment recorded successfully for " + studentID_2 + ".\n");
                    } catch (IOException ex) {
                        messageArea.append("Error writing to receipts file: " + ex.getMessage() + "\n");
                    }
                    recordFrame.dispose();
                } catch (Exception ex) {
                    messageArea.append("An unexpected error occurred: " + ex.getMessage() + "\n");
                }

            }
        });

        recordFrame.add(panel);
        recordFrame.setVisible(true);
    }

    private void generateReceiptGUI() {
        JFrame receiptFrame = new JFrame("Generate Receipt for Student");
        receiptFrame.setSize(600, 400);  // Make the receipt frame larger so itz easier to see
        receiptFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        receiptFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout()); //more fliexible if using border layout

        JLabel studentIdLabel = new JLabel("Student ID:");
        JTextField studentIdField = new JTextField(20);
        JButton submitButton = new JButton("Submit");

        // Create a separate panel for the input elements
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(studentIdLabel);
        inputPanel.add(studentIdField);
        inputPanel.add(submitButton);
        panel.add(inputPanel, BorderLayout.NORTH); // Add the input panel to the top

        JTextArea receiptArea = new JTextArea(15, 40); // Text area to display the receipt
        receiptArea.setEditable(false); // Prevent the user from editing the text

        JScrollPane scrollPaneReceipt = new JScrollPane(receiptArea); // Put the text area in a scrollable pane
        panel.add(scrollPaneReceipt, BorderLayout.CENTER); // Add the scrollable pane to the center

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentID_3 = studentIdField.getText();
                double totalPayment = 0;
                boolean paymentFound = false;
                String studentName = "Unknown";
                receiptArea.setText(""); // Clear the previous receipt

                try (BufferedReader brStudent = new BufferedReader(new FileReader("students.txt"))) {
                    String line;
                    while ((line = brStudent.readLine()) != null) {
                        String[] studentData = line.split(",");
                        if (studentData.length == 7 && studentData[0].equals(studentID_3)) {
                            studentName = studentData[1];
                        }
                    }
                } catch (FileNotFoundException ex) {
                    receiptArea.append("Error finding Student File " + ex.getMessage() + "\n");
                    return;
                } catch (IOException exception) {
                    receiptArea.append("Error reading Student File " + exception.getMessage() + "\n");
                }

                try (BufferedReader brPayment = new BufferedReader(new FileReader("receipts.txt"))) {
                    String line1;
                    receiptArea.append("\n--- Payment History for Student ID: " + studentID_3 + " ---\n");

                    while ((line1 = brPayment.readLine()) != null) {
                        String[] paymentData = line1.split(",");
                        if (paymentData.length == 4 && paymentData[0].equals(studentID_3)) {
                            paymentFound = true;
                            String paymentAmountStr = paymentData[1].substring(2);
                            double paymentAmount = Double.parseDouble(paymentAmountStr);
                            String paymentDate = paymentData[2];
                            String outstandingAmount = paymentData[3];
                            receiptArea.append(String.format("%-20s: %s\n", "Student Name", studentName));
                            receiptArea.append(String.format("%-20s: %s\n", "Amount", paymentData[1]));
                            receiptArea.append(String.format("%-20s: %s\n", "Date", paymentDate));
                            receiptArea.append(String.format("%-20s: %s\n", "Outstanding", outstandingAmount));
                            totalPayment += paymentAmount;
                        }
                    }

                    if (!paymentFound) {
                        receiptArea.append("No payment records found for Student ID: " + studentID_3 + "\n");
                    } else {
                        receiptArea.append("-------------------------------------------------------\n");
                        receiptArea.append("Total Payment Amount: RM" + String.format("%.2f", totalPayment) + "\n");
                    }
                } catch (FileNotFoundException ex) {
                    receiptArea.append("Payment file not found: " + ex.getMessage() + "\n");
                } catch (IOException ex) {
                    receiptArea.append("Error reading payment file: " + ex.getMessage() + "\n");
                } catch (NumberFormatException ex) {
                    receiptArea.append("Error parsing payment amount.  Check data in receipts.txt\n");
                }
            }
        });

        receiptFrame.add(panel);
        receiptFrame.setVisible(true);
    }

    private void goBackToReceptionistMenu() { //back button to receptionist menu
        CardLayout cardLayout = (CardLayout) receptionistMenuFrame.getContentPane().getLayout();
        cardLayout.show(receptionistMenuFrame.getContentPane(), "receptionistMenu");
    }

    public static receptionistOption_3 createInstance(JFrame frame) {
        return new receptionistOption_3(frame);
    }
}