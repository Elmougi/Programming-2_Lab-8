package UserManagement;

import Database.CourseService;
import java.util.*;

public class InstructorAnalytics {
    private String ID;
    private List<String> coursesID = new ArrayList<>();
    private Map<String, CourseAnalytics> courseAnalytics = new HashMap<>();

    public InstructorAnalytics(String ID) {
        this.ID = ID;
    }

    public void addCourseID(String courseId) {
        addCourseID(courseId, new ArrayList<>());
    }
    public void addCourseID(String courseId, CourseService courseService) {
        coursesID.add(courseId);
        courseAnalytics.put(courseId, new CourseAnalytics(courseId, courseService.getRecord(courseId).getStudents()));
    }
    public void addCourseID(String courseId, List<Student> students) {
        coursesID.add(courseId);
        courseAnalytics.put(courseId, new CourseAnalytics(courseId, students));
    }

    public void removeCourseID(String courseId) {
        coursesID.remove(courseId);
    }

    public List<String> getCoursesID() {
        return Collections.unmodifiableList(coursesID);
    }

    public List<Student> getStudents(String courseId) {
        return courseAnalytics.get(courseId).students;
    }

    public void updateStudents(String courseId, List<Student> students) {
        if (courseAnalytics.containsKey(courseId)) {
            courseAnalytics.put(courseId, new CourseAnalytics(courseId, students));
        }
    }

}
