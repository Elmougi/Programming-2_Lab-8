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
        setSize(800, 600);
        setLocationRelativeTo(null);

        setListeners();
        viewAllCourses();

        setVisible(true);
    }

    private void setListeners() {

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
                viewAllCourses();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });


        searchButtonC.addActionListener(e -> {
            String id = CourseIDField.getText();
            Course course = courseService.getRecord(id);

            if (course == null) {
                JOptionPane.showMessageDialog(this, "Course not found!");
                return;
            }

            if (!course.getInstructorId().equals(currentInstructor.getSearchKey())) {
                JOptionPane.showMessageDialog(this, "You can only edit your own courses!");
                return;
            }

            CourseIDEdited.setText(course.getSearchKey());
            titleCourseEdited.setText(course.getTitle());
            DescriptionEdited.setText(course.getDescription());
        });


        editButtonCourse.addActionListener(e -> {
            String oldId = CourseIDField.getText();
            String newId = CourseIDEdited.getText();
            String title = titleCourseEdited.getText();
            String description = DescriptionEdited.getText();

            try {
                currentInstructor.editCourseDetails(courseService, oldId, title, description);
                JOptionPane.showMessageDialog(this, "Course updated successfully!");
                CourseIDField.setText("");
                CourseIDEdited.setText("");
                titleCourseEdited.setText("");
                DescriptionEdited.setText("");
                viewAllCourses();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });


        DeleteButtonC.addActionListener(e -> {
            String id = DeletedCourseID.getText();
            Course course = courseService.getRecord(id);

            if (course == null) {
                JOptionPane.showMessageDialog(this, "Course not found!");
                return;
            }

            if (!course.getInstructorId().equals(currentInstructor.getSearchKey())) {
                JOptionPane.showMessageDialog(this, "You can only delete your own courses!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this course?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                currentInstructor.deleteCourse(courseService, id);
                JOptionPane.showMessageDialog(this, "Course deleted successfully!");
                DeletedCourseID.setText("");
                viewAllCourses();
            }
        });


        addButtonLesson.addActionListener(e -> {
            String courseId = CourseID_AddLesson.getText();
            String lessonId = AddLessonID.getText();
            String title = titleLessonfield.getText();
            String content = ContentField.getText();

            try {
                currentInstructor.addLesson(courseService, courseId, lessonId, title, content, new ArrayList<>());
                JOptionPane.showMessageDialog(this, "Lesson added successfully!");
                CourseID_AddLesson.setText("");
                AddLessonID.setText("");
                titleLessonfield.setText("");
                ContentField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });


        searchButtonL.addActionListener(e -> {
            String courseId = CourseLessonEdit.getText();
            String lessonId = LessonIDSe.getText();

            Course course = courseService.getRecord(courseId);
            if (course == null) {
                JOptionPane.showMessageDialog(this, "Course not found!");
                return;
            }

            Lesson lesson = course.searchLesson(lessonId);
            if (lesson == null) {
                JOptionPane.showMessageDialog(this, "Lesson not found in course!");
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
            String newLessonId = IDLessonEdited.getText();
            String title = titleLessonEdited.getText();
            String content = ContentEdited.getText();

            Course course = courseService.getRecord(courseId);
            if (course == null) {
                JOptionPane.showMessageDialog(this, "Course not found!");
                return;
            }

            Lesson lesson = course.searchLesson(oldLessonId);
            if (lesson == null) {
                JOptionPane.showMessageDialog(this, "Lesson not found!");
                return;
            }

            try {
                currentInstructor.editLesson(courseService, courseId, oldLessonId, title, content, lesson.getResources());
                JOptionPane.showMessageDialog(this, "Lesson updated successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });


        addResources.addActionListener(e -> {
            String courseId = CourseLessonEdit.getText();
            String lessonId = LessonIDSe.getText();
            String resource = ResourcesField.getText();

            Course course = courseService.getRecord(courseId);
            if (course == null) {
                JOptionPane.showMessageDialog(this, "Course not found!");
                return;
            }

            Lesson lesson = course.searchLesson(lessonId);
            if (lesson == null) {
                JOptionPane.showMessageDialog(this, "Lesson not found!");
                return;
            }

            lesson.addResource(resource);
            courseService.updateRecord(courseId, course);
            ResourcesComboBox.addItem(resource);
            ResourcesField.setText("");
            JOptionPane.showMessageDialog(this, "Resource added successfully!");
        });


        deleteResouces.addActionListener(e -> {
            String courseId = CourseLessonEdit.getText();
            String lessonId = LessonIDSe.getText();
            Object selectedResource = ResourcesComboBox.getSelectedItem();

            if (selectedResource == null) {
                JOptionPane.showMessageDialog(this, "Please select a resource to delete!");
                return;
            }

            Course course = courseService.getRecord(courseId);
            if (course == null) {
                JOptionPane.showMessageDialog(this, "Course not found!");
                return;
            }

            Lesson lesson = course.searchLesson(lessonId);
            if (lesson == null) {
                JOptionPane.showMessageDialog(this, "Lesson not found!");
                return;
            }

            lesson.removeResource(selectedResource.toString());
            courseService.updateRecord(courseId, course);
            ResourcesComboBox.removeItem(selectedResource);
            JOptionPane.showMessageDialog(this, "Resource removed successfully!");
        });


        deleteButton.addActionListener(e -> {
            String courseId = CourseID_lessonDelete.getText();
            String lessonId = LessonID_Delete.getText();

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this lesson?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    currentInstructor.deleteLesson(courseService, courseId, lessonId);
                    JOptionPane.showMessageDialog(this, "Lesson deleted successfully!");
                    CourseID_lessonDelete.setText("");
                    LessonID_Delete.setText("");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });
    }

    private void viewAllCourses() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Course ID");
        model.addColumn("Title");
        model.addColumn("Description");
        model.addColumn("Instructor ID");
        model.addColumn("Enrolled Students");

        ArrayList<Course> allCourses = courseService.returnAllRecords();
        for (Course course : allCourses) {
            if (course.getInstructorId().equals(currentInstructor.getSearchKey())) {
                model.addRow(new Object[]{
                        course.getSearchKey(),
                        course.getTitle(),
                        course.getDescription(),
                        course.getInstructorId(),
                        course.getStudents().size()
                });
            }
        }

        table1.setModel(model);
    }
}