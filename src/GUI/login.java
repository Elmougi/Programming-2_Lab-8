package GUI;

import UserManagement.User;
import Database.UserService;
import Utilities.Hashing;


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

        String input = usernameField.getText();   // email OR id
        String password = new String(passwordField.getPassword());
        String selectedRole = roleBox.getSelectedItem().toString();

        if (input.trim().isEmpty() || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(LoginContainer, "Please fill all fields");
            return;
        }

        User loggedUser = checkCredentials(input, password);

        if (loggedUser == null) {
            JOptionPane.showMessageDialog(LoginContainer, "Invalid email/ID or password");
            passwordField.setText("");
            return;
        }

        if (!loggedUser.getRole().equalsIgnoreCase(selectedRole)) {
            JOptionPane.showMessageDialog(LoginContainer, "Incorrect role selected");
            return;
        }

        //JOptionPane.showMessageDialog(LoginContainer,
          //      "Login successful. Welcome " + loggedUser.getName());


        if (loggedUser.getRole().equalsIgnoreCase("Student")) {
            new stdMainWindow();
        } else if (loggedUser.getRole().equalsIgnoreCase("Instructor")) {
            new insMainWindow();
        }

        dispose();
    }


    public User checkCredentials(String emailOrId, String plainPassword) {

        ArrayList<User> allUsers = userService.returnAllRecords();

        String hashedInput = Hashing.hashPassword(plainPassword);

        for (User user : allUsers) {

            boolean matchesEmail = user.getEmail().equalsIgnoreCase(emailOrId);
            boolean matchesId = user.getSearchKey().equalsIgnoreCase(emailOrId);

            if (matchesEmail || matchesId) {

                if (user.getPasswordHash().equals(hashedInput)) {
                    return user; // SUCCESS
                } else {
                    return null; // WRONG PASSWORD
                }
            }
        }
        return null; // USER NOT FOUND
    }




}
