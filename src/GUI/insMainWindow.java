package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import CourseManagement.*;
import java.util.*;
import Database.*;
import UserManagement.*;
import Utilities.Validation;
public class insMainWindow extends JFrame {
    private JPanel MainPanel;
    private JTabbedPane mainTaps;
    private JTabbedPane courseTabs;

    private JLabel welcomeLabel;
    private JLabel userLabel;
    private JButton logoutButton;

    private JTextField ContentField;
    private JTextField titleLessonfield;

    private JPanel AddLesson;
    private JTabbedPane EditLessonDe;
    private JPanel DeleteLesson;
    private JPanel CreateCourse;
    private JPanel EditCourse;
    private JPanel DeleteCourse;

    private JTextField CreateCourseID;
    private JTextField createCourseTitle;
    private JTextField CreateCourseD;
    private JButton createButton;
    private JButton editButtonCourse;
    private JTextField CourseIDField;
    private JButton addButtonLesson;
    private JTable table1;
    private JTextField LessonIDSe;
    private JTextField ContentEdited;
    private JTextField titleLessonEdited;
    private JButton searchButtonL;
    private JButton searchButtonC;

    private JButton EditButtonL;
    private JTextField DescriptionEdited;
    private JTextField titleCourseEdited;
    private JButton DeleteButtonC;
    private JTextField DeletedCourseID;
    private JTextField AddLessonID;
    private JTextField CourseID_AddLesson;
    private JTextField CourseLessonEdit;
    private JButton deleteButton;
    private JTextField CourseID_lessonDelete;
    private JTextField LessonID_Delete;
    private JTextField ResourcesField;
    private JButton addResources;
    private JComboBox ResourcesComboBox;
    private JButton deleteResouces;
    private JButton refreshTableButton;
    private JScrollPane tableScrollPane;


    private JComboBox<String> courseSelectComboBox;
    private JButton viewAnalyticsButton;


    private JTextField quizCourseIdField;
    private JTextField quizLessonIdField;
    private JButton searchQuizLessonButton;
    private JTextField questionTextField;
    private JTextField option1Field;
    private JTextField option2Field;
    private JTextField option3Field;
    private JTextField option4Field;
    private JComboBox<String> correctAnswerComboBox;
    private JButton addQuestionButton;
    private JList<String> questionsList;
    private DefaultListModel<String> questionsListModel;
    private JButton deleteQuestionButton;
    private JButton saveQuizButton;

    private Quiz currentQuiz;
    private List<Question> currentQuestions;

    private Instructor currentInstructor;
    private CourseService courseService;
    private UserService userService;

    public insMainWindow(Instructor instructor) {
        this.currentInstructor = instructor;
        this.courseService = new CourseService();
        this.userService = new UserService();
        this.currentQuestions = new ArrayList<>();

        setTitle("Instructor Dashboard - " + instructor.getName());
        setContentPane(MainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 750);
        setLocationRelativeTo(null);

        userLabel.setText("Welcome, " + instructor.getName() + " (ID: " + instructor.getSearchKey() + ")");

        initializeQuizComponents();
        setListeners();
        updateEnrolledStudentsTable();
        populateAnalyticsCourses();

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                saveAllData();
            }
        });

        setVisible(true);
    }

    private void initializeQuizComponents() {
        questionsListModel = new DefaultListModel<>();
        questionsList.setModel(questionsListModel);

        correctAnswerComboBox.addItem("Option 1");
        correctAnswerComboBox.addItem("Option 2");
        correctAnswerComboBox.addItem("Option 3");
        correctAnswerComboBox.addItem("Option 4");
    }

    private void setListeners() {
        logoutButton.addActionListener(e -> onLogout());

        refreshTableButton.addActionListener(e -> {
            updateEnrolledStudentsTable();
            JOptionPane.showMessageDialog(this, "Student list refreshed successfully!", "Refreshed", JOptionPane.INFORMATION_MESSAGE);
        });

        createButton.addActionListener(e -> {
            try {
                String courseId = CreateCourseID.getText();
                String title = createCourseTitle.getText();
                String description = CreateCourseD.getText();

                currentInstructor.createCourse(courseService, courseId, title, description, new ArrayList<>());

                JOptionPane.showMessageDialog(this, "Course created successfully!");
                CreateCourseID.setText("");
                createCourseTitle.setText("");
                CreateCourseD.setText("");
                updateEnrolledStudentsTable();
                populateAnalyticsCourses();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        searchButtonC.addActionListener(e -> {
            String id = CourseIDField.getText();
            Course course = courseService.getRecord(id);

            if (course == null) {
                JOptionPane.showMessageDialog(this, "Course not found!", "Not Found", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!course.getInstructorId().equals(currentInstructor.getSearchKey())) {
                JOptionPane.showMessageDialog(this, "You can only edit your own courses!", "Access Denied", JOptionPane.WARNING_MESSAGE);
                return;
            }

            titleCourseEdited.setText(course.getTitle());
            DescriptionEdited.setText(course.getDescription());
        });


        editButtonCourse.addActionListener(e -> {
            String courseId = CourseIDField.getText();
            String title = titleCourseEdited.getText();
            String description = DescriptionEdited.getText();

            try {
                Course course = courseService.getRecord(courseId);

                if (course == null) {
                    JOptionPane.showMessageDialog(this, "Course not found!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!course.getInstructorId().equals(currentInstructor.getSearchKey())) {
                    JOptionPane.showMessageDialog(this,
                            "You can only edit your own courses!",
                            "Access Denied", JOptionPane.WARNING_MESSAGE);
                    return;
                }


                if (Validation.isValidString(title)) course.setTitle(title);
                if (Validation.isValidString(description)) course.setDescription(description);

                courseService.updateRecord(courseId, course);

                JOptionPane.showMessageDialog(this,
                        "Course updated successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                titleCourseEdited.setText("");
                DescriptionEdited.setText("");
                updateEnrolledStudentsTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        DeleteButtonC.addActionListener(e -> {
            String id = DeletedCourseID.getText();
            Course course = courseService.getRecord(id);

            if (course == null) {
                JOptionPane.showMessageDialog(this, "Course not found!", "Not Found", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!course.getInstructorId().equals(currentInstructor.getSearchKey())) {
                JOptionPane.showMessageDialog(this, "You can only delete your own courses!", "Access Denied", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this course?\nThis will also remove all enrolled students from this course.",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                currentInstructor.deleteCourse(courseService, id);
                JOptionPane.showMessageDialog(this, "Course deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                DeletedCourseID.setText("");
                updateEnrolledStudentsTable();
                populateAnalyticsCourses();
            }
        });

        addButtonLesson.addActionListener(e -> {
            String courseId = CourseID_AddLesson.getText();
            String lessonId = AddLessonID.getText();
            String title = titleLessonfield.getText();
            String content = ContentField.getText();

            try {
                Course course = courseService.getRecord(courseId);
                if (course == null) {
                    JOptionPane.showMessageDialog(this, "Course not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!course.getInstructorId().equals(currentInstructor.getSearchKey())) {
                    JOptionPane.showMessageDialog(this, "You can only add lessons to your own courses!", "Access Denied", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                currentInstructor.addLesson(courseService, courseId, lessonId, title, content, new ArrayList<>());
                JOptionPane.showMessageDialog(this, "Lesson added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                CourseID_AddLesson.setText("");
                AddLessonID.setText("");
                titleLessonfield.setText("");
                ContentField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        searchButtonL.addActionListener(e -> {
            String courseId = CourseLessonEdit.getText();
            String lessonId = LessonIDSe.getText();

            Course course = courseService.getRecord(courseId);
            if (course == null) {
                JOptionPane.showMessageDialog(this, "Course not found!", "Not Found", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!course.getInstructorId().equals(currentInstructor.getSearchKey())) {
                JOptionPane.showMessageDialog(this, "You can only view lessons from your own courses!", "Access Denied", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Lesson lesson = course.searchLesson(lessonId);
            if (lesson == null) {
                JOptionPane.showMessageDialog(this, "Lesson not found in course!", "Not Found", JOptionPane.WARNING_MESSAGE);
                return;
            }

            titleLessonEdited.setText(lesson.getTitle());
            ContentEdited.setText(lesson.getContent());

            ResourcesComboBox.removeAllItems();
            for (String resource : lesson.getResources()) {
                ResourcesComboBox.addItem(resource);
            }
        });


        EditButtonL.addActionListener(e -> {
            String courseId = CourseLessonEdit.getText();
            String lessonId = LessonIDSe.getText();
            String title = titleLessonEdited.getText();
            String content = ContentEdited.getText();

            Course course = courseService.getRecord(courseId);
            if (course == null) {
                JOptionPane.showMessageDialog(this, "Course not found!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Lesson lesson = course.searchLesson(lessonId);
            if (lesson == null) {
                JOptionPane.showMessageDialog(this, "Lesson not found!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {

                if (Validation.isValidString(title)) lesson.setTitle(title);
                if (Validation.isValidString(content)) lesson.setContent(content);

                courseService.updateRecord(courseId, course);

                JOptionPane.showMessageDialog(this,
                        "Lesson updated successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        addResources.addActionListener(e -> {
            String courseId = CourseLessonEdit.getText();
            String lessonId = LessonIDSe.getText();
            String resource = ResourcesField.getText();

            if (resource.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Resource field cannot be empty!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Course course = courseService.getRecord(courseId);
            if (course == null) {
                JOptionPane.showMessageDialog(this, "Course not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Lesson lesson = course.searchLesson(lessonId);
            if (lesson == null) {
                JOptionPane.showMessageDialog(this, "Lesson not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            lesson.addResource(resource);
            courseService.updateRecord(courseId, course);
            ResourcesComboBox.addItem(resource);
            ResourcesField.setText("");
            JOptionPane.showMessageDialog(this, "Resource added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        deleteResouces.addActionListener(e -> {
            String courseId = CourseLessonEdit.getText();
            String lessonId = LessonIDSe.getText();
            Object selectedResource = ResourcesComboBox.getSelectedItem();

            if (selectedResource == null) {
                JOptionPane.showMessageDialog(this, "Please select a resource to delete!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Course course = courseService.getRecord(courseId);
            if (course == null) {
                JOptionPane.showMessageDialog(this, "Course not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Lesson lesson = course.searchLesson(lessonId);
            if (lesson == null) {
                JOptionPane.showMessageDialog(this, "Lesson not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            lesson.removeResource(selectedResource.toString());
            courseService.updateRecord(courseId, course);
            ResourcesComboBox.removeItem(selectedResource);
            JOptionPane.showMessageDialog(this, "Resource removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        deleteButton.addActionListener(e -> {
            String courseId = CourseID_lessonDelete.getText();
            String lessonId = LessonID_Delete.getText();

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this lesson?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    currentInstructor.deleteLesson(courseService, courseId, lessonId);
                    JOptionPane.showMessageDialog(this, "Lesson deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    CourseID_lessonDelete.setText("");
                    LessonID_Delete.setText("");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

       // to be implemented
        // viewAnalyticsButton.addActionListener(e -> onViewAnalytics());


        searchQuizLessonButton.addActionListener(e -> onSearchQuizLesson());
        addQuestionButton.addActionListener(e -> onAddQuestion());
        deleteQuestionButton.addActionListener(e -> onDeleteQuestion());
        saveQuizButton.addActionListener(e -> onSaveQuiz());
    }

    private void updateEnrolledStudentsTable() {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.addColumn("Course ID");
        model.addColumn("Course Title");
        model.addColumn("Description");
        model.addColumn("Student ID");
        model.addColumn("Student Name");
        model.addColumn("Student Email");

        ArrayList<Course> allCourses = courseService.returnAllRecords();
        for (Course course : allCourses) {
            if (course.getInstructorId().equals(currentInstructor.getSearchKey())) {
                List<Student> students = currentInstructor.enrolledStudents(courseService, userService, course.getSearchKey());

                if (students.isEmpty()) {
                    model.addRow(new Object[]{
                            course.getSearchKey(),
                            course.getTitle(),
                            course.getDescription().isEmpty() ? "N/A" : course.getDescription(),
                            "No students enrolled",
                            "-",
                            "-"
                    });
                } else {
                    for (Student student : students) {
                        model.addRow(new Object[]{
                                course.getSearchKey(),
                                course.getTitle(),
                                course.getDescription().isEmpty() ? "N/A" : course.getDescription(),
                                student.getSearchKey(),
                                student.getName(),
                                student.getEmail()
                        });
                    }
                }
            }
        }

        table1.setModel(model);

        if (table1.getColumnModel().getColumnCount() > 0) {
            table1.getColumnModel().getColumn(0).setPreferredWidth(80);
            table1.getColumnModel().getColumn(1).setPreferredWidth(150);
            table1.getColumnModel().getColumn(2).setPreferredWidth(200);
            table1.getColumnModel().getColumn(3).setPreferredWidth(80);
            table1.getColumnModel().getColumn(4).setPreferredWidth(120);
            table1.getColumnModel().getColumn(5).setPreferredWidth(150);
        }

        table1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    private void populateAnalyticsCourses() {
        courseSelectComboBox.removeAllItems();
        ArrayList<Course> allCourses = courseService.returnAllRecords();

        for (Course course : allCourses) {
            if (course.getInstructorId().equals(currentInstructor.getSearchKey())) {
                courseSelectComboBox.addItem(course.getTitle() + " (ID: " + course.getSearchKey() + ")");
            }
        }
    }

    /*
    to be implemented

    this will show the analytics for the selected course
    - bar chart for student progress
    - pie chart for quiz performance
    - list of top performing students

    private void onViewAnalytics() {
        String selectedCourse = (String) courseSelectComboBox.getSelectedItem();

        if (selectedCourse == null || selectedCourse.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please select a course to view analytics.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }




    }*/

    private void onLogout() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
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


    private void onSearchQuizLesson() {
        String courseId = quizCourseIdField.getText().trim();
        String lessonId = quizLessonIdField.getText().trim();

        if (!Validation.isValidString(courseId) || !Validation.isValidString(lessonId)) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both Course ID and Lesson ID.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE);
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

        if (!course.getInstructorId().equals(currentInstructor.getSearchKey())) {
            JOptionPane.showMessageDialog(this,
                    "You can only manage quizzes for your own courses!",
                    "Access Denied",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Lesson lesson = course.searchLesson(lessonId);
        if (lesson == null) {
            JOptionPane.showMessageDialog(this,
                    "Lesson not found in this course!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }


        currentQuiz = lesson.getQuiz();
        if (currentQuiz == null) {
            currentQuiz = new Quiz();
        }

        currentQuestions = new ArrayList<>(currentQuiz.getQuestions());
        updateQuestionsList();

        JOptionPane.showMessageDialog(this,
                "Lesson loaded successfully!\nLesson: " + lesson.getTitle() +
                        "\nExisting questions: " + currentQuestions.size(),
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void onAddQuestion() {
        String questionText = questionTextField.getText().trim();
        String option1 = option1Field.getText().trim();
        String option2 = option2Field.getText().trim();
        String option3 = option3Field.getText().trim();
        String option4 = option4Field.getText().trim();

        if (!Validation.isValidString(questionText) ||
                !Validation.isValidString(option1) ||
                !Validation.isValidString(option2) ||
                !Validation.isValidString(option3) ||
                !Validation.isValidString(option4)) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields (question and all 4 options).",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int correctAnswerIndex = correctAnswerComboBox.getSelectedIndex();
        if (correctAnswerIndex < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select the correct answer.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<String> options = new ArrayList<>();
        options.add(option1);
        options.add(option2);
        options.add(option3);
        options.add(option4);

        try {
            Question newQuestion = new Question(questionText, options, correctAnswerIndex);
            currentQuestions.add(newQuestion);
            updateQuestionsList();


            questionTextField.setText("");
            option1Field.setText("");
            option2Field.setText("");
            option3Field.setText("");
            option4Field.setText("");
            correctAnswerComboBox.setSelectedIndex(0);

            JOptionPane.showMessageDialog(this,
                    "Question added successfully!\nRemember to click 'Save Quiz' to save changes.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error adding question: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onDeleteQuestion() {
        int selectedIndex = questionsList.getSelectedIndex();

        if (selectedIndex < 0 || selectedIndex >= currentQuestions.size()) {
            JOptionPane.showMessageDialog(this,
                    "Please select a question to delete.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this question?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            currentQuestions.remove(selectedIndex);
            updateQuestionsList();
            JOptionPane.showMessageDialog(this,
                    "Question deleted!\nRemember to click 'Save Quiz' to save changes.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void onSaveQuiz() {
        String courseId = quizCourseIdField.getText().trim();
        String lessonId = quizLessonIdField.getText().trim();

        if (!Validation.isValidString(courseId) || !Validation.isValidString(lessonId)) {
            JOptionPane.showMessageDialog(this,
                    "Please search for a lesson first using the 'Search Lesson' button.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (currentQuestions.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Cannot save an empty quiz.",
                    "No Questions",
                    JOptionPane.WARNING_MESSAGE);
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

        Lesson lesson = course.searchLesson(lessonId);
        if (lesson == null) {
            JOptionPane.showMessageDialog(this,
                    "Lesson not found!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {

            Quiz newQuiz = new Quiz(new ArrayList<>(currentQuestions));


            lesson.getQuiz().setQuestions(new ArrayList<>(currentQuestions));


            courseService.updateRecord(courseId, course);

            JOptionPane.showMessageDialog(this,
                    "Quiz saved successfully!\n" +
                            "Total questions: " + currentQuestions.size(),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error saving quiz: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateQuestionsList() {
        questionsListModel.clear();

        if (currentQuestions.isEmpty()) {
            questionsListModel.addElement("No questions yet. Add questions above.");
        } else {
            for (int i = 0; i < currentQuestions.size(); i++) {
                Question q = currentQuestions.get(i);
                String correctOption = q.getOptions().get(q.getAnswerIndex());
                questionsListModel.addElement((i + 1) + ". " + q.getQuestionText() +
                        " [Correct: " + correctOption + "]");
            }
        }
    }
}