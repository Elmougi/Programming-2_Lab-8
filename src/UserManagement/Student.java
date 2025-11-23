package UserManagement;

import CourseManagement.Course;
import CourseManagement.Lesson;
import Database.CourseService;

import java.util.*;

public class Student extends User{
    private List<Certificate> certificates = new ArrayList<>();
    private Map<String, CourseProgress> progress = new HashMap<>();


    public Student(String name, String ID, String email, String password){
        super(name, ID, email, password);
        this.role = "Student";
    }

    public void enrollInCourse(CourseService courseService, String CourseID, Student student) {
        Course course = courseService.getRecord(CourseID);
        if (course != null) {
            course.addStudent(student);
            progress.put(CourseID, new CourseProgress(course));
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

    private Lesson getLesson(CourseService courseService, String CourseID, String lessonID) {
        Course course = courseService.getRecord(CourseID);
        if (course != null) {
            return course.searchLesson(lessonID);
        } else {
            throw new IllegalArgumentException("Invalid Course ID");
        }
    }

    public List<Certificate> getCertificates() {
        return Collections.unmodifiableList(certificates);
    }

    public boolean isLessonCompleted(String courseID, String lessonID) {
        if(progress.containsKey(courseID)) {
            CourseProgress courseProgress = progress.get(courseID);
            return courseProgress.isLessonCompleted(lessonID);
        }
        return false;
    }

    public void addCertificate(Certificate certificate) {
        if (certificate != null) {
            certificates.add(certificate);
        }
    }

    public boolean isCourseCompleted(String courseID) {
        if(progress.containsKey(courseID)) {
            CourseProgress courseProgress = progress.get(courseID);
            return courseProgress.isCourseCompleted();
        }

        return false;
    }

    public boolean markLessonCompleted(String courseID, String lessonID, double quizScore) {
        if(!progress.containsKey(courseID)) {
            throw new IllegalArgumentException("Invalid Course ID");
        }

        if(quizScore < 50.0 || quizScore > 100.0) {
            return false;
        }

        progress.get(courseID).markLessonProgressCompleted(lessonID, quizScore);
        if(isCourseCompleted(courseID)) {
            addCertificate(new Certificate(this.ID + "_" + this.certificates.size(), courseID, this.ID));    }
        return true;
    }

    public List<Lesson> getCompletedLessons(CourseService courseService, String courseID) {
        List<Lesson> completedLessons = new ArrayList<>();

        if(progress.containsKey(courseID)) {
            CourseProgress courseProgress = progress.get(courseID);
            List<String> completedLessonsID = courseProgress.getCompletedLessons();
            for (String lessonID : completedLessonsID) {
                Lesson lesson = getLesson(courseService, courseID, lessonID);
                if (lesson != null) {
                    completedLessons.add(lesson);
                }
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

    private void updateCourse(Course course, String courseID) {
        if(!progress.containsKey(courseID)) {
            return;
        }

        progress.get(courseID).updateCourseProgress(course);

    }

    public Map<String, Map<String, Double>> getProgress() {
        Map<String, Map<String, Double>> res = new HashMap<>();
        for (String courseID : progress.keySet()) {
            CourseProgress courseProgress = progress.get(courseID);
            res.put(courseID, courseProgress.getCourseProgress());
        }
        return res;
    }

    public CourseProgress getCourseProgress(String courseID) {
        return progress.get(courseID);
    }

    public void setProgress(Map<String, Map<String, Double>> progress) {
        for (String courseID : progress.keySet()) {
            Map<String, Double> lessonProgress = progress.get(courseID);
            CourseProgress courseProgress = new CourseProgress(lessonProgress);

            this.progress.put(courseID, courseProgress);
        }
    }


    // elmougi sends his regards
    public void incrementQuizAttempt(String courseID, String lessonID) {
        if(!progress.containsKey(courseID)) {
            throw new IllegalArgumentException("Invalid Course ID");
        }
        progress.get(courseID).incrementQuizAttempt(lessonID);
    }

    public int getTotalAttemptOfQuiz(String courseID, String lessonID) {
        if(!progress.containsKey(courseID)) {
            return 0;
        }
        return progress.get(courseID).getTotalAttempts(lessonID);
    }


}
