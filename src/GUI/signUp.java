package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Utilities.*;
import UserManagement.*;
import Database.*;

public class signUp extends JFrame {

    private JPanel newPanel;
    private JTextField nameField;
    private JTextField emailField;
    private JComboBox roleBox;
    private JButton signUpButton;
    private JTextField idField;
    private JTextField passField;
    private JPanel signupPanel;
    private JButton loginButton;


    public signUp() {

        setTitle("SignUp Registration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
        setContentPane(newPanel);

        roleBox.addItem("Student");
        roleBox.addItem("Instructor");

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                processInput();
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new login();
                dispose();
            }
        });
    }

    private void processInput() {

        String name = nameField.getText();
        if (!Validation.isValidString(name)) {
            JOptionPane.showMessageDialog(this, "Name cannot be empty.");
            return;
        }

        String email = emailField.getText();
        if (!Validation.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Invalid email format.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idField.getText());
            if (!Validation.isValidInt(id)) {
                JOptionPane.showMessageDialog(this, "ID must be a positive number.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID must be a valid integer.");
            return;
        }

        String password = passField.getText();
        if (!Validation.isValidString(password)) {
            JOptionPane.showMessageDialog(this, "Password cannot be empty.");
            return;
        }

        String role = roleBox.getSelectedItem().toString();
        if (!Validation.isValidString(role)) {
            JOptionPane.showMessageDialog(this, "Please select a role.");
            return;
        }

        String hashedPassword = Hashing.hashPassword(password);

        UserService userService = new UserService();
        for (User u : userService.returnAllRecords()) {
            if (u.getEmail().equalsIgnoreCase(email) || u.getSearchKey().equals(String.valueOf(id))) {
                JOptionPane.showMessageDialog(this, "Email or ID already exists.");
                return;
            }
        }

        User newUser;
        if (role.equalsIgnoreCase("Student")) {
            newUser = new Student(name, String.valueOf(id), email, hashedPassword);
            new stdMainWindow((Student) newUser);
        } else {
            newUser = new Instructor(name, String.valueOf(id), email, hashedPassword);
            new insMainWindow((Instructor) newUser);
        }

        userService.insertRecord(newUser);
        dispose();

        // JOptionPane.showMessageDialog(this, role + " account created successfully!");
    }

}
