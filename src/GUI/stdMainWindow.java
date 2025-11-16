package GUI;

import CourseManagement.Course;
import CourseManagement.Lesson;
import Database.CourseService;
import Database.UserService;
import UserManagement.Student;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class stdMainWindow extends JFrame {
    private JPanel mainPanel;
    private JList<String> availableCoursesList;
    private JList<String> enrolledCoursesList;
    private JButton enrollButton;
    private JButton openCourseButton;
    private JButton refreshButton;
    private JButton logoutButton;
    private JScrollPane availableCoursesScroll;
    private JScrollPane enrolledCoursesScroll;
    private JLabel welcomeLabel;
    private JLabel userLabel;
    private JLabel availableLabel;
    private JLabel enrolledLabel;

    private DefaultListModel<String> availableCoursesModel;
    private DefaultListModel<String> enrolledCoursesModel;

    private CourseService courseService;
    private UserService userService;
    private Student currentStudent;

    private ArrayList<Course> allCourses;
    private ArrayList<Course> studentEnrolledCourses;

    private HashMap<String, HashSet<String>> lessonProgress;

    public stdMainWindow(Student student) {
        this.currentStudent = student;
        this.courseService = new CourseService();
        this.userService = new UserService();
        this.lessonProgress = new HashMap<>();

        userLabel.setText("Welcome, " + student.getName() + " (ID: " + student.getSearchKey() + ")");

        availableCoursesModel = new DefaultListModel<>();
        enrolledCoursesModel = new DefaultListModel<>();

        availableCoursesList.setModel(availableCoursesModel);
        enrolledCoursesList.setModel(enrolledCoursesModel);

        loadCourses();

        enrollButton.addActionListener(e -> onEnrollClicked());
        openCourseButton.addActionListener(e -> onOpenCourseClicked());
        refreshButton.addActionListener(e -> loadCourses());
        logoutButton.addActionListener(e -> onLogout());

        setTitle("Student Dashboard - " + student.getName());
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(mainPanel);
        setVisible(true);
    }

    private void loadCourses() {
        allCourses = courseService.returnAllRecords();
        studentEnrolledCourses = new ArrayList<>();

        availableCoursesModel.clear();
        enrolledCoursesModel.clear();

        for (Course course : allCourses) {
            String courseDisplay = course.getTitle() + " (ID: " + course.getSearchKey() + ")";

            if (course.searchStudent(currentStudent.getSearchKey()) != null) {
                enrolledCoursesModel.addElement(courseDisplay);
                studentEnrolledCourses.add(course);

                if (!lessonProgress.containsKey(course.getSearchKey())) {
                    lessonProgress.put(course.getSearchKey(), new HashSet<>());
                }
            } else {
                availableCoursesModel.addElement(courseDisplay);
            }
        }

        if (availableCoursesModel.isEmpty()) {
            availableCoursesModel.addElement("No available courses.");
        }
        if (enrolledCoursesModel.isEmpty()) {
            enrolledCoursesModel.addElement("No enrolled courses yet.");
        }
    }

    private void onEnrollClicked() {
        String selectedCourse = availableCoursesList.getSelectedValue();

        if (selectedCourse == null || selectedCourse.equals("No available courses.")) {
            JOptionPane.showMessageDialog(this,
                    "Please select a course to enroll in.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String courseId = extractCourseId(selectedCourse);

        Course course = courseService.getRecord(courseId);

        if (course == null) {
            JOptionPane.showMessageDialog(this,
                    "Course not found!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (course.searchStudent(currentStudent.getSearchKey()) != null) {
            JOptionPane.showMessageDialog(this,
                    "You are already enrolled in this course!",
                    "Already Enrolled",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
                "Do you want to enroll in:\n" + course.getTitle() + "?",
                "Confirm Enrollment",
                JOptionPane.YES_NO_OPTION);

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        course.addStudent(currentStudent);
        courseService.updateRecord(courseId, course);

        JOptionPane.showMessageDialog(this,
                "Successfully enrolled in: " + course.getTitle(),
                "Enrollment Success",
                JOptionPane.INFORMATION_MESSAGE);

        loadCourses();
    }

    private void onOpenCourseClicked() {
        String selectedCourse = enrolledCoursesList.getSelectedValue();

        if (selectedCourse == null || selectedCourse.equals("No enrolled courses yet.")) {
            JOptionPane.showMessageDialog(this,
                    "Please select an enrolled course to open.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String courseId = extractCourseId(selectedCourse);

        Course course = courseService.getRecord(courseId);

        if (course == null) {
            JOptionPane.showMessageDialog(this,
                    "Course not found!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }


        new CourseDetailsWindow(course, currentStudent, lessonProgress);
    }

    private void onLogout() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            new login();
        }
    }

    private String extractCourseId(String courseDisplay) {
        int startIndex = courseDisplay.indexOf("ID: ") + 4;
        int endIndex = courseDisplay.indexOf(")", startIndex);
        return courseDisplay.substring(startIndex, endIndex);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}