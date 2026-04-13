import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class receptionistMenu extends JFrame implements ActionListener {

    private CardLayout cardLayout = new CardLayout();
    private JPanel contentPane = new JPanel(cardLayout);
    private JPanel receptionistMenuPanel;

    private JButton registerStudentButton;
    private JButton updateEnrollmentButton;
    private JButton acceptPaymentButton;
    private JButton deleteAccountButton;
    private JButton updateProfileButton;
    private JButton logoutButton;
    private JButton exitButton;
    private JLabel messageLabel;
    private systemLogin loginWindow; // Reference to the login window to logout


    public receptionistMenu(systemLogin loginWindow) {
        this.loginWindow = loginWindow;
        setTitle("Receptionist Menu");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the Receptionist Menu Panel
        receptionistMenuPanel = new JPanel();
        receptionistMenuPanel.setLayout(new GridLayout(8, 1));

        // components / buttons for the menu
        registerStudentButton = new JButton("Register a Student and Enroll");
        updateEnrollmentButton = new JButton("Update Subject Enrollment");
        acceptPaymentButton = new JButton("Accept Payment and Generate Receipt");
        deleteAccountButton = new JButton("Delete Student Account");
        updateProfileButton = new JButton("Update Own Profile and View Requests");
        logoutButton = new JButton("Logout");
        exitButton = new JButton("Exit");
        messageLabel = new JLabel(""); //start the message label

        // Set action listeners
        registerStudentButton.addActionListener(this);
        updateEnrollmentButton.addActionListener(this);
        acceptPaymentButton.addActionListener(this);
        deleteAccountButton.addActionListener(this);
        updateProfileButton.addActionListener(this);
        logoutButton.addActionListener(this);
        exitButton.addActionListener(this);

        receptionistMenuPanel.add(registerStudentButton);
        receptionistMenuPanel.add(updateEnrollmentButton);
        receptionistMenuPanel.add(acceptPaymentButton);
        receptionistMenuPanel.add(deleteAccountButton);
        receptionistMenuPanel.add(updateProfileButton);
        receptionistMenuPanel.add(logoutButton);
        receptionistMenuPanel.add(exitButton);
        receptionistMenuPanel.add(messageLabel);

        // 2. Add the receptionistMenuPanel to the CardLayout
        contentPane.add(receptionistMenuPanel, "receptionistMenu");

        // Set the content pane of the JFrame to the CardLayout panel
        setContentPane(contentPane);
        contentPane.setLayout(cardLayout); //Ensure to layout cardlayout

        // Show the receptionist menu panel initially
        cardLayout.show(contentPane, "receptionistMenu");

        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerStudentButton) {
            showOption1();
        } else if (e.getSource() == updateEnrollmentButton) {
            showOption2(); // NOW USING showOption2
        } else if (e.getSource() == acceptPaymentButton) {
            showOption3();
        } else if (e.getSource() == deleteAccountButton) {
            showOption4();
        } else if (e.getSource() == updateProfileButton) {
            showOption5();
        } else if (e.getSource() == logoutButton) {
            // Log out close the receptionist menu
            logout();
        } else if (e.getSource() == exitButton) {
            exitProgram(); //exit the whole program
        }
    }

    private void showOption1() { //so it doesnt make a new pane overlay
        receptionistOption_1 option1Panel = receptionistOption_1.createInstance(this);
        contentPane.add(option1Panel, "option1");
        cardLayout.show(contentPane, "option1");
    }

    private void showOption2() { //so it doesnt make a new pane overlay
        receptionistOption_2 option2Panel = receptionistOption_2.createInstance(this);
        contentPane.add(option2Panel, "option2");
        cardLayout.show(contentPane, "option2");
    }

    private void showOption3() { //so it doesnt make a new pane overlay
        receptionistOption_3 option3Panel = receptionistOption_3.createInstance(this);
        contentPane.add(option3Panel, "option3");
        cardLayout.show(contentPane, "option3");
    }

    private void showOption4() { //so it doesnt make a new pane overlay
        receptionistOption_4 option4Panel = receptionistOption_4.createInstance(this);
        contentPane.add(option4Panel, "option4");
        cardLayout.show(contentPane, "option4");
    }

    private void showOption5() {
        receptionistOption_5 option5Panel = receptionistOption_5.createInstance(this);
        contentPane.add(option5Panel, "option5");
        cardLayout.show(contentPane, "option5");
    }

    private void logout() {
        // Close the receptionist menu
        this.dispose();
        // Show the login window
        loginWindow.setVisible(true); // Make the login window visible again
    }

    private void exitProgram() {
        System.exit(0);  // exits the entire application properly
    }
}