package UserManagement;

import CourseManagement.Course;

public class Admin extends User {
    public Admin(String name, String ID, String email, String password) {
        super(name, ID, email, password);
        this.role = "Admin";
    }

    public void approveCourse(Course course) {
        course.setStatus("APPROVED");
    }
    public void rejectCourse(Course course) {
        course.setStatus("REJECTED");
    }


}
