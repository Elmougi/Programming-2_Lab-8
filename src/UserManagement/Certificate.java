package UserManagement;

import java.time.LocalDate;

public class Certificate {
    private final String certificateId;
    private final String courseId;
    private final String studentId;
    private final String issueDate;

    public Certificate(String certificateId, String courseId, String studentId) {
        this.certificateId = certificateId;
        this.courseId = courseId;
        this.studentId = studentId;
        this.issueDate = LocalDate.now().toString();
    }

    public Certificate(String certificateId, String courseId, String studentId, String issueDate) {
        this.certificateId = certificateId;
        this.courseId = courseId;
        this.studentId = studentId;
        this.issueDate = issueDate;
    }
    // I do not need setters, it will be used internally; hence, no setters nor validation is needed

    public String getCertificateId() {
        return certificateId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getIssueDate() {
        return issueDate;
    }
}
