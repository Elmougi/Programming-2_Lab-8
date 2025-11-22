package GUI;

import CourseManagement.Course;
import CourseManagement.Lesson;
import Database.CourseService;
import Database.UserService;
import UserManagement.Student;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class stdMainWindow extends JFrame {
    private JPanel mainPanel;
    private JList<String> availableCoursesList;
    private JList<String> enrolledCoursesList;
    private JButton enrollButton;
    private JButton openCourseButton;
    private JButton dropCourseButton;
    private JButton refreshButton;
    private JButton logoutButton;
    private JScrollPane availableCoursesScroll;
    private JScrollPane enrolledCoursesScroll;
    private JLabel welcomeLabel;
    private JLabel userLabel;
    private JLabel availableLabel;
    private JLabel enrolledLabel;
    private JButton certificatesButton;

    private DefaultListModel<String> availableCoursesModel;
    private DefaultListModel<String> enrolledCoursesModel;

    private CourseService courseService;
    private UserService userService;
    private Student currentStudent;

    private ArrayList<Course> allCourses;

    public stdMainWindow(Student student) {
        this.currentStudent = student;
        this.courseService = new CourseService();
        this.userService = new UserService();

        userLabel.setText("Welcome, " + student.getName() + " (ID: " + student.getSearchKey() + ")");

        availableCoursesModel = new DefaultListModel<>();
        enrolledCoursesModel = new DefaultListModel<>();

        availableCoursesList.setModel(availableCoursesModel);
        enrolledCoursesList.setModel(enrolledCoursesModel);

        loadCourses();

        enrollButton.addActionListener(e -> onEnrollClicked());
        openCourseButton.addActionListener(e -> onOpenCourseClicked());
        dropCourseButton.addActionListener(e -> onDropCourseClicked());
        refreshButton.addActionListener(e -> onRefreshClicked());
        certificatesButton.addActionListener(e -> onViewCertificatesClicked());
        logoutButton.addActionListener(e -> onLogout());


        setTitle("Student Dashboard - " + student.getName());
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(mainPanel);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                saveAllData();
            }
        });

        setVisible(true);
    }

    private void loadCourses() {
       
        Student freshStudent = (Student) userService.getRecord(currentStudent.getSearchKey());
        if (freshStudent != null) {
            currentStudent = freshStudent;
        }

        allCourses = courseService.returnAllRecords();

        availableCoursesModel.clear();
        enrolledCoursesModel.clear();

        List<Course> enrolledCourses = currentStudent.getEnrolledCourses(courseService);

        for (Course course : allCourses) {
            String courseDisplay = course.getTitle() + " (ID: " + course.getSearchKey() + ")";

            boolean isEnrolled = false;
            for (Course enrolledCourse : enrolledCourses) {
                if (enrolledCourse.getSearchKey().equals(course.getSearchKey())) {
                    isEnrolled = true;
                    break;
                }
            }

            if (isEnrolled) {
                enrolledCoursesModel.addElement(courseDisplay);
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

    private void onRefreshClicked() {
        
        saveAllData();

        
        courseService = new CourseService();
        userService = new UserService();

    
        loadCourses();

        
        JOptionPane.showMessageDialog(this,
                "Course lists have been refreshed successfully!",
                "Refresh Complete",
                JOptionPane.INFORMATION_MESSAGE);
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

        try {
            
            currentStudent.enrollInCourse(courseService, courseId, currentStudent);

            course.addStudent(currentStudent);
            courseService.updateRecord(courseId, course);

           
            userService.updateRecord(currentStudent.getSearchKey(), currentStudent);

            JOptionPane.showMessageDialog(this,
                    "Successfully enrolled in: " + course.getTitle(),
                    "Enrollment Success",
                    JOptionPane.INFORMATION_MESSAGE);

            loadCourses();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    "Error enrolling in course: " + e.getMessage(),
                    "Enrollment Error",
                    JOptionPane.ERROR_MESSAGE);
        }
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

        new CourseDetailsWindow(course, currentStudent, courseService, userService);
    }

    private void onViewCertificatesClicked() {
        new CertificateWindow(currentStudent, courseService, userService);
    }

    private void onLogout() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?\nAll changes will be saved.",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            
            saveAllData();

            
            dispose();

           
            new login();
        }
    }


    private void onDropCourseClicked() {
        String selectedCourse = enrolledCoursesList.getSelectedValue();

        if (selectedCourse == null || selectedCourse.equals("No enrolled courses yet.")) {
            JOptionPane.showMessageDialog(this,
                    "Please select an enrolled course to drop.",
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

        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to drop:\n" + course.getTitle() + "?\n\nAll progress in this course will be lost.",
                "Confirm Drop Course",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            
            currentStudent.dropCourse(courseService, courseId, currentStudent);

            
            course.removeStudent(currentStudent.getSearchKey());
            courseService.updateRecord(courseId, course);

           
            userService.updateRecord(currentStudent.getSearchKey(), currentStudent);

            JOptionPane.showMessageDialog(this,
                    "Successfully dropped: " + course.getTitle(),
                    "Course Dropped",
                    JOptionPane.INFORMATION_MESSAGE);

            loadCourses();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    "Error dropping course: " + e.getMessage(),
                    "Drop Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private String extractCourseId(String courseDisplay) {
        int startIndex = courseDisplay.indexOf("ID: ") + 4;
        int endIndex = courseDisplay.indexOf(")", startIndex);
        return courseDisplay.substring(startIndex, endIndex);
    }

    private void saveAllData() {
        
        userService.updateRecord(currentStudent.getSearchKey(), currentStudent);

        
        courseService.saveToFile();

       
        userService.saveToFile();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
