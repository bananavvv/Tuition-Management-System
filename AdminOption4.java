import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class AdminOption4 extends JFrame implements ActionListener {

    private final JComboBox<String> monthComboBox;
    private final JButton generateReportButton;
    private final JTextArea reportArea;
    private final JLabel totalIncomeLabel;

    public AdminOption4() {
        setTitle("--- Monthly Income Report ---");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- Top Panel for Input ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        topPanel.add(new JLabel("Select Month:"));

        // This JComboBox replaces the need for Scanner and manual input validation.
        String[] months = {"January (1)", "February (2)", "March (3)", "April (4)", "May (5)", "June (6)",
                "July (7)", "August (8)", "September (9)", "October (10)", "November (11)", "December (12)"};
        monthComboBox = new JComboBox<>(months);
        topPanel.add(monthComboBox);

        generateReportButton = new JButton("Generate Report");
        generateReportButton.addActionListener(this);
        topPanel.add(generateReportButton);

        // --- Center Panel for Report Details ---
        // This JTextArea is the GUI equivalent of printing each receipt to the console.
        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Report Details"));

        // --- Bottom Panel for Total Income ---
        // This JLabel is the GUI equivalent of the final System.out.printf for the total.
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        totalIncomeLabel = new JLabel("Total Income for Selected Month: RM 0.00");
        totalIncomeLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        bottomPanel.add(totalIncomeLabel);

        // Add panels to the frame
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == generateReportButton) {
            generateReport();
        }
    }


    // Generates a report based on the selected month.
    // It reads from "receipts.txt" with the format:
    // studentid,amount needed to be paid,date(month-year),outstanding balance

    private void generateReport() {
        // Step 1: Get the target month from the UI.
        int targetMonth = monthComboBox.getSelectedIndex() + 1;

        // Clear previous results.
        reportArea.setText("");
        totalIncomeLabel.setText("Total Income for Selected Month: RM 0.00");

        // Step 2: Check if the 'receipts.txt' file exists.
        File receiptsFile = new File("receipts.txt");
        if (!receiptsFile.exists()) {
            JOptionPane.showMessageDialog(this, "No receipts found. The 'receipts.txt' file does not exist.", "File Not Found", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Step 3: Initialize variables.
        double totalIncome = 0.0;
        boolean foundReceiptsForMonth = false;
        StringBuilder reportContent = new StringBuilder();
        reportContent.append(String.format("--- Diagnostic Report for Month %d ---\n\n", targetMonth));

        // Step 4: Read the file and process each line.
        try (BufferedReader br = new BufferedReader(new FileReader(receiptsFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length != 4) {
                    reportContent.append("Skipping line: Expected 4 columns, but found ")
                            .append(parts.length).append(". -> [").append(line).append("]\n");
                    continue;
                }

                try {
                    // MODIFIED: Date is the 3rd element (index 2), split by "/"
                    String dateString = parts[2].trim();
                    String[] dateParts = dateString.split("/"); // Use "/" as the separator

                    if (dateParts.length != 2) {
                        reportContent.append("Skipping line: Incorrect date format. Expected 'month/year'. -> [")
                                .append(line).append("]\n");
                        continue;
                    }

                    int receiptMonth = Integer.parseInt(dateParts[0]);

                    // MODIFIED: Amount is the 2nd element (index 1).
                    // Must remove "RM" before parsing.
                    String amountString = parts[1].trim().toUpperCase();
                    if (!amountString.startsWith("RM")) {
                        reportContent.append("Skipping line: Amount does not start with 'RM'. -> [")
                                .append(line).append("]\n");
                        continue;
                    }
                    // Remove "RM" prefix and then parse
                    double amount = Double.parseDouble(amountString.substring(2));


                    if (receiptMonth == targetMonth) {
                        foundReceiptsForMonth = true;
                        totalIncome += amount;
                        // Append details for successfully processed receipts.
                        reportContent.append("Processed Receipt: Student ID ")
                                .append(parts[0].trim())
                                .append(", Amount: ")
                                .append(String.format("RM%.2f", amount)) // Display with RM
                                .append("\n");
                    }
                } catch (NumberFormatException ex) {
                    // This will catch errors from parsing the month or the cleaned amount string.
                    reportContent.append("Skipping line: A number was badly formatted (check date or amount). -> [")
                            .append(line).append("]\n");
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "An error occurred while reading the receipts file: " + ex.getMessage(), "IO Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Step 5: Display the final results.
        reportContent.append("\n-----------------------------\n");
        if (foundReceiptsForMonth) {
            reportContent.append("Report generation complete.\n");
            reportArea.setText(reportContent.toString());
            totalIncomeLabel.setText(String.format("Total income for month %d is: RM %.2f", targetMonth, totalIncome));
        } else {
            reportContent.append("No valid income was recorded for month ").append(targetMonth).append(".\n");
            reportContent.append("Please check the diagnostic messages above to fix your 'receipts.txt' file.\n");
            reportArea.setText(reportContent.toString());
            totalIncomeLabel.setText("Total Income for Selected Month: RM 0.00");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminOption4::new);
    }
}