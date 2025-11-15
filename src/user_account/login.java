package user_account;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

public class login {
    private JPanel LoginContainer;
    private JPasswordField passwordField;
    private JLabel PasswordLabel;
    private JLabel UsernameLabel;
    private JTextField usernameField;
    private JLabel Title;
    private JButton submit;

    private HashMap<String, String> users;


    public login() {
        users = new HashMap<>();
//        loadUsers();
//        setVisible(true);
//        setSize(400,400);
//        setContentPane(LoginContainer);
//        setTitle("Login");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//        submit.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                processInput();
//            }
//        });
    }

//    private void processInput() {
//        String username = usernameField.getText();
//        String password = new String(passwordField.getPassword()); //Arrays.toString(passwordField.getPassword());
//        if(username.trim().isEmpty() || password.trim().isEmpty()) {
//            JOptionPane.showMessageDialog(LoginContainer, "Please fill all the fields");
//            return;
//        }
//        if(checkCredentials(username,password)) {
//            System.out.println("Login Successful");
//            MainWindow mainWindow = new MainWindow();
//            dispose();
//        }
//        else {
//            JOptionPane.showMessageDialog(LoginContainer, "Invalid Credentials");
//            passwordField.setText("");
//            password = "";
//        }
//
//    }
//
//    private boolean checkCredentials(String username, String password) {
//        return users.containsKey(username) && users.get(username).equals(password);
//    }
//
//    private void loadUsers() {
//        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split(",");
//                if (parts.length == 2) {
//                    users.put(parts[0], parts[1]);
//                }
//            }
//            System.out.println("Loaded " + users.size() + " users");
//        } catch (IOException e) {
//            System.err.println("Error reading users.txt: " + e.getMessage());
//        }
//    }
//}
