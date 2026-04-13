import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminOption1 extends JFrame implements ActionListener {

    // GUI components are declared as final because they are initialized once in the constructor.
    private final JButton registerButton;
    private final JButton deleteButton;
    private final JButton viewButton;
    private final JButton exitButton;

    public AdminOption1() {
        setTitle("---Manage Tutors---");
        setSize(400, 300); // Increased height slightly for better spacing
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
        setLocationRelativeTo(null); // Center the window

        // Create components
        JLabel titleLabel = new JLabel("Select an option to manage tutors:", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton = new JButton("Register New Tutor");
        deleteButton = new JButton("Delete Existing Tutor");
        viewButton = new JButton("View All Tutors");
        exitButton = new JButton("Back to Main Menu");

        // Add action listeners to the buttons
        registerButton.addActionListener(this);
        deleteButton.addActionListener(this);
        viewButton.addActionListener(this);
        exitButton.addActionListener(this);

        // Create layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10)); // 5 rows, 1 column, with gaps
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        panel.add(titleLabel);
        panel.add(registerButton);
        panel.add(deleteButton);
        panel.add(viewButton);
        panel.add(exitButton);

        // Add panel to the frame
        add(panel);

        // Make the window visible
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // The try-catch block is removed because this menu only opens other windows.
        // Each new window is now responsible for handling its own exceptions.

        Object source = e.getSource();

        if (source == registerButton) {
            // Create a new instance of the RegisterTutor GUI window.
            new RegisterTutor();

        } else if (source == deleteButton) {
            // Create a new instance of the DeleteTutor GUI window.
            new DeleteTutor();

        } else if (source == viewButton) {
            // Create a new instance of the ViewAllTutors GUI window.
            new ViewAllTutors();

        } else if (source == exitButton) {
            // Closes the "Manage Tutors" window.
            this.dispose();
        }
    }

    public static void main(String[] args) {
        // This ensures the GUI is created safely on the Event Dispatch Thread.
        SwingUtilities.invokeLater(AdminOption1::new);
    }
}