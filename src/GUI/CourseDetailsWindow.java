package GUI;

import CourseManagement.Course;
import CourseManagement.Lesson;
import UserManagement.Student;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;

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
    private HashMap<String, HashSet<String>> lessonProgress;

    public CourseDetailsWindow(Course course, Student student, HashMap<String, HashSet<String>> lessonProgress) {
        this.course = course;
        this.student = student;
        this.lessonProgress = lessonProgress;

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
            HashSet<String> completed = lessonProgress.getOrDefault(course.getSearchKey(), new HashSet<>());
            for (int i = 0; i < course.getLessons().size(); i++) {
                Lesson lesson = course.getLessons().get(i);
                String status = completed.contains(lesson.getSearchKey()) ? " ✓ [Completed]" : "";
                lessonsModel.addElement((i + 1) + ". " + lesson.getTitle() + status);
            }
        }
    }

    private void updateProgressLabel() {
        int totalLessons = course.getLessons().size();
        int completedLessons = lessonProgress.getOrDefault(course.getSearchKey(), new HashSet<>()).size();
        double progressPercent = totalLessons > 0 ? (completedLessons * 100.0 / totalLessons) : 0;

        progressLabel.setText(String.format("Progress: %d/%d lessons completed (%.1f%%)",
                completedLessons, totalLessons, progressPercent));
    }

    private void setupListeners() {
        viewLessonButton.addActionListener(e -> onViewLessonClicked());
        markCompleteButton.addActionListener(e -> onMarkCompleteClicked());
        closeButton.addActionListener(e -> dispose());
    }

    private void onViewLessonClicked() {
        int selectedIndex = lessonsList.getSelectedIndex();

        if (selectedIndex >= 0 && selectedIndex < course.getLessons().size()) {
            Lesson selectedLesson = course.getLessons().get(selectedIndex);
            // Use the new separate LessonDetailsWindow
            new LessonDetailsWindow(this, selectedLesson, course, lessonProgress);
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
        HashSet<String> completedLessons = lessonProgress.getOrDefault(course.getSearchKey(), new HashSet<>());

        if (completedLessons.contains(lesson.getSearchKey())) {
            JOptionPane.showMessageDialog(this,
                    "This lesson is already marked as completed!",
                    "Already Completed",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            completedLessons.add(lesson.getSearchKey());
            lessonProgress.put(course.getSearchKey(), completedLessons);

            JOptionPane.showMessageDialog(this,
                    "Lesson '" + lesson.getTitle() + "' marked as completed!",
                    "Progress Updated",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}