package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import CourseManagement.*;
import java.util.*;
import Database.*;
import UserManagement.*;

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
    private JTextField IDLessonEdited;
    private JTextField ContentEdited;
    private JTextField titleLessonEdited;
    private JButton searchButtonL;
    private JButton searchButtonC;

    private JButton EditButtonL;
    private JTextField DescriptionEdited;
    private JTextField titleCourseEdited;
    private JTextField CourseIDEdited;
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

    private Instructor currentInstructor;
    private CourseService courseService;
    private UserService userService;

    public insMainWindow(Instructor instructor) {
        this.currentInstructor = instructor;
        this.courseService = new CourseService();
        this.userService = new UserService();

        setTitle("Instructor Dashboard - " + instructor.getName());
        setContentPane(MainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 750);
        setLocationRelativeTo(null);


        userLabel.setText("Welcome, " + instructor.getName() + " (ID: " + instructor.getSearchKey() + ")");

        setListeners();
        updateEnrolledStudentsTable();


        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                saveAllData();
            }
        });

        setVisible(true);
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

            CourseIDEdited.setText(course.getSearchKey());
            titleCourseEdited.setText(course.getTitle());
            DescriptionEdited.setText(course.getDescription());
        });


        editButtonCourse.addActionListener(e -> {
            String oldId = CourseIDField.getText();
            String title = titleCourseEdited.getText();
            String description = DescriptionEdited.getText();

            try {
                currentInstructor.editCourseDetails(courseService, oldId, title, description);
                JOptionPane.showMessageDialog(this, "Course updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                CourseIDField.setText("");
                CourseIDEdited.setText("");
                titleCourseEdited.setText("");
                DescriptionEdited.setText("");
                updateEnrolledStudentsTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

            IDLessonEdited.setText(lesson.getSearchKey());
            titleLessonEdited.setText(lesson.getTitle());
            ContentEdited.setText(lesson.getContent());

            ResourcesComboBox.removeAllItems();
            for (String resource : lesson.getResources()) {
                ResourcesComboBox.addItem(resource);
            }
        });


        EditButtonL.addActionListener(e -> {
            String courseId = CourseLessonEdit.getText();
            String oldLessonId = LessonIDSe.getText();
            String title = titleLessonEdited.getText();
            String content = ContentEdited.getText();

            Course course = courseService.getRecord(courseId);
            if (course == null) {
                JOptionPane.showMessageDialog(this, "Course not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Lesson lesson = course.searchLesson(oldLessonId);
            if (lesson == null) {
                JOptionPane.showMessageDialog(this, "Lesson not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                currentInstructor.editLesson(courseService, courseId, oldLessonId, title, content, lesson.getResources());
                JOptionPane.showMessageDialog(this, "Lesson updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
            table1.getColumnModel().getColumn(0).setPreferredWidth(80);  // Course ID
            table1.getColumnModel().getColumn(1).setPreferredWidth(150); // Course Title
            table1.getColumnModel().getColumn(2).setPreferredWidth(200); // Description
            table1.getColumnModel().getColumn(3).setPreferredWidth(80);  // Student ID
            table1.getColumnModel().getColumn(4).setPreferredWidth(120); // Student Name
            table1.getColumnModel().getColumn(5).setPreferredWidth(150); // Student Email
        }

        table1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    private void onLogout() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?\nAll changes have been saved.",
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
}