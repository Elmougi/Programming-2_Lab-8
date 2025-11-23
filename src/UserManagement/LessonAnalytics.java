package UserManagement;

public class LessonAnalytics extends CourseAnalytics {
    private String lessonID;

    public LessonAnalytics(String courseId, String lessonID, java.util.List<UserManagement.Student> students) {
        super(courseId, students);
        this.lessonID = lessonID;
    }

    public double getLessonCompletionPercentage() {
        int completedCount = 0;
        for (UserManagement.Student student : super.students) {
            if (student.getCourseProgress(super.courseId).isLessonCompleted(lessonID)) {
                completedCount++;
            }
        }
        return (double) completedCount / getTotalStudentsCount() * 100;
    }

    public int getNoOfStudentsCompletedLesson() {
        int count = 0;
        for (UserManagement.Student student : super.students) {
            if (student.getCourseProgress(super.courseId).isLessonCompleted(lessonID)) {
                count++;
            }
        }
        return count;
    }

    public double getLessonTotalScore() {
        double total = 0;
        for (UserManagement.Student student : super.students) {
            if (student.getCourseProgress(super.courseId).isLessonCompleted(lessonID)) {
                double score = student.getCourseProgress(super.courseId).getQuizScore(lessonID);
                if (score >= 0) {
                    total += score;
                }
            }
        }
        return total;
    }

    public double getLessonAverageScore() {
        double total = 0;
        for (UserManagement.Student student : super.students) {
            if (student.getCourseProgress(super.courseId).isLessonCompleted(lessonID)) {
                double score = student.getCourseProgress(super.courseId).getQuizScores().get(lessonID);
                if (score >= 0) {
                    total += score;
                }
            }
        }
        return total / getNoOfStudentsCompletedLesson();
    }

    public Student[] getLessonTop3Students(){
        Student[] topStudents = new Student[3];
        topStudents[0] = null; topStudents[1] = null; topStudents[2] = null;
        double top1 = 0.0, top2 = 0.0, top3 = 0.0;
        for (Student student : students) {
            double totalScore = student.getCourseProgress(courseId).getQuizScore(lessonID);
            if (totalScore >= top1) {
                top3 = top2;
                top2 = top1;
                top1 = totalScore;
                if (topStudents[0] != null) {
                    topStudents[2] = topStudents[1];
                    topStudents[1] = topStudents[0];
                    topStudents[0] = student;
                } else {
                    topStudents[0] = student;
                }
            } else if (totalScore >= top2) {
                top3 = top2;
                top2 = totalScore;
                if (topStudents[1] != null) {
                    topStudents[2] = topStudents[1];
                    topStudents[1] = student;
                } else {
                    topStudents[1] = student;
                }
            } else if (totalScore >= top3) {
                top3 = totalScore;
                topStudents[2] = student;
            }
        }

        return topStudents;
    }



}
