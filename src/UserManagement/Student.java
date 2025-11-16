package UserManagement;

import CourseManagement.Course;
import CourseManagement.Lesson;
import Database.CourseService;
import java.util.*;

public class Student extends User{
    public Student(String name, String ID, String email, String password){
        super(name, ID, email, password);
        this.role = "Student";
    }

    public void enrollInCourse(CourseService courseService, String CourseID, Student student) {
        Course course = courseService.getRecord(CourseID);
        if (course != null) {
            course.addStudent(student);
        } else {
            throw new IllegalArgumentException("Invalid Course ID");
        }
    }

    public void dropCourse(CourseService courseService, String CourseID, Student student) {
        Course course = courseService.getRecord(CourseID);
        if (course != null) {
            course.removeStudent(student.getSearchKey());
        } else {
            throw new IllegalArgumentException("Invalid Course ID");
        }
    }

    public List<Lesson> getAllLessons(CourseService courseService, String CourseID) {
        Course course = courseService.getRecord(CourseID);
        if (course != null) {
            return Collections.unmodifiableList(course.getLessons());
        } else {
            throw new IllegalArgumentException("Invalid Course ID");
        }
    }

    public Lesson getLesson(CourseService courseService, String CourseID, String lessonID) {
        Course course = courseService.getRecord(CourseID);
        if (course != null) {
            return course.searchLesson(lessonID);
        } else {
            throw new IllegalArgumentException("Invalid Course ID");
        }
    }
}
