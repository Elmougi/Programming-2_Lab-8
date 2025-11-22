package GUI;

import CourseManagement.Course;
import UserManagement.Certificate;
import UserManagement.Student;

import javax.swing.*;

public class CertificateDetailsWindow extends JFrame {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JSeparator separatorTop;
    private JLabel certifiesLabel;
    private JLabel studentNameLabel;
    private JLabel studentIdLabel;
    private JLabel completedLabel;
    private JLabel courseTitleLabel;
    private JSeparator separatorBottom;
    private JLabel certificateIdLabel;
    private JLabel issueDateLabel;
    private JLabel courseIdLabel;
    private JButton closeButton;

    public CertificateDetailsWindow(JFrame parent, Certificate certificate, Course course, Student student) {
        setTitle("Certificate Details");
        setSize(600, 500);
        setLocationRelativeTo(parent);
        setContentPane(mainPanel);


        studentNameLabel.setText(student.getName());
        studentIdLabel.setText("Student ID: " + certificate.getStudentId());


        String courseTitle = (course != null) ? course.getTitle() : "Course (ID: " + certificate.getCourseId() + ")";
        courseTitleLabel.setText(courseTitle);


        certificateIdLabel.setText("Certificate ID: " + certificate.getCertificateId());
        issueDateLabel.setText("Issue Date: " + certificate.getIssueDate());
        courseIdLabel.setText("Course ID: " + certificate.getCourseId());


        closeButton.addActionListener(e -> dispose());

        setVisible(true);
    }
}