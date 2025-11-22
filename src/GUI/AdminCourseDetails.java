package GUI;

import CourseManagement.Course;
import javax.swing.*;

public class AdminCourseDetails extends JFrame {
    private JPanel mainPanel;
    private JLabel courseIdLabel;
    private JLabel titleLabel;
    private JLabel descriptionLabel;
    private JLabel instructorIdLabel;
    private JLabel statusLabel;
    private JLabel lessonsCountLabel;
    private JList<String> lessonsList;
    private JScrollPane lessonsScroll;
    private JButton closeButton;

    private DefaultListModel<String> lessonsModel;

    public AdminCourseDetails(JFrame parent, Course course) {
        setTitle("Course Details - " + course.getTitle());
        setSize(600, 500);
        setLocationRelativeTo(parent);
        setContentPane(mainPanel);


        courseIdLabel.setText("Course ID: " + course.getSearchKey());
        titleLabel.setText("Title: " + course.getTitle());
        descriptionLabel.setText("Description: " + (course.getDescription().isEmpty() ? "N/A" : course.getDescription()));
        instructorIdLabel.setText("Instructor ID: " + course.getInstructorId());
        statusLabel.setText("Status: " + course.getStatus());


        lessonsModel = new DefaultListModel<>();
        lessonsList.setModel(lessonsModel);

        lessonsCountLabel.setText("Total Lessons: " + course.getLessons().size());

        if (course.getLessons().isEmpty()) {
            lessonsModel.addElement("No lessons added yet");
        } else {
            for (int i = 0; i < course.getLessons().size(); i++) {
                lessonsModel.addElement((i + 1) + ". " + course.getLessons().get(i).getTitle());
            }
        }


        closeButton.addActionListener(e -> dispose());

        setVisible(true);
    }
}