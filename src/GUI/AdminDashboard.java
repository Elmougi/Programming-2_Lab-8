package GUI;

import CourseManagement.Course;
import Database.CourseService;
import Database.UserService;
import UserManagement.Admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class AdminDashboard extends JFrame {
    private JPanel mainPanel;
    private JLabel welcomeLabel;
    private JLabel userLabel;
    private JButton logoutButton;
    private JButton refreshButton;
    private JScrollPane tableScrollPane;
    private JTable coursesTable;
    private JButton viewDetailsButton;
    private JButton approveButton;
    private JButton rejectButton;

    private Admin currentAdmin;
    private CourseService courseService;
    private UserService userService;

    public AdminDashboard(Admin admin) {
        this.currentAdmin = admin;
        this.courseService = new CourseService();
        this.userService = new UserService();

        setTitle("Admin Dashboard - " + admin.getName());
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        userLabel.setText("Welcome, " + admin.getName() + " (ID: " + admin.getSearchKey() + ")");

        loadPendingCourses();
        setupListeners();

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                saveAllData();
            }
        });

        setVisible(true);
    }

    private void loadPendingCourses() {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.addColumn("Course ID");
        model.addColumn("Title");
        model.addColumn("Description");
        model.addColumn("Instructor ID");
        model.addColumn("Lessons");
        model.addColumn("Status");

        ArrayList<Course> allCourses = courseService.returnAllRecords();
        for (Course course : allCourses) {
            if (course.getStatus().equals("PENDING")) {
                model.addRow(new Object[]{
                        course.getSearchKey(),
                        course.getTitle(),
                        course.getDescription().isEmpty() ? "N/A" : course.getDescription(),
                        course.getInstructorId(),
                        course.getLessons().size(),
                        course.getStatus()
                });
            }
        }

        if (model.getRowCount() == 0) {
            model.addRow(new Object[]{"No pending courses", "-", "-", "-", "-", "-"});
        }

        coursesTable.setModel(model);

        if (coursesTable.getColumnModel().getColumnCount() > 0) {
            coursesTable.getColumnModel().getColumn(0).setPreferredWidth(80);
            coursesTable.getColumnModel().getColumn(1).setPreferredWidth(200);
            coursesTable.getColumnModel().getColumn(2).setPreferredWidth(250);
            coursesTable.getColumnModel().getColumn(3).setPreferredWidth(100);
            coursesTable.getColumnModel().getColumn(4).setPreferredWidth(60);
            coursesTable.getColumnModel().getColumn(5).setPreferredWidth(80);
        }

        coursesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    private void setupListeners() {
        refreshButton.addActionListener(e -> {
            loadPendingCourses();
            JOptionPane.showMessageDialog(this,
                    "Course list refreshed successfully!",
                    "Refreshed",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        viewDetailsButton.addActionListener(e -> onViewDetailsClicked());
        approveButton.addActionListener(e -> onApproveCourse());
        rejectButton.addActionListener(e -> onRejectCourse());
        logoutButton.addActionListener(e -> onLogout());
    }

    private void onViewDetailsClicked() {
        int selectedRow = coursesTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a course to view details.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String courseId = (String) coursesTable.getValueAt(selectedRow, 0);
        if (courseId.equals("No pending courses")) {
            return;
        }

        Course course = courseService.getRecord(courseId);
        if (course == null) {
            JOptionPane.showMessageDialog(this,
                    "Course not found!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        showCourseDetailsDialog(course);
    }

    private void showCourseDetailsDialog(Course course) {
        JDialog detailsDialog = new JDialog(this, "Course Details", true);
        detailsDialog.setSize(600, 500);
        detailsDialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        panel.add(createInfoLabel("Course ID: " + course.getSearchKey(), true));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createInfoLabel("Title: " + course.getTitle(), true));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createInfoLabel("Description: " + (course.getDescription().isEmpty() ? "N/A" : course.getDescription()), false));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createInfoLabel("Instructor ID: " + course.getInstructorId(), false));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createInfoLabel("Status: " + course.getStatus(), false));
        panel.add(Box.createVerticalStrut(20));

        JLabel lessonsLabel = new JLabel("Lessons (" + course.getLessons().size() + "):");
        lessonsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lessonsLabel);
        panel.add(Box.createVerticalStrut(10));

        if (course.getLessons().isEmpty()) {
            panel.add(createInfoLabel("No lessons added yet", false));
        } else {
            for (int i = 0; i < course.getLessons().size(); i++) {
                panel.add(createInfoLabel((i + 1) + ". " + course.getLessons().get(i).getTitle(), false));
                panel.add(Box.createVerticalStrut(5));
            }
        }

        panel.add(Box.createVerticalGlue());

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> detailsDialog.dispose());
        panel.add(closeBtn);

        detailsDialog.setContentPane(new JScrollPane(panel));
        detailsDialog.setVisible(true);
    }

    private JLabel createInfoLabel(String text, boolean bold) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", bold ? Font.BOLD : Font.PLAIN, 13));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void onApproveCourse() {
        int selectedRow = coursesTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a course to approve.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String courseId = (String) coursesTable.getValueAt(selectedRow, 0);
        if (courseId.equals("No pending courses")) {
            return;
        }

        Course course = courseService.getRecord(courseId);
        if (course == null) {
            JOptionPane.showMessageDialog(this,
                    "Course not found!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to APPROVE this course?\n\nCourse: " + course.getTitle(),
                "Confirm Approval",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            currentAdmin.approveCourse(course);
            courseService.updateRecord(courseId, course);

            JOptionPane.showMessageDialog(this,
                    "Course '" + course.getTitle() + "' has been approved!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            loadPendingCourses();
        }
    }

    private void onRejectCourse() {
        int selectedRow = coursesTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a course to reject.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String courseId = (String) coursesTable.getValueAt(selectedRow, 0);
        if (courseId.equals("No pending courses")) {
            return;
        }

        Course course = courseService.getRecord(courseId);
        if (course == null) {
            JOptionPane.showMessageDialog(this,
                    "Course not found!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to REJECT this course?\n\nCourse: " + course.getTitle(),
                "Confirm Rejection",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            currentAdmin.rejectCourse(course);
            courseService.updateRecord(courseId, course);

            JOptionPane.showMessageDialog(this,
                    "Course '" + course.getTitle() + "' has been rejected.",
                    "Rejected",
                    JOptionPane.INFORMATION_MESSAGE);

            loadPendingCourses();
        }
    }

    private void onLogout() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?\n",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            saveAllData();
            dispose();
            new login();
        }
    }

    private void saveAllData() {
        courseService.saveToFile();
        userService.saveToFile();
    }
}