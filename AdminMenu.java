import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// IOException is no longer needed here
// import java.io.IOException;

public class AdminMenu extends JFrame implements ActionListener {

    public AdminMenu() {
        setTitle("Admin Menu");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Create components
        JButton manageTutorsButton = new JButton("Manage Tutors");
        JButton assignTutorButton = new JButton("Assign Tutor");
        JButton manageReceptionistsButton = new JButton("Manage Receptionists");
        JButton viewMonthlyIncomeButton = new JButton("View Monthly Income Report");
        JButton updateAdminProfileButton = new JButton("Update Admin Profile");
        JButton logoutButton = new JButton("Logout");
        JButton exitButton = new JButton("Exit");

        // Add action listeners
        manageTutorsButton.addActionListener(this);
        assignTutorButton.addActionListener(this);
        manageReceptionistsButton.addActionListener(this);
        viewMonthlyIncomeButton.addActionListener(this);
        updateAdminProfileButton.addActionListener(this);
        logoutButton.addActionListener(this);
        exitButton.addActionListener(this);

        // Set action commands to distinguish buttons
        manageTutorsButton.setActionCommand("manageTutors");
        assignTutorButton.setActionCommand("assignTutor");
        manageReceptionistsButton.setActionCommand("manageReceptionists");
        viewMonthlyIncomeButton.setActionCommand("viewMonthlyIncome");
        updateAdminProfileButton.setActionCommand("updateAdminProfile");
        logoutButton.setActionCommand("logout");
        exitButton.setActionCommand("exit");

        // Create layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 1, 10, 10)); // 7 rows, 1 column, with gaps
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        panel.add(manageTutorsButton);
        panel.add(assignTutorButton);
        panel.add(manageReceptionistsButton);
        panel.add(viewMonthlyIncomeButton);
        panel.add(updateAdminProfileButton);
        panel.add(logoutButton);
        panel.add(exitButton);

        // Add panel to the frame
        add(panel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        // The try-catch block for IOException is removed as it's not needed here.
        // Individual windows handle their own exceptions.
        switch (command) {
            case "manageTutors":
                // Simply create a new instance of the window.
                new AdminOption1();
                break;
            case "assignTutor":
                // The constructor shows the window.
                new AdminOption2();
                break;
            case "manageReceptionists":
                // The constructor shows the window.
                new AdminOption3();
                break;
            case "viewMonthlyIncome":
                // The constructor shows the window.
                new AdminOption4();
                break;
            case "updateAdminProfile":
                // Call the static method designed for this purpose.
                AdminOption5.promptForPasswordAndShowGUI();
                break;
            case "logout":
                this.dispose(); // Close the admin menu
                // Assuming a systemLogin class exists to open the login window
                new systemLogin();
                break;
            case "exit":
                System.exit(0);
                break;
        }
    }

    public static void main(String[] args) {
        // This makes the GUI runnable on its own.
        SwingUtilities.invokeLater(AdminMenu::new);
    }
}