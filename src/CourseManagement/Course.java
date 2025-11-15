package CourseManagement;

import Users.Student;
import database.DataInfo;

public class Course implements DataInfo {

    private String courseId;
    private String title;
    private String description;
    private String instructorId;
    private Lesson[] lessons;
    private Student[] students;

    public Course(String courseId, String title, String description, String instructorId) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.instructorId = instructorId;
    }

    @Override
    public String getSearchKey() { return courseId; }


    public void setCourseId(String courseId) { this.courseId = courseId; }

    public void setTitle(String title) { this.title = title; }

    public void setDescription(String description) { this.description = description; }

    public void setInstructorId(String instructorId) { this.instructorId = instructorId; }

    public String getTitle() { return title; }

    public String getDescription() { return description; }

    public String getInstructorId() { return instructorId; }
}