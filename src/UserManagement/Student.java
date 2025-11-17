package UserManagement;

import CourseManagement.Course;
import CourseManagement.Lesson;
import Database.CourseService;

import java.util.*;

public class Student extends User{
    private Map<String, Map<String, Boolean>> progress = new HashMap<>();

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

            UpdateCourse(course, CourseID);
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
            return lessonProgress.getOrDefault(lessonID, false);
        }
        return false;
    }

    public void markLessonCompleted(String courseID, String lessonID) {
        if (progress.containsKey(courseID)) {
            Map<String, Boolean> lessonProgress = progress.get(courseID);

            if(!lessonProgress.containsKey(lessonID)) {
                lessonProgress.put(lessonID, true);
            } else {
                lessonProgress.put(lessonID, true);
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

                UpdateCourse(course, courseID);
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

    public List<Course> getEnrolledCourses(CourseService courseService) {
        List<Course> enrolledCourses = new ArrayList<>();
        for (String courseID : progress.keySet()) {
            Course course = courseService.getRecord(courseID);
            if (course != null) {
                enrolledCourses.add(course);
            }
        }
        return enrolledCourses;
    }


    private void UpdateCourse(Course course, String courseID) {
        if (!progress.containsKey(courseID)) {
            return;
        }

        Map<String, Boolean> lessonProgress = progress.get(courseID);
        Map<String, Boolean> newProgress = new HashMap<>();


        for (Lesson lesson : course.getLessons()) {
            String lessonID = lesson.getSearchKey();

            newProgress.put(lessonID, lessonProgress.getOrDefault(lessonID, false));
        }


        progress.put(courseID, newProgress);
    }


    public void updateLessonId(String courseID, String oldLessonID, String newLessonID) {
        if (progress.containsKey(courseID)) {
            Map<String, Boolean> lessonProgress = progress.get(courseID);
            if (lessonProgress.containsKey(oldLessonID)) {

                Boolean completionStatus = lessonProgress.get(oldLessonID);
                lessonProgress.remove(oldLessonID);
                lessonProgress.put(newLessonID, completionStatus);
            }
        }
    }


    public void updateCourseId(String oldCourseID, String newCourseID) {
        if (progress.containsKey(oldCourseID)) {

            Map<String, Boolean> lessonProgress = progress.get(oldCourseID);
            progress.remove(oldCourseID);
            progress.put(newCourseID, lessonProgress);
        }
    }

    public Map<String, Map<String, Boolean>> getProgress() {
        return progress;
    }

    public void setProgress(Map<String, Map<String, Boolean>> progress) {
        this.progress = progress;
    }
}