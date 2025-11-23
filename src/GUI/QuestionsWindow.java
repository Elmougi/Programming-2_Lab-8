package GUI;

import CourseManagement.Course;
import CourseManagement.Lesson;
import CourseManagement.Question;
import CourseManagement.Quiz;
import Database.CourseService;
import Database.UserService;
import UserManagement.Student;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionsWindow extends JDialog {
    private JPanel mainPanel;
    private JLabel progressLabel;
    private JScrollPane questionsScroll;
    private JPanel questionsPanel;
    private JButton submitButton;
    private JButton closeButton;
    private JPanel resultPanel;
    private JLabel scoreLabel;
    private JLabel statusLabel;

    private Lesson lesson;
    private Course course;
    private Student student;
    private CourseService courseService;
    private UserService userService;
    private LessonDetailsWindow parentWindow;

    private List<ButtonGroup> answerGroups;
    private List<JRadioButton[]> radioButtons;
    private Quiz quiz;

    public QuestionsWindow(LessonDetailsWindow parent, Lesson lesson, Course course,
                           Student student, CourseService courseService, UserService userService) {
        super(parent, "Quiz Questions: " + lesson.getTitle(), true);

        this.parentWindow = parent;
        this.lesson = lesson;
        this.course = course;
        this.student = student;
        this.courseService = courseService;
        this.userService = userService;
        this.quiz = lesson.getQuiz();

        this.answerGroups = new ArrayList<>();
        this.radioButtons = new ArrayList<>();

        setSize(750, 650);
        setLocationRelativeTo(parent);
        setContentPane(mainPanel);

        initializeQuestions();
        setupListeners();

        setVisible(true);
    }

    private void initializeQuestions() {
        progressLabel.setText("Questions: " + quiz.getQuestions().size());

        questionsPanel.setLayout(new BoxLayout(questionsPanel, BoxLayout.Y_AXIS));

        List<Question> questions = quiz.getQuestions();
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            addQuestionPanel(i + 1, question);
        }

        resultPanel.setVisible(false);
    }

    private void addQuestionPanel(int questionNumber, Question question) {
        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
        questionPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        questionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        questionPanel.setBackground(Color.WHITE);

        JLabel questionLabel = new JLabel("Q" + questionNumber + ": " + question.getQuestionText());
        questionLabel.setFont(new Font("Arial", Font.BOLD, 13));
        questionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        questionPanel.add(questionLabel);

        questionPanel.add(Box.createVerticalStrut(10));

        ButtonGroup buttonGroup = new ButtonGroup();
        List<String> options = question.getOptions();
        JRadioButton[] buttons = new JRadioButton[options.size()];

        for (int i = 0; i < options.size(); i++) {
            JRadioButton radioButton = new JRadioButton(options.get(i));
            radioButton.setFont(new Font("Arial", Font.PLAIN, 12));
            radioButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            radioButton.setBackground(Color.WHITE);
            buttonGroup.add(radioButton);
            buttons[i] = radioButton;
            questionPanel.add(radioButton);
        }

        answerGroups.add(buttonGroup);
        radioButtons.add(buttons);

        questionsPanel.add(questionPanel);
        questionsPanel.add(Box.createVerticalStrut(10));
    }

    private void setupListeners() {
        submitButton.addActionListener(e -> onSubmitQuiz());
        closeButton.addActionListener(e -> dispose());
    }

    private void onSubmitQuiz() {
        List<Integer> userAnswers = new ArrayList<>();

        for (int i = 0; i < radioButtons.size(); i++) {
            JRadioButton[] buttons = radioButtons.get(i);
            int selectedAnswer = -1;

            for (int j = 0; j < buttons.length; j++) {
                if (buttons[j].isSelected()) {
                    selectedAnswer = j;
                    break;
                }
            }

            if (selectedAnswer == -1) {
                JOptionPane.showMessageDialog(this,
                        "Please answer all questions before submitting!",
                        "Incomplete Quiz",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            userAnswers.add(selectedAnswer);
        }
        student.incrementQuizAttempt(course.getSearchKey(), lesson.getSearchKey()); //elmougi
        double score = quiz.calculateScore(userAnswers);

        displayResults(score, userAnswers);

        boolean passed = score >= 50.0;
        if (passed) {
            student.markLessonCompleted(course.getSearchKey(), lesson.getSearchKey(), score);
            userService.updateRecord(student.getSearchKey(), student);
        }

        submitButton.setEnabled(false);
        disableAllRadioButtons();
        highlightCorrectAnswers(userAnswers);
    }

    private void displayResults(double score, List<Integer> userAnswers) {
        resultPanel.setVisible(true);

        int totalQuestions = quiz.getQuestions().size();
        int correctAnswers = (int) Math.round(score / 100.0 * totalQuestions);

        scoreLabel.setText(String.format("Your Score: %d/%d (%.1f%%)",
                correctAnswers, totalQuestions, score));

        if (score >= 50.0) {
            statusLabel.setText("PASSED");

        } else {
            statusLabel.setText("FAILED");
            statusLabel.setForeground(Color.RED);
        }

        JOptionPane.showMessageDialog(this,
                String.format("Quiz Score: %.1f%%\n%s",
                        score,
                        score >= 50.0 ? "You PASSED! Lesson is Completed! Mabrouk" : "You FAILED. M3lesh Try Again!"),
                "Quiz Results",
                score >= 50.0 ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
    }

    private void disableAllRadioButtons() {
        for (JRadioButton[] buttons : radioButtons) {
            for (JRadioButton button : buttons) {
                button.setEnabled(false);
            }
        }
    }

    private void highlightCorrectAnswers(List<Integer> userAnswers) {
        List<Question> questions = quiz.getQuestions();

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            int correctIndex = question.getAnswerIndex();
            int userAnswerIndex = userAnswers.get(i);

            JRadioButton[] buttons = radioButtons.get(i);


            buttons[correctIndex].setFont(new Font("Arial", Font.BOLD, 20));


        }
    }
}