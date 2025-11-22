package GUI;

import javax.swing.*;
import java.awt.*;
import Utilities.*;
import UserManagement.*;
import Database.*;

public class signUp extends JFrame {
    private JPanel mainPanel;
    private JPanel signupCard;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JLabel nameLabel;
    private JTextField nameField;
    private JLabel emailLabel;
    private JTextField emailField;
    private JLabel idLabel;
    private JTextField idField;
    private JLabel passwordLabel;
    private JPasswordField passField;
    private JLabel roleLabel;
    private JComboBox<String> roleBox;
    private JButton signUpButton;
    private JLabel loginLabel;
    private JButton loginButton;

    public signUp() {
        setTitle("SkillForge - Sign Up");
        setSize(650, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(mainPanel);


        signupCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(40, 60, 40, 60)
        ));


        roleBox.addItem("Student");
        roleBox.addItem("Instructor");
        roleBox.addItem("Admin");


        signUpButton.addActionListener(e -> processSignup());
        loginButton.addActionListener(e -> {
            new login();
            dispose();
        });


        nameField.addActionListener(e -> emailField.requestFocus());
        emailField.addActionListener(e -> idField.requestFocus());
        idField.addActionListener(e -> passField.requestFocus());
        passField.addActionListener(e -> processSignup());

        setVisible(true);
    }

    private void processSignup() {

        String name = nameField.getText().trim();
        if (!Validation.isValidString(name)) {
            showError("Name cannot be empty.");
            return;
        }


        String email = emailField.getText().trim();
        if (!Validation.isValidEmail(email)) {
            showError("Invalid email format. Please use a valid email address.");
            return;
        }


        int id;
        try {
            id = Integer.parseInt(idField.getText().trim());
            if (!Validation.isValidInt(id)) {
                showError("ID must be a positive number.");
                return;
            }
        } catch (NumberFormatException e) {
            showError("ID must be a valid number.");
            return;
        }


        String password = new String(passField.getPassword());
        if (!Validation.isValidString(password)) {
            showError("Password cannot be empty.");
            return;
        }

        if (password.length() < 6) {
            showError("Password must be at least 6 characters long.");
            return;
        }


        String role = (String) roleBox.getSelectedItem();
        if (!Validation.isValidString(role)) {
            showError("Please select a role.");
            return;
        }


        String hashedPassword = Hashing.hashPassword(password);


        UserService userService = new UserService();
        for (User u : userService.returnAllRecords()) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                showError("An account with this email already exists.\nPlease use a different email or sign in.");
                return;
            }
            if (u.getSearchKey().equals(String.valueOf(id))) {
                showError("This User ID is already taken.\nPlease choose a different ID.");
                return;
            }
        }


        User newUser;
        try {
            if (role.equalsIgnoreCase("Student")) {
                newUser = new Student(name, String.valueOf(id), email, hashedPassword);
                userService.insertRecord(newUser);

                JOptionPane.showMessageDialog(this,
                        "Account created successfully!\nWelcome to SkillForge, " + name + "!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                new stdMainWindow((Student) newUser);
            } else if (role.equalsIgnoreCase("Instructor")) {
                newUser = new Instructor(name, String.valueOf(id), email, hashedPassword);
                userService.insertRecord(newUser);

                JOptionPane.showMessageDialog(this,
                        "Account created successfully!\nWelcome to SkillForge, " + name + "!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                new insMainWindow((Instructor) newUser);
            } else if (role.equalsIgnoreCase("Admin")) {
                newUser = new Admin(name, String.valueOf(id), email, hashedPassword);
                userService.insertRecord(newUser);

                JOptionPane.showMessageDialog(this,
                        "Account created successfully!\nWelcome to SkillForge, " + name + "!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                new AdminDashboard((Admin) newUser);
            } else {
                return;
            }

            dispose();
        } catch (Exception e) {
            showError("Error creating account: " + e.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Registration Error",
                JOptionPane.ERROR_MESSAGE);
    }
}