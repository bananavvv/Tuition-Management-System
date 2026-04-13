import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

public class MyFrame extends JFrame implements ActionListener {

    JMenuBar TutorMenu;
    JMenu ClassMenu;
    JMenu StudentList;
    JMenu Profile;
    JMenuItem addClass;
    JMenuItem updateClass;
    JMenuItem deleteClass;
    JMenuItem ViewStudentList;
    JMenuItem ViewProfile;
    JMenuItem UpdateProfile;
    JMenuItem logoutItem; // New JMenuItem for logout
    String username;
    JPanel contentPanel;

    MyFrame(String username) {
        this.username = username;
        this.setTitle("Tuition Management System");
        this.setVisible(true);
        this.setLayout(new BorderLayout());
        this.setSize(800, 600);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TutorMenu = new JMenuBar();

        ClassMenu = new JMenu("CLASS");
        StudentList = new JMenu("STUDENT LIST");
        Profile = new JMenu("PROFILE ");

        StudentList.addActionListener(this);
        Profile.addActionListener(this);

        addClass = new JMenuItem("ADD CLASS");
        updateClass = new JMenuItem("UPDATE CLASS");
        deleteClass = new JMenuItem("DELETE CLASS");
        ViewStudentList = new JMenuItem("View Students in my class");
        ViewProfile = new JMenuItem("VIEW PROFILE");
        UpdateProfile = new JMenuItem("UPDATE PROFILE");
        logoutItem = new JMenuItem("LOGOUT");

        addClass.addActionListener(this);
        updateClass.addActionListener(this);
        deleteClass.addActionListener(this);
        ViewStudentList.addActionListener(this);
        UpdateProfile.addActionListener(this);
        ViewProfile.addActionListener(this);
        logoutItem.addActionListener(this);

        ClassMenu.add(addClass);
        ClassMenu.add(updateClass);
        ClassMenu.add(deleteClass);

        StudentList.add(ViewStudentList);

        Profile.add(ViewProfile);
        Profile.add(UpdateProfile);
        Profile.addSeparator();
        Profile.add(logoutItem);

        this.setJMenuBar(TutorMenu);
        TutorMenu.add(ClassMenu);
        TutorMenu.add(StudentList);
        TutorMenu.add(Profile);

        contentPanel = new JPanel();
        this.add(contentPanel, BorderLayout.CENTER);

        showWelcomePanel();
    }

    public void showWelcomePanel() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome to the Tutor Page! Use the menu on top to navigate through the system", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 20));
        contentPanel.add(welcomeLabel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        if (e.getSource() == addClass) {
            contentPanel.add(new AddClassGUI(this.username, this));
        } else if (e.getSource() == updateClass) {
            contentPanel.add(new UpdateClassGUI(this.username, this));
        } else if (e.getSource() == deleteClass) {
            contentPanel.add(new DeleteClassGUI(this.username, this));
        } else if (e.getSource() == ViewStudentList) {
            contentPanel.add(new ViewStudentGUI(this.username, this));
        } else if (e.getSource() == UpdateProfile) {
            contentPanel.add(new UpdateTutorGUI(this.username, this));
        } else if (e.getSource() == ViewProfile) {
            contentPanel.add(new ViewProfileGUI(this.username, this));
        } else if (e.getSource() == logoutItem) {
            this.dispose(); // Close the current frame
            new systemLogin(); // Open a new login window
        }

        revalidate();
        repaint();
    }
}