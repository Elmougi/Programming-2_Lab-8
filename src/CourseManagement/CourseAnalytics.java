package CourseManagement;

import UserManagement.Student;
import java.util.*;

public class CourseAnalytics {
    protected String courseId;
    protected List<Student> students;

    public CourseAnalytics(String courseId, List<Student> students) {
        this.courseId = courseId;
        this.students = students;
    }

    public Map<String, Double> getAverageScores() {
        Map<String, Double> averageScores = new HashMap<>();
        for (Student student : students) {
            averageScores.put(student.getSearchKey(), student.getCourseProgress(courseId).getAverageScore());
        }
        return averageScores;
    }

    public Map<String, Double> getTotalScores() {
        Map<String, Double> totalScores = new HashMap<>();
        for (Student student : students) {
            totalScores.put(student.getSearchKey(), student.getCourseProgress(courseId).getTotalScore());
        }
        return totalScores;
    } // for all students -> may need to filter incomplete students

    // for students that have completed the course
    public double getOverallAverageScore() {
        double total = 0;
        for (Student student : students) {
            if(student.getCourseProgress(courseId).isCourseCompleted()) {
                double avgScore = student.getCourseProgress(courseId).getAverageScore();
                if (avgScore >= 0) {
                    total += avgScore;
                }
            }
        }
        return total / getNoOfCompletedStudents();
    }

    public int getNoOfCompletedStudents() {
        int count = 0;
        for (Student student : students) {
            if (student.getCourseProgress(courseId).isCourseCompleted()) {
                count++;
            }
        }
        return count;
    }

    public int getTotalStudentsCount() {
        return students.size();
    }

    public double getCompletionPercentage() {
        int completedCount = getNoOfCompletedStudents();
        int totalCount = getTotalStudentsCount();
        if (totalCount == 0) {
            return 0.0;
        }
        return (double) completedCount / totalCount * 100;
    }

    public Student[] getTop3Students(){
        Student[] topStudents = new Student[3];
        topStudents[0] = null; topStudents[1] = null; topStudents[2] = null;
        double top1 = 0.0, top2 = 0.0, top3 = 0.0;
        for (Student student : students) {
            double totalScore = student.getCourseProgress(courseId).getTotalScore();
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
    } // what if ==? which is better?



}
