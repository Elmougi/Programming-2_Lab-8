package UserManagement;

import CourseManagement.Lesson;
import Utilities.Hashing;
import CourseManagement.Course;
import Utilities.Validation;
import Database.CourseService;
import Database.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Instructor extends User {
    public Instructor(String name, String ID, String email, String password){
        super(name, ID, email, password);
        this.role = "Instructor";
    }

    public void createCourse(CourseService courseService, String courseId, String title, String description, List<Lesson> lessons){
        Course newCourse = new Course(courseId, title, description, this.getSearchKey(), lessons);
        courseService.insertRecord(newCourse);
    }

    public void editCourseDetails(CourseService courseService, String courseId, String title, String description){
        Course existingCourse = courseService.getRecord(courseId);
        String key = existingCourse.getSearchKey();

        if(Validation.isValidString(courseId)) existingCourse.setCourseId(courseId);
        if (Validation.isValidString(title)) existingCourse.setTitle(title);
        if(Validation.isValidString(description)) existingCourse.setDescription(description);

        courseService.updateRecord(key, existingCourse);
    }

    public void deleteCourse(CourseService courseService, String courseId){
        courseService.deleteRecord(courseId);
    }

    public void addLesson(CourseService courseService, String courseId, String lessonID, String title, String content, List<String> resources){
        Course existingCourse = courseService.getRecord(courseId);
        String key = existingCourse.getSearchKey();

        existingCourse.addLesson(new Lesson(lessonID, title, content, resources));
        courseService.updateRecord(key, existingCourse);
    }

    public void editLesson(CourseService courseService, String courseId, String lessonID, String title, String content, List<String> resources){
        Course existingCourse = courseService.getRecord(courseId);
        String key = existingCourse.getSearchKey();

        existingCourse.editLesson(lessonID, new Lesson(lessonID, title, content, resources));

        courseService.updateRecord(key, existingCourse);
    }

    public void deleteLesson(CourseService courseService, String courseId, String lessonID){
        Course existingCourse = courseService.getRecord(courseId);
        String key = existingCourse.getSearchKey();

        existingCourse.removeLesson(lessonID);
        courseService.updateRecord(key, existingCourse);
    }



    public List<Student> enrolledStudents(CourseService courseService, UserService userService, String courseId){
        Course existingCourse = courseService.getRecord(courseId);

        if (existingCourse == null) {
            return Collections.emptyList();
        }


        List<Student> enrolledStudents = new ArrayList<>();
        List<User> allUsers = userService.returnAllRecords();

        for (User user : allUsers) {
            if (user instanceof Student) {
                Student student = (Student) user;

                if (student.getProgress().containsKey(courseId)) {
                    enrolledStudents.add(student);
                }
            }
        }


        existingCourse.getStudents().clear();


        for (Student student : enrolledStudents) {
            existingCourse.addStudent(student);
        }


        courseService.updateRecord(courseId, existingCourse);

        return Collections.unmodifiableList(enrolledStudents);
    }
}

