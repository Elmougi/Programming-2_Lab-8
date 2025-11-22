package CourseManagement;

import Database.DataInfo;
import java.util.*;

public class Quiz {
    private List<Question> questions;

    public Quiz() {
        this.questions = new ArrayList<>();
    }
    public Quiz(List<Question> questions) {
        if(questions != null)
            this.questions = questions;
        else
            this.questions = new ArrayList<>();
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        if(questions != null)
            this.questions = questions;
    }

    public void addQuizQuestion(Question question) {
        if(question != null) {
            this.questions.add(question);
        }
    }

    public double calculateScore(List<Integer> userAnswers) {
        if(userAnswers.size() != questions.size()) {
            throw new IllegalArgumentException("Number of user answers must match number of questions.");
        }
        if(questions.isEmpty()) {
            return 100;
        }

        int correctCount = 0;
        for(int i = 0; i < questions.size(); i++) {
            if(questions.get(i).isCorrect(userAnswers.get(i))) {
                correctCount++;
            }
        }

        return (double) correctCount / questions.size() * 100;
    }



}
