package UserManagement;

import CourseManagement.Course;
import CourseManagement.Lesson;
import Database.CourseService;

import java.util.*;

public class Student extends User{
//    private Map<String, Map<String, Boolean>> progress = new HashMap<>(); // <CourseID, <LessonID, isCompleted>>
    private List<Certificate> certificates = new ArrayList<>();
    private Map<String, CourseProgress> progress2 = new HashMap<>();

    public Student(String name, String ID, String email, String password){
        super(name, ID, email, password);
        this.role = "Student";
    }

    public void enrollInCourse(CourseService courseService, String CourseID, Student student) {
        Course course = courseService.getRecord(CourseID);
        if (course != null) {
            course.addStudent(student);
            progress2.put(CourseID, new CourseProgress(course));

//            Map<String, Boolean> lessonProgress = new HashMap<>();
//            for (Lesson lesson : course.getLessons()) {
//                lessonProgress.put(lesson.getSearchKey(), false);
//            }
//            progress.put(CourseID, lessonProgress);

        } else {
            throw new IllegalArgumentException("Invalid Course ID");
        }
    }

    public void dropCourse(CourseService courseService, String CourseID, Student student) {
        Course course = courseService.getRecord(CourseID);
        if (course != null) {
            course.removeStudent(student.getSearchKey());
            progress2.remove(CourseID);
//            progress.remove(CourseID);
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
        if(progress2.containsKey(courseID)) {
            CourseProgress courseProgress = progress2.get(courseID);
            return courseProgress.isLessonCompleted(lessonID);
        }

//        if (progress.containsKey(courseID)) {
//            Map<String, Boolean> lessonProgress = progress.get(courseID);
//            return lessonProgress.getOrDefault(lessonID, false);
//        }

        return false;
    }

    public void addCertificate(Certificate certificate) {
        if (certificate != null) {
            certificates.add(certificate);
        }
    }

    public boolean isCourseCompleted(String courseID) {
        if(progress2.containsKey(courseID)) {
            CourseProgress courseProgress = progress2.get(courseID);
            return courseProgress.isCourseCompleted();
        }

//        if (progress.containsKey(courseID)) {
//            Map<String, Boolean> lessonProgress = progress.get(courseID);
//            for (Boolean completed : lessonProgress.values()) {
//                if (!completed) {
//                    return false;
//                }
//            }
//            return true; // all lessons are done
//        }

        return false;
    }

    public boolean markLessonCompleted(String courseID, String lessonID, double quizScore) {
        if(!progress2.containsKey(courseID)) {
            throw new IllegalArgumentException("Invalid Course ID");
        }

//        if (!progress.containsKey(courseID)) {
//            throw new IllegalArgumentException("Invalid Course ID");
//        }

//        Map<String, Boolean> lessonProgress = progress.get(courseID);
//        lessonProgress.put(lessonID, true);
//
        if(quizScore < 50.0 || quizScore > 100.0) {
            return false;
        }

        progress2.get(courseID).markLessonProgressCompleted(lessonID, quizScore);
        if(isCourseCompleted(courseID)) {
            addCertificate(new Certificate(this.ID + "_" + this.certificates.size(), courseID, this.ID));    }
        return true;
    }

    public List<Lesson> getCompletedLessons(CourseService courseService, String courseID) {
        List<Lesson> completedLessons = new ArrayList<>();
//        if (progress.containsKey(courseID)) {
//            Map<String, Boolean> lessonProgress = progress.get(courseID);
//            Course course = courseService.getRecord(courseID);
//            if (course != null) {
//
//                UpdateCourse(course, courseID);
//                for (Lesson lesson : course.getLessons()) {
//                    if (lessonProgress.getOrDefault(lesson.getSearchKey(), false)) {
//                        completedLessons.add(lesson);
//                    }
//                }
//            } else {
//                throw new IllegalArgumentException("Invalid Course ID");
//            }
//        } else {
//            throw new IllegalArgumentException("Invalid Course ID");
//        }

        if(progress2.containsKey(courseID)) {
            CourseProgress courseProgress = progress2.get(courseID);
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
//        for (String courseID : progress.keySet()) {
//            Course course = courseService.getRecord(courseID);
//            if (course != null) {
//                enrolledCourses.add(course);
//            }
//        }

        for (String courseID : progress2.keySet()) {
            Course course = courseService.getRecord(courseID);
            if (course != null) {
                enrolledCourses.add(course);
            }
        }

        return enrolledCourses;
    }


    private void updateCourse(Course course, String courseID) {
//        if (!progress.containsKey(courseID)) {
//            return;
//        }
        if(!progress2.containsKey(courseID)) {
            return;
        }

        progress2.get(courseID).updateCourseProgress(course);


//        Map<String, Boolean> lessonProgress = progress.get(courseID);
//        Map<String, Boolean> newProgress = new HashMap<>();
//
//
//        for (Lesson lesson : course.getLessons()) {
//            String lessonID = lesson.getSearchKey();
//
//            newProgress.put(lessonID, lessonProgress.getOrDefault(lessonID, false));
//        }
//        progress.put(courseID, newProgress);
    }


    public void updateLessonId(String courseID, String oldLessonID, String newLessonID) {
//        if (progress.containsKey(courseID)) {
//            Map<String, Boolean> lessonProgress = progress.get(courseID);
//            if (lessonProgress.containsKey(oldLessonID)) {
//
//                Boolean completionStatus = lessonProgress.get(oldLessonID);
//                lessonProgress.remove(oldLessonID);
//                lessonProgress.put(newLessonID, completionStatus);
//            }
//        }

        if (progress2.containsKey(courseID)) {
            CourseProgress courseProgress = progress2.get(courseID);
            courseProgress.updateProgressLessonID(oldLessonID, newLessonID);
        }
    }


    public void updateCourseId(String oldCourseID, String newCourseID) {
//        if (progress.containsKey(oldCourseID)) {
//
//            Map<String, Boolean> lessonProgress = progress.get(oldCourseID);
//            progress.remove(oldCourseID);
//            progress.put(newCourseID, lessonProgress);
//        }

        if (progress2.containsKey(oldCourseID)) {

            CourseProgress courseProgress = progress2.get(oldCourseID);
            progress2.remove(oldCourseID);
            progress2.put(newCourseID, courseProgress);
        }
    }

    public Map<String, Map<String, Double>> getProgress() {
        Map<String, Map<String, Double>> res = new HashMap<>();
        for (String courseID : progress2.keySet()) {
            CourseProgress courseProgress = progress2.get(courseID);
            res.put(courseID, courseProgress.getCourseProgress());
        }
        return res;
    }

    public void setProgress(Map<String, Map<String, Double>> progress) {
        for (String courseID : progress.keySet()) {
            Map<String, Double> lessonProgress = progress.get(courseID);
            CourseProgress courseProgress = new CourseProgress(lessonProgress);
            for (String lessonID : lessonProgress.keySet()) {
                Double score = lessonProgress.get(lessonID);
                courseProgress.markLessonProgressCompleted(lessonID, score);
            }
            this.progress2.put(courseID, courseProgress);
        }
    }
}