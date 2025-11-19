package CourseManagement;

import java.util.*;
import Utilities.Validation;

public class Question {
    private String questionText;
    private List<String> options;
    private int answerIndex;

    public Question(String questionText, List<String> options, int answerIndex) {
        this.questionText = questionText;
        this.options = options;
        this.answerIndex = answerIndex;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getOptions() {
        return Collections.unmodifiableList(options);
    }

    public int getAnswerIndex() {
        return answerIndex;
    }

    public void setQuestionText(String questionText) {
        if(!Validation.isValidString(questionText)) {
            throw new IllegalArgumentException("Invalid Question Text");
        }
        this.questionText = questionText;
    }

    public void setOptions(List<String> options) {
        for(String option : options) {
            if(!Validation.isValidString(option)) {
                throw new IllegalArgumentException("Invalid Option Text");
            }
        }
        this.options = options;
    }

    public void setAnswerIndex(int answerIndex) {
        if(answerIndex < 0 || answerIndex >= options.size()) {
            throw new IllegalArgumentException("Answer index out of bounds");
        }
        this.answerIndex = answerIndex;
    }

    public boolean isCorrect(int input) {
        return this.answerIndex == input;
    }

}
