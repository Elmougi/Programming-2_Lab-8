package GUI;

import CourseManagement.Course;
import Database.CourseService;
import Database.UserService;
import UserManagement.Admin;
import UserManagement.Student;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;
public class AdminDashboard extends JFrame {
    private JPanel mainPanel;
    private JLabel welcomeLabel;
    private JLabel userLabel;
    private JButton logoutButton;

    private JTabbedPane statusTabs;

    private JPanel pendingPanel;
    private JButton refreshPendingButton;
    private JScrollPane pendingScrollPane;
    private JTable pendingTable;
    private DefaultTableModel pendingModel;

    private JPanel approvedPanel;
    private JButton refreshApprovedButton;
    private JScrollPane approvedScrollPane;
    private JTable approvedTable;
    private DefaultTableModel approvedModel;

    private JPanel rejectedPanel;
    private JButton refreshRejectedButton;
    private JScrollPane rejectedScrollPane;
    private JTable rejectedTable;
    private DefaultTableModel rejectedModel;

    private JButton viewDetailsButton;
    private JButton approveButton;
    private JButton rejectButton;

    private Admin currentAdmin;
    private CourseService courseService;
    private UserService userService;
    private Student student;

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

        loadCoursesForTable(pendingModel, "PENDING");
        loadCoursesForTable(approvedModel, "APPROVED");
        loadCoursesForTable(rejectedModel, "REJECTED");

        setupListeners();

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                saveAllData();
            }
        });

        setVisible(true);
    }

    private void createUIComponents() {

        pendingModel = createTableModel();
        pendingTable = new JTable(pendingModel);
        configureTable(pendingTable);

        approvedModel = createTableModel();
        approvedTable = new JTable(approvedModel);
        configureTable(approvedTable);

        rejectedModel = createTableModel();
        rejectedTable = new JTable(rejectedModel);
        configureTable(rejectedTable);
    }

    private DefaultTableModel createTableModel() {
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


        return model;
    }

    private void configureTable(JTable table) {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        SwingUtilities.invokeLater(() -> {
            if (table.getColumnModel().getColumnCount() > 0) {
                table.getColumnModel().getColumn(0).setPreferredWidth(80);
                table.getColumnModel().getColumn(1).setPreferredWidth(200);
                table.getColumnModel().getColumn(2).setPreferredWidth(250);
                table.getColumnModel().getColumn(3).setPreferredWidth(100);
                table.getColumnModel().getColumn(4).setPreferredWidth(60);
            }
        });
    }

    private void loadCoursesForTable(DefaultTableModel model, String status) {

        model.setRowCount(0);

        ArrayList<Course> allCourses = courseService.returnAllRecords();
        for (Course course : allCourses) {
            if (course.getStatus().equals(status)) {
                model.addRow(new Object[]{
                        course.getSearchKey(),
                        course.getTitle(),
                        course.getDescription().isEmpty() ? "N/A" : course.getDescription(),
                        course.getInstructorId(),
                        course.getLessons().size()
                });
            }
        }

        if (model.getRowCount() == 0) {
            model.addRow(new Object[]{"No " + status.toLowerCase() + " courses", "-", "-", "-", "-", "-"});
        }
    }

    private void setupListeners() {
        refreshPendingButton.addActionListener(e -> {
            loadCoursesForTable(pendingModel, "PENDING");
            JOptionPane.showMessageDialog(this,
                    "Pending courses refreshed!",
                    "Refreshed",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        refreshApprovedButton.addActionListener(e -> {
            loadCoursesForTable(approvedModel, "APPROVED");
            JOptionPane.showMessageDialog(this,
                    "Approved courses refreshed!",
                    "Refreshed",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        refreshRejectedButton.addActionListener(e -> {
            loadCoursesForTable(rejectedModel, "REJECTED");
            JOptionPane.showMessageDialog(this,
                    "Rejected courses refreshed!",
                    "Refreshed",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        viewDetailsButton.addActionListener(e -> onViewDetailsClicked());
        approveButton.addActionListener(e -> onApproveCourse());
        rejectButton.addActionListener(e -> onRejectCourse());
        logoutButton.addActionListener(e -> onLogout());
    }

    private JTable getCurrentTable() {
        int selectedTab = statusTabs.getSelectedIndex();
        switch (selectedTab) {
            case 0:
                return pendingTable;
            case 1:
                return approvedTable;
            case 2:
                return rejectedTable;
            default:
                return pendingTable;
        }
    }



    private void onViewDetailsClicked() {
        JTable currentTable = getCurrentTable();
        int selectedRow = currentTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a course to view details.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String courseId = (String) currentTable.getValueAt(selectedRow, 0);
        if (courseId.startsWith("No ")) {
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

        new AdminCourseDetails(this, course);
    }

    private void onApproveCourse() {
        JTable currentTable = getCurrentTable();
        int selectedRow = currentTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a course to approve.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String courseId = (String) currentTable.getValueAt(selectedRow, 0);
        if (courseId.startsWith("No ")) {
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

            refreshAllTables();
        }
    }

    private void onRejectCourse() {
        JTable currentTable = getCurrentTable();
        int selectedRow = currentTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a course to reject.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String courseId = (String) currentTable.getValueAt(selectedRow, 0);
        if (courseId.startsWith("No ")) {
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
                "Are you sure you want to REJECT this course?\n\nCourse: " + course.getTitle() +
                        "\n\nThis will remove all enrolled students from this course.",
                "Confirm Rejection",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {

            currentAdmin.rejectCourse(course);


            List<Student> enrolledStudents = new ArrayList<>(); //to be ready for threads if needed in lab 9 or 10 or 200 :) i get new copy for concurrent events
            //all the refresh buttons as well are for threads too if needed
            ArrayList<UserManagement.User> allUsers = userService.returnAllRecords();

            for (UserManagement.User user : allUsers) {
                if (user instanceof Student) {
                    Student student = (Student) user;
                    if (student.getProgress().containsKey(courseId)) {
                        enrolledStudents.add(student);
                    }
                }
            }


            for (Student enrolledStudent : enrolledStudents) {
                enrolledStudent.dropCourse(courseService, courseId, enrolledStudent);
                userService.updateRecord(enrolledStudent.getSearchKey(), enrolledStudent);
            }

            course.getStudents().clear();

            courseService.updateRecord(courseId, course);

            JOptionPane.showMessageDialog(this,
                    "Course '" + course.getTitle() + "' has been rejected.\n" +
                            enrolledStudents.size() + " student(s) have been unenrolled.",
                    "Rejected",
                    JOptionPane.INFORMATION_MESSAGE);

            refreshAllTables();
        }
    }


    private void refreshAllTables() {
        loadCoursesForTable(pendingModel, "PENDING");
        loadCoursesForTable(approvedModel, "APPROVED");
        loadCoursesForTable(rejectedModel, "REJECTED");
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