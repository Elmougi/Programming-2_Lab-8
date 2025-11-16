package GUI;

import CourseManagement.Course;
import CourseManagement.Lesson;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;

public class LessonDetailsWindow extends JDialog {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JLabel idLabel;
    private JLabel statusLabel;
    private JTextArea contentArea;
    private JScrollPane contentScroll;
    private JList<String> resourcesList;
    private JScrollPane resourcesScroll;
    private JButton closeButton;

    private DefaultListModel<String> resourcesModel;

    private Lesson lesson;
    private Course course;
    private HashMap<String, HashSet<String>> lessonProgress;

    public LessonDetailsWindow(JFrame parent, Lesson lesson, Course course, HashMap<String, HashSet<String>> lessonProgress) {
        super(parent, "Lesson: " + lesson.getTitle(), true);

        this.lesson = lesson;
        this.course = course;
        this.lessonProgress = lessonProgress;

        setSize(700, 500);
        setLocationRelativeTo(parent);
        setContentPane(mainPanel);

        initializeData();
        setupListeners();

        setVisible(true);
    }

    private void initializeData() {

        titleLabel.setText("Lesson: " + lesson.getTitle());
        idLabel.setText("Lesson ID: " + lesson.getSearchKey());


        HashSet<String> completed = lessonProgress.getOrDefault(course.getSearchKey(), new HashSet<>());
        boolean isCompleted = completed.contains(lesson.getSearchKey());
        String status = isCompleted ? "✓ Completed" : "Not Completed";

        statusLabel.setText("Status: " + status);
        statusLabel.setForeground(isCompleted ? new Color(0, 128, 0) : Color.RED);


        contentArea.setText(lesson.getContent());
        contentArea.setCaretPosition(0); // Scroll to top


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

    private void setupListeners() {
        closeButton.addActionListener(e -> dispose());
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}