package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class signUp extends JFrame {

    private JPanel newPanel;
    private JTextField nameField;
    private JTextField emailField;
    private JComboBox roleBox;
    private JButton signUpButton;
    private JTextField idField;
    private JTextField passField;
    private JPanel signupPanel;


    public  signUp() {

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

    }

    private void processInput() {

        String name = nameField.getText();
        String email = emailField.getText();
        int id = Integer.parseInt( idField.getText() );
        String password = passField.getText();
        String role = roleBox.getSelectedItem().toString();


    }

}
