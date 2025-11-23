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
    private final InstructorAnalytics analytics;

    public Instructor(String name, String ID, String email, String password){
        super(name, ID, email, password);
        this.role = "Instructor";
        this.analytics = new InstructorAnalytics(ID);
    }

    public void createCourse(CourseService courseService, String courseId, String title, String description, List<Lesson> lessons){
        Course newCourse = new Course(courseId, title, description, this.getSearchKey(), lessons);
        courseService.insertRecord(newCourse);
        analytics.addCourseID(courseId, new ArrayList<>());
    }

    public void editCourseDetails(CourseService courseService, String courseId, String title, String description){
        Course existingCourse = courseService.getRecord(courseId);

        if (existingCourse == null) {
            throw new IllegalArgumentException("Course not found");
        }

        if (Validation.isValidString(title)) existingCourse.setTitle(title);
        if(Validation.isValidString(description)) existingCourse.setDescription(description);

        courseService.updateRecord(courseId, existingCourse);
    }

    public void deleteCourse(CourseService courseService, String courseId){
        analytics.removeCourseID(courseId);
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

        Lesson updatedLesson = new Lesson(lessonID, title, content, resources);

        existingCourse.editLesson(lessonID, updatedLesson);

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
        analytics.updateStudents(courseId, enrolledStudents);

        return Collections.unmodifiableList(enrolledStudents);
    }

    public List<String> getCoursesID(){
        return analytics.getCoursesID();
    }

    public void addCourseID(String courseId){
        analytics.addCourseID(courseId);
    }

    public void addCourseID(String courseId, List<Student> students){
        analytics.addCourseID(courseId, students);
    }

    public List<Student> getStudents(String courseId) {
        return analytics.getStudents(courseId);
    }
}

//elmougi sends his regrads changed and made sure that the course id and lesson id are not changed when editing items