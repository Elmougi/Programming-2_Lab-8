package UserManagement;

import CourseManagement.Course;
import CourseManagement.Lesson;
import Database.CourseService;
import java.util.*;

public class Student extends User{
    private Map<String, Map<String, Boolean>> progress = new HashMap<>();
    // 2l5olasa: Map<course id, Map<lesson id, is lesson completed?>>

    public Student(String name, String ID, String email, String password){
        super(name, ID, email, password);
        this.role = "Student";
    }

    public void enrollInCourse(CourseService courseService, String CourseID, Student student) {
        Course course = courseService.getRecord(CourseID);
        if (course != null) {
            course.addStudent(student);
            Map<String, Boolean> lessonProgress = new HashMap<>();
            for (Lesson lesson : course.getLessons()) {
                lessonProgress.put(lesson.getSearchKey(), false);
            }
            progress.put(CourseID, lessonProgress);
        } else {
            throw new IllegalArgumentException("Invalid Course ID");
        }
    }

    public void dropCourse(CourseService courseService, String CourseID, Student student) {
        Course course = courseService.getRecord(CourseID);
        if (course != null) {
            course.removeStudent(student.getSearchKey());
            progress.remove(CourseID);
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

    public boolean isLessonCompleted(String courseID, String lessonID) {
        if (progress.containsKey(courseID)) {
            Map<String, Boolean> lessonProgress = progress.get(courseID);
            if(lessonProgress.containsKey(lessonID)) {
                return true;
            }
        }
        return false;
    }

    public void markLessonCompleted(String courseID, String lessonID) {
        if (progress.containsKey(courseID)) {
            Map<String, Boolean> lessonProgress = progress.get(courseID);
            if(lessonProgress.containsKey(lessonID)) {
                lessonProgress.put(lessonID, true);
            } else {
                throw new IllegalArgumentException("Invalid Lesson ID");
            }
        } else {
            throw new IllegalArgumentException("Invalid Course ID");
        }
    }

    public List<Lesson> getCompletedLessons(CourseService courseService, String courseID) {
        List<Lesson> completedLessons = new ArrayList<>();
        if (progress.containsKey(courseID)) {
            Map<String, Boolean> lessonProgress = progress.get(courseID);
            Course course = courseService.getRecord(courseID);
            if (course != null) {
                for (Lesson lesson : course.getLessons()) {
                    if (lessonProgress.getOrDefault(lesson.getSearchKey(), false)) {
                        completedLessons.add(lesson);
                    }
                }
            } else {
                throw new IllegalArgumentException("Invalid Course ID");
            }
        } else {
            throw new IllegalArgumentException("Invalid Course ID");
        }
        return completedLessons;
    }


}
