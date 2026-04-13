import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class AdminOption3 extends JFrame implements ActionListener {

    private final JButton registerButton;
    private final JButton deleteButton;
    private final JButton viewButton;
    private final JButton exitButton;

    public AdminOption3() {
        setTitle("---Manage Receptionists---");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
        setLocationRelativeTo(null); // Center the window

        // Create visual components
        JLabel titleLabel = new JLabel("Select an option to manage receptionists:", SwingConstants.CENTER);
        registerButton = new JButton("Register Receptionist");
        deleteButton = new JButton("Delete Receptionist");
        viewButton = new JButton("View All Receptionists");
        exitButton = new JButton("Exit");

        // Add action listeners
        registerButton.addActionListener(this);
        deleteButton.addActionListener(this);
        viewButton.addActionListener(this);
        exitButton.addActionListener(this);

        // Create a panel for layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add components to the panel
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
        // The try-catch block is retained for any unexpected errors during GUI launching.
        try {
            if (e.getSource() == registerButton) {
                // Call the static method to show the registration GUI.
                RegisterReceptionist.showRegistrationGUI();

            } else if (e.getSource() == deleteButton) {
                // Call the static method to show the deletion GUI.
                DeleteReceptionist.showDeleteGUI();

            } else if (e.getSource() == viewButton) {
                // Call the static method to show the view GUI.
                ViewAllReceptionists.showViewGUI();

            } else if (e.getSource() == exitButton) {
                // dispose() closes only this specific "Manage Receptionists" window.
                this.dispose();
            }
        } catch (Exception ex) {
            // Catch any unexpected errors during the launch of the windows.
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) {
        // Run the GUI on the Event Dispatch Thread for thread safety.
        SwingUtilities.invokeLater(AdminOption3::new);
    }
}