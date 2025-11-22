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

                String display = String.format("🏆 %s | Issued: %s | Certificate ID: %s",
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

        new CertificateDetailsDialog(this, certificate, course, student);
    }
}

class CertificateDetailsDialog extends JDialog {
    public CertificateDetailsDialog(JFrame parent, Certificate certificate, Course course, Student student) {
        super(parent, "Certificate Details", true);

        setSize(600, 500);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        mainPanel.setBackground(Color.WHITE);

        mainPanel.add(createCenteredLabel("✨ CERTIFICATE OF COMPLETION ✨", new Font("Arial", Font.BOLD, 22), new Color(50, 50, 150)));
        mainPanel.add(Box.createVerticalStrut(30));

        mainPanel.add(createSeparator());
        mainPanel.add(Box.createVerticalStrut(20));

        mainPanel.add(createCenteredLabel("This certifies that", new Font("Arial", Font.PLAIN, 14), Color.DARK_GRAY));
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(createCenteredLabel(student.getName(), new Font("Arial", Font.BOLD, 20), new Color(0, 100, 0)));
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(createCenteredLabel("Student ID: " + student.getSearchKey(), new Font("Arial", Font.ITALIC, 12), Color.GRAY));

        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(createCenteredLabel("has successfully completed", new Font("Arial", Font.PLAIN, 14), Color.DARK_GRAY));
        mainPanel.add(Box.createVerticalStrut(10));

        String courseTitle = (course != null) ? course.getTitle() : "Course (ID: " + certificate.getCourseId() + ")";
        mainPanel.add(createCenteredLabel(courseTitle, new Font("Arial", Font.BOLD, 18), new Color(0, 100, 200)));

        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(createSeparator());
        mainPanel.add(Box.createVerticalStrut(20));

        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.add(createLeftLabel("Certificate ID: " + certificate.getCertificateId(), new Font("Arial", Font.PLAIN, 12)));
        infoPanel.add(createLeftLabel("Issue Date: " + certificate.getIssueDate(), new Font("Arial", Font.PLAIN, 12)));
        infoPanel.add(createLeftLabel("Course ID: " + certificate.getCourseId(), new Font("Arial", Font.PLAIN, 12)));
        mainPanel.add(infoPanel);

        mainPanel.add(Box.createVerticalGlue());

        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.PLAIN, 13));
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.addActionListener(e -> dispose());
        mainPanel.add(closeButton);

        setContentPane(mainPanel);
        setVisible(true);
    }

    private JLabel createCenteredLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JLabel createLeftLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(Color.DARK_GRAY);
        return label;
    }

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setForeground(Color.LIGHT_GRAY);
        return separator;
    }
}