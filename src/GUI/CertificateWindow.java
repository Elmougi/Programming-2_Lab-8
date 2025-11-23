package GUI;

import CourseManagement.Course;
import Database.CourseService;
import Database.UserService;
import UserManagement.Certificate;
import UserManagement.Student;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CertificateWindow extends JFrame {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JScrollPane certificateScroll;
    private JList<String> certificateList;
    private JButton viewDetailsButton;
    private JButton closeButton;

    private DefaultListModel<String> certificateModel;
    private Student student;
    private CourseService courseService;
    private UserService userService;

    public CertificateWindow(Student student, CourseService courseService, UserService userService) {
        this.student = student;
        this.courseService = courseService;
        this.userService = userService;

        setTitle("Certificates - " + student.getName());
        setSize(800, 600);
        setLocationRelativeTo(null);
        setContentPane(mainPanel);

        certificateModel = new DefaultListModel<>();
        certificateList.setModel(certificateModel);

        loadCertificates();
        setupListeners();

        setVisible(true);
    }

    private void loadCertificates() {
        certificateModel.clear();

        List<Certificate> certificates = student.getCertificates();

        if (certificates.isEmpty()) {
            certificateModel.addElement("No certificates earned yet. Complete courses to earn certificates!");
        } else {
            for (Certificate certificate : certificates) {
                Course course = courseService.getRecord(certificate.getCourseId());
                String courseTitle = (course != null) ? course.getTitle() : "Unknown Course";

                String display = String.format("%s | Issued: %s | Certificate ID: %s",
                        courseTitle,
                        certificate.getIssueDate(),
                        certificate.getCertificateId());

                certificateModel.addElement(display);
            }
        }
    }

    private void setupListeners() {
        viewDetailsButton.addActionListener(e -> onViewDetailsClicked());
        closeButton.addActionListener(e -> dispose());
    }

    private void onViewDetailsClicked() {
        int selectedIndex = certificateList.getSelectedIndex();

        List<Certificate> certificates = student.getCertificates();

        if (selectedIndex < 0 || selectedIndex >= certificates.size()) {
            JOptionPane.showMessageDialog(this,
                    "Please select a certificate to view details.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Certificate certificate = certificates.get(selectedIndex);
        Course course = courseService.getRecord(certificate.getCourseId());

        new CertificateDetailsWindow(this, certificate, course, student);
    }
}