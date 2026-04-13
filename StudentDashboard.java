import javax.swing.*;
import java.awt.GridLayout;


public class StudentDashboard {
    private final String studentID;
    private final String username;

    public StudentDashboard(String studentID, String username) {
        this.studentID = studentID;
        this.username = username;
    }

    public void showDashboard() {
        JFrame frame = new JFrame("Student Dashboard - " + username);
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(6, 1));
        frame.setLocationRelativeTo(null);

        JButton profileBtn = new JButton("Profile");
        JButton requestBtn = new JButton("Requests");
        JButton scheduleBtn = new JButton("Schedule");
        JButton paymentBtn = new JButton("Payment Status");
        JButton logoutBtn = new JButton("Logout");

        profileBtn.addActionListener(e -> new ProfileManager(studentID, username).showProfile());
        requestBtn.addActionListener(e -> new RequestManager(studentID).requestMenu());
        scheduleBtn.addActionListener(e -> new ScheduleViewer(studentID).showSchedule());
        paymentBtn.addActionListener(e -> new PaymentStatusViewer(studentID).showPaymentStatus());
        logoutBtn.addActionListener(e -> {
            frame.dispose();
            new systemLogin();
        });

        frame.add(profileBtn);
        frame.add(requestBtn);
        frame.add(scheduleBtn);
        frame.add(paymentBtn);
        frame.add(logoutBtn);
        frame.setVisible(true);
    }
}