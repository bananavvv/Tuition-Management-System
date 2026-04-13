import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class systemLogin extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;

    private static final Scanner scanner = new Scanner(System.in); //Keep the scanner in the gui
    private int loginAttempts = 0;  // Track login attempts


    public systemLogin() {
        setTitle("Tuition Centre Management System");
        setSize(550, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Create components for user and password
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        messageLabel = new JLabel("");

        // Set action listener for the login button
        loginButton.addActionListener(this);

        // Create layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2)); // 4 rows, 2 columns

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(loginButton);
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(messageLabel);

        //panel to the frame
        add(panel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            try {
                if (authenticateRecept(username, password)) {
                    messageLabel.setText("Login successful (Receptionist)!");
                    openReceptionistMenu(); // Open the receptionist menu
                    loginAttempts = 0; // Reset attempts on successful login.

                } else if (authenticateAdmin(username, password)) {
                    messageLabel.setText("Login successful (Admin)!");
                    openAdminMenu(); // Open the admin menu
                    loginAttempts = 0; // Reset attempts

                } else if (authenticateTutor(username, password)) {
                    messageLabel.setText("Login successful (Tutor)!");
                    openTutorMenu(username); // Open the tutor menu
                    loginAttempts = 0; // Reset attempts

                } else if (authenticateStudent(username, password)) {
                    messageLabel.setText("Login successful (Student)!");
                    String studentID = getStudentID(username);

                    if (studentID != null) {
                        openStudentMenu(studentID, username); // Open the student menu
                        loginAttempts = 0; // Reset attempts
                    } else {
                        messageLabel.setText("Error: Could not retrieve student ID.");
                    }

                } else {
                    loginAttempts++;
                    messageLabel.setText("Invalid username or password. Attempts: " + loginAttempts);

                    if (loginAttempts >= 3) {
                        messageLabel.setText("Too many incorrect login attempts. Exiting.");
                        JOptionPane.showMessageDialog(this, "Too many incorrect login attempts. Exiting."); //Optional Dialog Box

                        System.exit(0); // close

                    }
                }
            } catch (IOException ex) {
                messageLabel.setText("Error reading user file.");
                ex.printStackTrace();
            }
        }
    }

    // New method to fetch the student ID from the students.txt file.
    private String getStudentID(String username) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("students.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userData = line.split(",");
                // Assuming the format is: studentID,username,password
                if (userData.length >= 3 && userData[1].equals(username)) {
                    return userData[0];
                }
            }
        }
        return null; // Return null if student ID is not found for the given username
    }

    private boolean authenticateRecept(String username, String password) throws IOException {
        return authenticateUser(username, password, "receptionist");
    }

    private boolean authenticateAdmin(String username, String password) throws IOException {
        return authenticateUser(username, password, "admin");
    }

    private boolean authenticateTutor(String username, String password) throws IOException {
        return authenticateUser(username, password, "tutor");
    }

    private boolean authenticateStudent(String username, String password) throws IOException {
        return authenticateUser(username, password, "student");
    }

    private boolean authenticateUser(String username, String password, String role) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length == 3 && userData[0].equals(username) && userData[1].equals(password) && userData[2].equals(role)) {
                    return true;
                }
            }
        }
        return false;
    }


    private void openReceptionistMenu() {
        receptionistMenu receptionistMenu = new receptionistMenu(this);
        //Hide the login window
        this.setVisible(false);
    }

    private void openAdminMenu() {
        AdminMenu AdminMenu = new AdminMenu();
        this.setVisible(false);
    }

    private void openTutorMenu(String username) {
        this.dispose();
        MyFrame tutorFrame = new MyFrame(username);
    }

    private void openStudentMenu(String studentID, String username) {
        this.setVisible(false); // Hide the login window.
        StudentDashboard dashboard = new StudentDashboard(studentID, username);
        dashboard.showDashboard();
    }

    public static void MainLogin() throws IOException { //Kept the function and only call it
        int loginAttempts = 0;
        while (loginAttempts < 3) {
            System.out.println("System Login");
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            if (authenticateReceptStatic(username, password)) {
                System.out.println("Login successful!");
                return; // Exit the login loop after successful login

            } else if (authenticateAdminStatic(username, password)) {
                System.out.println("Login successful!");
                return;
            } else if (authenticateTutorStatic(username, password)) {
                System.out.println("Login successful!");
                new MyFrame(username);
                return;
            } else if (authenticateStudentStatic(username, password)) {
                System.out.println("Login successful!");
            }
            else{
                System.out.println("Incorrect username or password. Please try again.");
                loginAttempts = loginAttempts + 1;
            }
        }
        System.out.println("Too many incorrect login attempts. Exiting.");
    }

    private static boolean authenticateReceptStatic(String username, String password) throws IOException {
        return authenticateUserStatic(username, password, "receptionist");
    }

    private static boolean authenticateAdminStatic(String username, String password) throws IOException {
        return authenticateUserStatic(username, password, "admin");
    }

    private static boolean authenticateTutorStatic(String username, String password) throws IOException {
        return authenticateUserStatic(username, password, "tutor");
    }

    private static boolean authenticateStudentStatic(String username, String password) throws IOException {
        return authenticateUserStatic(username, password, "student");
    }
    private static boolean authenticateUserStatic(String username, String password, String role) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length == 3 && userData[0].equals(username) && userData[1].equals(password) && userData[2].equals(role)) {
                    return true;
                }
            }
        }
        return false;
    }


    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(() -> new systemLogin());
    }
}