package GUI;

import CourseManagement.Course;
import CourseManagement.Lesson;
import Database.CourseService;
import Database.UserService;
import UserManagement.Student;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CourseDetailsWindow extends JFrame {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JLabel idLabel;
    private JLabel descLabel;
    private JLabel progressLabel;
    private JList<String> lessonsList;
    private JScrollPane lessonsScroll;
    private JButton viewLessonButton;
    private JButton markCompleteButton;
    private JButton closeButton;

    private DefaultListModel<String> lessonsModel;

    private Course course;
    private Student student;
    private CourseService courseService;
    private UserService userService;

    public CourseDetailsWindow(Course course, Student student, CourseService courseService, UserService userService) {
        this.course = course;
        this.student = student;
        this.courseService = courseService;
        this.userService = userService;

        setTitle("Course: " + course.getTitle());
        setSize(800, 600);
        setLocationRelativeTo(null);
        setContentPane(mainPanel);

        initializeData();
        setupListeners();

        setVisible(true);
    }

    private void initializeData() {

        titleLabel.setText("Title: " + course.getTitle());
        idLabel.setText("Course ID: " + course.getSearchKey());

        String description = course.getDescription().isEmpty() ? "No description available" : course.getDescription();
        descLabel.setText("Description: " + description);


        lessonsModel = new DefaultListModel<>();
        lessonsList.setModel(lessonsModel);


        updateLessonsList();
        updateProgressLabel();
    }

    private void updateLessonsList() {
        lessonsModel.clear();

        if (course.getLessons().isEmpty()) {
            lessonsModel.addElement("No lessons available yet.");
        } else {

            List<Lesson> allLessons = student.getAllLessons(courseService, course.getSearchKey());

            for (int i = 0; i < allLessons.size(); i++) {
                Lesson lesson = allLessons.get(i);

                boolean isCompleted = student.isLessonCompleted(course.getSearchKey(), lesson.getSearchKey());
                String status = isCompleted ? " ✓ [Completed]" : "";
                lessonsModel.addElement((i + 1) + ". " + lesson.getTitle() + status);
            }
        }
    }

    private void updateProgressLabel() {
        int totalLessons = course.getLessons().size();


        List<Lesson> completedLessons = student.getCompletedLessons(courseService, course.getSearchKey());
        int completedCount = completedLessons.size();

        double progressPercent = totalLessons > 0 ? (completedCount * 100.0 / totalLessons) : 0;

        progressLabel.setText(String.format("Progress: %d/%d lessons completed (%.1f%%)",
                completedCount, totalLessons, progressPercent));
    }

    private void setupListeners() {
        viewLessonButton.addActionListener(e -> onViewLessonClicked());
        markCompleteButton.addActionListener(e -> onMarkCompleteClicked());
        closeButton.addActionListener(e -> {

            userService.updateRecord(student.getSearchKey(), student);
            dispose();
        });
    }

    private void onViewLessonClicked() {
        int selectedIndex = lessonsList.getSelectedIndex();

        if (selectedIndex >= 0 && selectedIndex < course.getLessons().size()) {
            Lesson selectedLesson = course.getLessons().get(selectedIndex);

            new LessonDetailsWindow(this, selectedLesson, course, student, courseService, userService);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please select a lesson to view.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onMarkCompleteClicked() {
        int selectedIndex = lessonsList.getSelectedIndex();

        if (selectedIndex >= 0 && selectedIndex < course.getLessons().size()) {
            Lesson selectedLesson = course.getLessons().get(selectedIndex);
            markLessonComplete(selectedLesson);


            updateLessonsList();
            updateProgressLabel();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please select a lesson to mark as completed.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void markLessonComplete(Lesson lesson) {

        boolean isCompleted = student.isLessonCompleted(course.getSearchKey(), lesson.getSearchKey());

        if (isCompleted) {
            JOptionPane.showMessageDialog(this,
                    "This lesson is already marked as completed!",
                    "Already Completed",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            try {

                student.markLessonCompleted(course.getSearchKey(), lesson.getSearchKey());


                userService.updateRecord(student.getSearchKey(), student);

                JOptionPane.showMessageDialog(this,
                        "Lesson '" + lesson.getTitle() + "' marked as completed!",
                        "Progress Updated",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this,
                        "Error marking lesson as completed: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    public void refreshDisplay() {
        updateLessonsList();
        updateProgressLabel();
    }


}