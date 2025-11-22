package GUI;

import UserManagement.User;
import UserManagement.Student;
import UserManagement.Instructor;
import UserManagement.Admin;
import Database.UserService;
import Utilities.*;

import javax.swing.*;
import java.awt.*;

public class login extends JFrame {
    private JPanel mainPanel;
    private JPanel loginCard;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JLabel usernameLabel;
    private JTextField usernameField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JLabel roleLabel;
    private JComboBox<String> roleBox;
    private JButton loginButton;
    private JLabel signupLabel;
    private JButton signupButton;

    private UserService userService = new UserService();

    public login() {
        setTitle("SkillForge - Login");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(mainPanel);

        loginCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(40, 60, 40, 60)
        ));

        roleBox.addItem("Student");
        roleBox.addItem("Instructor");
        roleBox.addItem("Admin");

        loginButton.addActionListener(e -> processLogin());
        signupButton.addActionListener(e -> {
            new signUp();
            dispose();
        });

        usernameField.addActionListener(e -> passwordField.requestFocus());
        passwordField.addActionListener(e -> processLogin());

        setVisible(true);
    }

    private void processLogin() {
        String input = usernameField.getText().trim();
        if (!Validation.isValidString(input)) {
            showError("Email or ID cannot be empty.");
            return;
        }

        String password = new String(passwordField.getPassword());
        if (!Validation.isValidString(password)) {
            showError("Password cannot be empty.");
            passwordField.setText("");
            return;
        }

        String selectedRole = (String) roleBox.getSelectedItem();
        if (!Validation.isValidString(selectedRole)) {
            showError("Please select a role.");
            return;
        }

        User loggedUser = checkCredentials(input, password);
        if (loggedUser == null) {
            showError("Invalid email/ID or password.");
            passwordField.setText("");
            return;
        }

        if (!loggedUser.getRole().equalsIgnoreCase(selectedRole)) {
            showError("Incorrect role selected. This account is registered as a " + loggedUser.getRole() + ".");
            passwordField.setText("");
            return;
        }

        if (loggedUser.getRole().equalsIgnoreCase("Student")) {
            new stdMainWindow((Student) loggedUser);
        } else if (loggedUser.getRole().equalsIgnoreCase("Instructor")) {
            new insMainWindow((Instructor) loggedUser);
        } else if (loggedUser.getRole().equalsIgnoreCase("Admin")) {
            new AdminDashboard((Admin) loggedUser);
        }

        dispose();
    }

    public User checkCredentials(String emailOrId, String plainPassword) {
        java.util.ArrayList<User> allUsers = userService.returnAllRecords();
        String hashedInput = Hashing.hashPassword(plainPassword);

        for (User user : allUsers) {
            boolean matchesEmail = user.getEmail().equalsIgnoreCase(emailOrId);
            boolean matchesId = user.getSearchKey().equalsIgnoreCase(emailOrId);

            if (matchesEmail || matchesId) {
                if (hashedInput.equals(user.getPasswordHash())) {
                    return user;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Login Error",
                JOptionPane.ERROR_MESSAGE);
    }
}