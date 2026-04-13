import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class PaymentStatusViewer {
    private final String studentID;

    public PaymentStatusViewer(String studentID) {
        this.studentID = studentID;
    }

    public void showPaymentStatus() {
        JFrame frame = new JFrame("Payment Status");
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        JTextArea display = new JTextArea();
        display.setEditable(false);

        try (Scanner sc = new Scanner(new File("receipts.txt"))) {
            while (sc.hasNextLine()) {
                String[] parts = sc.nextLine().split(",");
                if (parts[0].equals(studentID)) {
                    display.setText("Payment: " + parts[1] + "\nDate: " + parts[2] + "\nBalance: " + parts[3]);
                    break;
                }
            }
        } catch (IOException e) {
            display.setText("Error: " + e.getMessage());
        }

        frame.add(new JScrollPane(display));
        frame.setVisible(true);
    }
}