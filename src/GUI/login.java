package GUI;

import UserManagement.User;
import UserManagement.Student;
import UserManagement.Instructor;
import Database.UserService;
import Utilities.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class login extends JFrame {

    private JPanel LoginContainer;
    private JPasswordField passwordField;
    private JLabel PasswordLabel;
    private JLabel UsernameLabel;
    private JTextField usernameField;
    private JLabel Title;
    private JButton submit;
    private JButton signButton;
    private JLabel signLabel;
    private JComboBox roleBox;
    private JLabel roleLabel;

    private UserService userService = new UserService();

    public login() {

        setVisible(true);
        setSize(400,400);
        setContentPane(LoginContainer);
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        roleBox.addItem("Student");
        roleBox.addItem("Instructor");

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processInput();
            }
        });

        signButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new signUp();
                dispose();
            }
        });
    }


    private void processInput() {

        String input = usernameField.getText();
        if (!Validation.isValidString(input)) {
            JOptionPane.showMessageDialog(LoginContainer, "Email or ID cannot be empty.");
            passwordField.setText("");
            return;
        }

        String password = new String(passwordField.getPassword());
        if (!Validation.isValidString(password)) {
            JOptionPane.showMessageDialog(LoginContainer, "Password cannot be empty.");
            passwordField.setText("");
            return;
        }

        String selectedRole = roleBox.getSelectedItem().toString();
        if (!Validation.isValidString(selectedRole)) {
            JOptionPane.showMessageDialog(LoginContainer, "Please select a role.");
            passwordField.setText("");
            return;
        }

        User loggedUser = checkCredentials(input, password);
        if (loggedUser == null) {
            JOptionPane.showMessageDialog(LoginContainer, "Invalid email/ID or password.");
            passwordField.setText("");
            return;
        }

        if (!loggedUser.getRole().equalsIgnoreCase(selectedRole)) {
            JOptionPane.showMessageDialog(LoginContainer, "Incorrect role selected.");
            passwordField.setText("");
            return;
        }

        if (loggedUser.getRole().equalsIgnoreCase("Student")) {
            new stdMainWindow( (Student) loggedUser);
        } else {
            new insMainWindow();
        }

        dispose();
    }


    public User checkCredentials(String emailOrId, String plainPassword) {

        ArrayList<User> allUsers = userService.returnAllRecords();

        String hashedInput = Hashing.hashPassword(plainPassword);

        for (User user : allUsers) {
//            System.out.println("enterd");

            boolean matchesEmail = user.getEmail().equalsIgnoreCase(emailOrId);
            boolean matchesId = user.getSearchKey().equalsIgnoreCase(emailOrId);

            if (matchesEmail || matchesId) {
//                System.out.println("enterd");
                System.out.println(hashedInput);
                if (user.getPasswordHash().equals(hashedInput)) {
//                    System.out.println("here?!");
                    return user;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

}
