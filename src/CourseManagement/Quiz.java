package CourseManagement;

import Database.DataInfo;
import java.util.*;

public class Quiz {
//    private List<String[]> quizData;
//    private List<String> questions;
//    private List<List<String>> options;
//    private List<Integer> answersIndex;
    // private Map<Map<String, List<String>>, Integer> quizData;
    // Map that contains question+options and the answer index in the options
    // the questions+options is a map where the options is a list of strings
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
//    public Quiz(String quizId, Map<Map<String, List<String>>, Integer> quizData) {
//        this.quizId = quizId;
//        if (quizData != null) this.quizData = quizData;
//        else this.quizData = new HashMap<>();
//    }

//    public Map<String, List<String>> getQuizData() {
//        Map<String, List<String>> res = new HashMap<>();
//
//        for(int i=0; i < questions.size(); i++) {
//            res.put(questions.get(i), options.get(i));
//        }
//
//        return Collections.unmodifiableMap(res);
//    }
//
//    public List<String> getQuestions() {
//        return Collections.unmodifiableList(questions);
//    }
//
//    public List<List<String>> getOptions() {
//        return Collections.unmodifiableList(options);
//    }
//
//    public List<Integer> getAnswersIndex() {
//        return Collections.unmodifiableList(answersIndex);
//    }
//
//    public void setQuizData(List<String> questions, List<List<String>> options, List<Integer> answersIndex) {
//        if(questions.size() != options.size() || answersIndex.size() != questions.size()) {
//            throw new IllegalArgumentException("Number of questions and answers must be the same.");
//        }
//
//        this.questions = questions;
//        this.options = options;
//        this.answersIndex = answersIndex;
//    }
//
//    public void addQuizQuestion(String question, List<String> options, int answerIndex) {
//        if(answerIndex < 0 || answerIndex >= options.size()) {
//            throw new IllegalArgumentException("Answer index is out of bounds.");
//        }
//
//        this.questions.add(question);
//        this.options.add(options);
//        this.answersIndex.add(answerIndex);
//    }
//
//    public boolean isCorrect(int index, int answerIndex) {
//        if(index < 0 || index >= questions.size()) {
//            throw new IllegalArgumentException("Question index is out of bounds.");
//        }
//
//        return this.answersIndex.get(index) == answerIndex;
//    }
//
//    public double calculateScore(List<Integer> userAnswers) {
//        if(userAnswers.size() != questions.size()) {
//            throw new IllegalArgumentException("Number of user answers must match number of questions.");
//        }
//
//        int correctCount = 0;
//        for(int i = 0; i < questions.size(); i++) {
//            if(isCorrect(i, userAnswers.get(i))) {
//                correctCount++;
//            }
//        }
//
//        return (double) correctCount / questions.size() * 100;
//    }


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
