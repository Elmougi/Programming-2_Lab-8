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
    private List<String> coursesID = new ArrayList<>();

    public Instructor(String name, String ID, String email, String password){
        super(name, ID, email, password);
        this.role = "Instructor";
    }

    public void createCourse(CourseService courseService, String courseId, String title, String description, List<Lesson> lessons){
        Course newCourse = new Course(courseId, title, description, this.getSearchKey(), lessons);
        coursesID.add(courseId);
        courseService.insertRecord(newCourse);
    }

    public void editCourseDetails(CourseService courseService, UserService userService, String oldCourseId, String newCourseId, String title, String description){
        Course existingCourse = courseService.getRecord(oldCourseId);

        if (existingCourse == null) {
            throw new IllegalArgumentException("Course not found");
        }

        boolean courseIdChanged = !oldCourseId.equals(newCourseId) && Validation.isValidString(newCourseId);


        if(courseIdChanged) {

            if (courseService.contains(newCourseId)) {
                throw new IllegalArgumentException("Course ID '" + newCourseId + "' already exists");
            }
            existingCourse.setCourseId(newCourseId);
        }
        if (Validation.isValidString(title)) existingCourse.setTitle(title);
        if(Validation.isValidString(description)) existingCourse.setDescription(description);


        if (courseIdChanged) {
            List<User> allUsers = userService.returnAllRecords();
            for (User user : allUsers) {
                if (user instanceof Student) {
                    Student student = (Student) user;
                    student.updateCourseId(oldCourseId, newCourseId);
                    userService.updateRecord(student.getSearchKey(), student);
                }
            }


            courseService.deleteRecord(oldCourseId);
            courseService.insertRecord(existingCourse);
        } else {

            courseService.updateRecord(oldCourseId, existingCourse);
        }
    }

    public void deleteCourse(CourseService courseService, String courseId){
        coursesID.remove(courseId);
        courseService.deleteRecord(courseId);
    }

    public void addLesson(CourseService courseService, String courseId, String lessonID, String title, String content, List<String> resources){
        Course existingCourse = courseService.getRecord(courseId);
        String key = existingCourse.getSearchKey();

        existingCourse.addLesson(new Lesson(lessonID, title, content, resources));
        courseService.updateRecord(key, existingCourse);
    }

    public void editLesson(CourseService courseService, UserService userService, String courseId, String oldLessonID, String newLessonID, String title, String content, List<String> resources){
        Course existingCourse = courseService.getRecord(courseId);
        String key = existingCourse.getSearchKey();

        boolean lessonIdChanged = !oldLessonID.equals(newLessonID) && Validation.isValidString(newLessonID);


        Lesson updatedLesson = new Lesson(
                Validation.isValidString(newLessonID) ? newLessonID : oldLessonID,
                title,
                content,
                resources
        );


        existingCourse.editLesson(oldLessonID, updatedLesson);


        if (lessonIdChanged) {
            List<User> allUsers = userService.returnAllRecords();
            for (User user : allUsers) {
                if (user instanceof Student) {
                    Student student = (Student) user;

                    student.updateLessonId(courseId, oldLessonID, newLessonID);

                    userService.updateRecord(student.getSearchKey(), student);
                }
            }
        }

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


    // needs to be moved to Course
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

    public List<String> getCoursesID(){
        return Collections.unmodifiableList(coursesID);
    }

    public void addCourseID(String courseId){
        coursesID.add(courseId);
    }
}