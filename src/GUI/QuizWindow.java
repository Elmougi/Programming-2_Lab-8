package GUI;

import CourseManagement.Course;
import CourseManagement.Lesson;
import CourseManagement.Quiz;
import Database.CourseService;
import Database.UserService;
import UserManagement.Student;

import javax.swing.*;

public class QuizWindow extends JDialog {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JLabel questionsCountLabel;
    private JButton startQuizButton;
    private JButton cancelButton;

    private Lesson lesson;
    private Course course;
    private Student student;
    private CourseService courseService;
    private UserService userService;
    private LessonDetailsWindow parentWindow;
    private Quiz quiz;

    public QuizWindow(LessonDetailsWindow parent, Lesson lesson, Course course,
                      Student student, CourseService courseService, UserService userService) {
        super(parent, "Quiz - " + lesson.getTitle(), true);

        this.parentWindow = parent;
        this.lesson = lesson;
        this.course = course;
        this.student = student;
        this.courseService = courseService;
        this.userService = userService;
        this.quiz = lesson.getQuiz();

        setSize(600, 400);
        setLocationRelativeTo(parent);
        setContentPane(mainPanel);

        initializeQuiz();
        setupListeners();

        setVisible(true);
    }

    private void initializeQuiz() {
        titleLabel.setText("Quiz: " + lesson.getTitle());

        if (quiz == null || quiz.getQuestions().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No quiz available for this lesson.",
                    "No Quiz",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
            return;
        }

        questionsCountLabel.setText("Total Questions: " + quiz.getQuestions().size());
    }

    private void setupListeners() {
        startQuizButton.addActionListener(e -> onStartQuiz());
        cancelButton.addActionListener(e -> dispose());
    }

    private void onStartQuiz() {
       /* if (student.getTotalAttemptOfQuiz(course.getSearchKey() , lesson.getSearchKey() ) >= 3){
            JOptionPane.showMessageDialog(this,
                    "You have reached the maximum number of attempts for this Quiz",
                    "No Quiz",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        dispose();

        new QuestionsWindow(parentWindow, lesson, course, student, courseService, userService);
    */}
}