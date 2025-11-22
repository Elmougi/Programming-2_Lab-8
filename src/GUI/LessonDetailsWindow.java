package GUI;

import CourseManagement.Course;
import CourseManagement.Lesson;
import Database.CourseService;
import Database.UserService;
import UserManagement.Student;

import javax.swing.*;
import java.awt.*;

public class LessonDetailsWindow extends JDialog {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JLabel idLabel;
    private JLabel statusLabel;
    private JTextArea contentArea;
    private JScrollPane contentScroll;
    private JList<String> resourcesList;
    private JScrollPane resourcesScroll;
    private JButton takeQuizButton;
    private JButton closeButton;

    private DefaultListModel<String> resourcesModel;

    private Lesson lesson;
    private Course course;
    private Student student;
    private CourseService courseService;
    private UserService userService;
    private CourseDetailsWindow parentWindow;

    public LessonDetailsWindow(CourseDetailsWindow parent, Lesson lesson, Course course,
                               Student student, CourseService courseService, UserService userService) {
        super(parent, "Lesson: " + lesson.getTitle(), true);

        this.parentWindow = parent;
        this.lesson = lesson;
        this.course = course;
        this.student = student;
        this.courseService = courseService;
        this.userService = userService;

        setSize(700, 550);
        setLocationRelativeTo(parent);
        setContentPane(mainPanel);

        initializeData();
        setupListeners();

        setVisible(true);
    }

    private void initializeData() {
        titleLabel.setText("Lesson: " + lesson.getTitle());
        idLabel.setText("Lesson ID: " + lesson.getSearchKey());

        updateStatusLabel();

        contentArea.setText(lesson.getContent());
        contentArea.setCaretPosition(0);

        resourcesModel = new DefaultListModel<>();
        resourcesList.setModel(resourcesModel);

        if (lesson.getResources().isEmpty()) {
            resourcesModel.addElement("No resources available.");
        } else {
            for (String resource : lesson.getResources()) {
                resourcesModel.addElement("• " + resource);
            }
        }
    }

    private void updateStatusLabel() {
        boolean isCompleted = student.isLessonCompleted(course.getSearchKey(), lesson.getSearchKey());
        String status = isCompleted ? "✓ Completed" : "Not Completed";
        statusLabel.setText("Status: " + status);
        statusLabel.setForeground(isCompleted ? new Color(0, 128, 0) : Color.RED);
    }

    private void setupListeners() {
        takeQuizButton.addActionListener(e -> onTakeQuizClicked());
        closeButton.addActionListener(e -> {
            if (parentWindow != null) {
                parentWindow.refreshDisplay();
            }
            dispose();
        });
    }

    private void onTakeQuizClicked() {
        if (lesson.getQuiz() == null || lesson.getQuiz().getQuestions().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No quiz available for this lesson.",
                    "No Quiz",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        new QuizWindow(this, lesson, course, student, courseService, userService);

        updateStatusLabel();
        if (parentWindow != null) {
            parentWindow.refreshDisplay();
        }
    }

    public void refreshDisplay() {
        updateStatusLabel();
    }
}