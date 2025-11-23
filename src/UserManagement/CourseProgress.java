package UserManagement;

import CourseManagement.Course;
import CourseManagement.Lesson;

import java.util.*;

public class CourseProgress {
    private List<String> lessonsID = new ArrayList<>();
    private List<Boolean> isCompleted = new ArrayList<>();
    private Map<String, Double> quizScores = new HashMap<>(); // <LessonID, Score>
    private Map<String, Integer> quizAttempts = new HashMap<>(); // Elmougi sends his regards

    public CourseProgress(Course course) {
        for (var lesson : course.getLessons()) {
            lessonsID.add(lesson.getSearchKey());
            isCompleted.add(false);
            quizScores.put(lesson.getSearchKey(), 0.0);
            quizAttempts.put(lesson.getSearchKey(), 0); // elmougi sends his regards
        }
    }
    public CourseProgress(Map<String, Double> quizScores) {
        for (String lessonID : quizScores.keySet()) {
            lessonsID.add(lessonID);
            if(quizScores.get(lessonID) < 50 || quizScores.get(lessonID) > 100) {
                isCompleted.add(false);
            } else {
                isCompleted.add(true);
            }
            this.quizScores.put(lessonID, quizScores.get(lessonID));
            this.quizAttempts.put(lessonID, 0); // elmougi sends his regards
        }
    }

    public boolean isLessonCompleted(String lessonID) {
        return isCompleted.get(this.lessonsID.indexOf(lessonID));
    }

    public boolean isCourseCompleted() {
        for (boolean completed : isCompleted) {
            if (!completed) {
                return false;
            }
        }
        return true;
    }

    public void markLessonProgressCompleted(String lessonID, double quizScore) {
        int index = this.lessonsID.indexOf(lessonID);
        if (index != -1) {
            isCompleted.set(index, true);
            quizScores.put(lessonID, quizScore);
        } else {
            throw new IllegalArgumentException("Invalid Lesson ID");
        }
    }

    public List<String> getCompletedLessons() {
        List<String> completedLessons = new ArrayList<>();
        for (int i = 0; i < lessonsID.size(); i++) {
            if (isCompleted.get(i)) {
                completedLessons.add(lessonsID.get(i));
            }
        }
        return completedLessons;
    }

    // NOTICE: if a lesson's ID is not changed, progress is the same even if the lesson content is changed
    public void updateCourseProgress(Course course) {
        List<String> newLessonsID = new ArrayList<>();

        for (Lesson lesson : course.getLessons()) {
            String lessonID = lesson.getSearchKey();
            newLessonsID.add(lesson.getSearchKey()); // save new lessons id
            if (!lessonsID.contains(lessonID)) {
                lessonsID.add(lessonID);
                isCompleted.add(false);
                quizScores.put(lessonID, 0.0);
                quizAttempts.put(lessonID, 0); // elmougi sends his regards
            }
        }

        for(String currentLessonID : lessonsID){
            if(!newLessonsID.contains(currentLessonID)){
                int index = lessonsID.indexOf(currentLessonID);
                lessonsID.remove(currentLessonID);
                isCompleted.remove(index);
                quizScores.remove(currentLessonID);
                quizAttempts.remove(currentLessonID); // elmougi sends his regards
            }
        } // if any lesson is removed from course, remove it from progress as well
    }

    public Map<String, Double> getCourseProgress(){
        Map<String, Double> progress = new HashMap<>();
        for (int i = 0; i < lessonsID.size(); i++) {
            String id = lessonsID.get(i);
            progress.put(id, quizScores.get(id));
        }
        return progress;
    }

    public Map<String, Double> getQuizScores() {
        return quizScores;
    }

    // Elmougi sends his regards
    public void incrementQuizAttempt(String lessonID) {
        if (!lessonsID.contains(lessonID)) {
            throw new IllegalArgumentException("Invalid Lesson ID");
        }
        quizAttempts.put(lessonID, quizAttempts.getOrDefault(lessonID, 0) + 1);
    }

    public int getTotalAttempts(String lessonID) {
        if (!lessonsID.contains(lessonID)) {
            return 0;
        }
        return quizAttempts.getOrDefault(lessonID, 0);
    }

    public Map<String, Integer> getQuizAttempts() {
        return quizAttempts;
    }


    // Analytics per Student
    public double getTotalScore() {
        double total = 0.0;
        for (double score : quizScores.values()) {
            total += score;
        }
        return total;
    }
    public double getAverageScore() {
        if (quizScores.isEmpty()) {
            return 0.0;
        }
        return getTotalScore() / quizScores.size();
    }
    public Map<String, Double> getAllScores() {
        return Collections.unmodifiableMap(quizScores);
    }

}
