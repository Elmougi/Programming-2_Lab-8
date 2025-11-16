package GUI;

import javax.swing.*;
import CourseManagement.*;
import java.util.*;
import Database.*;
import com.sun.jdi.PathSearchingVirtualMachine;

public class insMainWindow extends JFrame {
    private JPanel MainPanel;
    private JTabbedPane mainTaps;
    private JTabbedPane courseTabs;

    private JTextField IDCouresField;
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

    public insMainWindow() {
        setTitle("Instructor Dashboard");
        setContentPane(MainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setListners() {
        //Create course
        createButton.addActionListener(e -> {
            try {
                String courseId = CreateCourseID.getText();
                String title = createCourseTitle.getText();
                String description = CreateCourseD.getText();
                String instructorId = "INS001"; // replace with actual logged instructor ID

                Course newCourse = new Course(
                        courseId,
                        title,
                        description,
                        instructorId,
                        new ArrayList<>()
                );

                CourseService cs = new CourseService();
                cs.insertRecord(newCourse);
                JOptionPane.showMessageDialog(this, "Course created!");


            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        // EDIT COURSE
        searchButtonC.addActionListener(e -> {
            String id = CourseIDField.getText();
            CourseService cs = new CourseService();

            Course course = cs.getRecord(id);
            if (course == null) {
                JOptionPane.showMessageDialog(this, "Course not found!");
                return;
            }

            CourseIDEdited.setText(course.getSearchKey());
            titleCourseEdited.setText(course.getTitle());
            DescriptionEdited.setText(course.getDescription());

            editButtonCourse.addActionListener(e1 -> {
                course.setTitle(titleCourseEdited.getText());
                course.setDescription(DescriptionEdited.getText());
                course.setCourseId(CourseIDEdited.getText());
                cs.updateRecord(course.getSearchKey(), course);
                JOptionPane.showMessageDialog(this, "Course updated!");

            });
        });

        // DELETE COURSE
        DeleteButtonC.addActionListener(e -> {
            String id = DeletedCourseID.getText();
            CourseService cs = new CourseService();
            Course course = cs.getRecord(id);
            if (course == null) {
                JOptionPane.showMessageDialog(this, "Course not found!");
                return;
            }
            cs.deleteRecord(id);
            JOptionPane.showMessageDialog(this, "Course deleted!");


        });

        // ADD LESSON
        addButtonLesson.addActionListener(e -> {
            String id = CourseID_AddLesson.getText();
            String LessonID = AddLessonID.getText();
            String title = titleLessonfield.getText();
            String content = ContentField.getText();
            CourseService cs = new CourseService();
            Course course = cs.getRecord(id);
            if (course == null) {
                JOptionPane.showMessageDialog(this, "Course not found!");
                return;
            } else if (course.getLessons().contains(getLesson(course, LessonID))) {
                JOptionPane.showMessageDialog(this, "Lesson Already found!");
                return;
            }
            Lesson lesson = new Lesson(LessonID, title, content, new ArrayList<>());
            course.getLessons().add(lesson);

            cs.updateRecord(id, course);
            JOptionPane.showMessageDialog(this, "Lesson added Successfully!");


        });
        //Edit Lesson
        searchButtonL.addActionListener(e -> {
            String id = CourseLessonEdit.getText();
            String lessonID = LessonIDSe.getText();
            CourseService cs = new CourseService();
            Course course = cs.getRecord(id);
            if (course == null) {
                JOptionPane.showMessageDialog(this, "Course not found!");
                return;
            } else if (!course.getLessons().contains(getLesson(course, lessonID))) {
                JOptionPane.showMessageDialog(this, "Lesson not found in Course!");
                return;
            }
            Lesson l=getLesson(course, lessonID);
            IDLessonEdited.setText((l.getSearchKey());
            titleLessonEdited.setText(l.getTitle());
            ContentEdited.setText(l.getContent());

            List <String> items = l.getResources();
            for (String s : items) {
                ResourcesComboBox.addItem(s);
            }
            EditButtonL.addActionListener(e1 -> {
                l.setLessonID(IDLessonEdited.getText());
                l.setTitle(titleLessonEdited.getText());
                l.setContent(ContentEdited.getText());
                JOptionPane.showMessageDialog(this, "Lesson updated Successfully!");

            });
            //Edit Resources
            addResources.addActionListener(e1 -> {
                l.addResource(ResourcesField.getText());
                ResourcesField.setText("");
            });
            deleteResouces.addActionListener(e1 -> {
                items = l.getResources();
                for (String s : items) {
                    ResourcesComboBox.addItem(s);
                }
                l.removeResource(ResourcesComboBox.getSelectedItem().toString());
                JOptionPane.showMessageDialog(this, "Resource removed successfully!");

            });

        });
        // Delete Lesson
        deleteButton.addActionListener(e -> {
            String id = CourseID_lessonDelete.getText();
            String lessonID = LessonID_Delete.getText();
            CourseService cs = new CourseService();
            Course course = cs.getRecord(id);
            if (course == null) {
                JOptionPane.showMessageDialog(this, "Course not found!");
                return;
            } else if (!course.getLessons().contains(lessonID)) {
                JOptionPane.showMessageDialog(this, "Lesson not found in Course!");
                return;
            }
            for (Lesson lesson : course.getLessons()) {
                if (lesson.getSearchKey().equals(LessonID_Delete.getText())) {
                    course.getLessons().remove(lesson);
                    cs.updateRecord(id, course);
                    JOptionPane.showMessageDialog(this, "Lesson deleted Successfully!");
                    break;
                }
            }
        });
    }

    private Lesson getLesson(Course course, String lessonId) {
        for (Lesson l : course.getLessons()) {
            if (l.getSearchKey().equals(lessonId)) {
                return l;
            }
        }
        return null;
    }

}

