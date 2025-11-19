package Database;

import CourseManagement.*;
import javax.json.*;
import java.util.ArrayList;
import java.util.List;

public class CourseService extends JsonDatabaseManager<Course> {

    public CourseService(){
        super("course");
    }

    @Override
    public ArrayList<Course> recordsFromJson(JsonObject all){
        ArrayList<Course> courses = new ArrayList<>();
        JsonArray coursesArray = all.getJsonArray("courses");

        if (coursesArray != null) {
            for (JsonValue value : coursesArray) {
                courses.add(retrieveCourse(value));
            }
        }

        return courses;
    }

    @Override
    public JsonObject recordsToJson(ArrayList<Course> courses) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder(); // main object with many courses - elements

        for (Course course : courses) {

            arrayBuilder.add(Json.createObjectBuilder()
                    .add("courseId", course.getSearchKey())
                    .add("title", course.getTitle())
                    .add("description", course.getDescription())
                    .add("instructorId", course.getInstructorId())
                    .add("status", course.getStatus())
                    .add("lessons", buildLessonsArray(course.getLessons()))
                    .build());
        }

        return Json.createObjectBuilder().add("courses", arrayBuilder.build()).build();
    }

    // --------------------------------------------------------------------------------------------
    // retrieve peripherals:

    private Course retrieveCourse(JsonValue value){
        Course course = null;
        JsonObject courseObj = (JsonObject) value;
        if(courseObj != null){
            String courseId = courseObj.getString("courseId");
            String title = courseObj.getString("title");
            String description = courseObj.getString("description", "");
            String instructorId = courseObj.getString("instructorId");
            String status = courseObj.getString("status");
            List<Lesson> lessons = retrieveLessons(courseObj.getJsonArray("lessons"));

            return new Course(courseId, title, description, instructorId, lessons, status);
        } else {
            return null;
        }
    }

    private List<Lesson> retrieveLessons(JsonArray lessonsArray) {
            if (lessonsArray == null) {
                return new ArrayList<>();
            }

            ArrayList<Lesson> lessons = new ArrayList<>();
            for (JsonValue value : lessonsArray) {
                JsonObject lessonObj = (JsonObject) value;
                String lessonID = lessonObj.getString("lessonID");
                String lessonTitle = lessonObj.getString("title");
                String content = lessonObj.getString("content");
                List<String> resources = retrieveResources(lessonObj.getJsonArray("resources"));
                Quiz quiz = retrieveQuiz(lessonObj.getJsonObject("quiz"));

                lessons.add(new Lesson(lessonID, lessonTitle, content, resources, quiz));
            }

            return lessons;
    }

    private List<String> retrieveResources(JsonArray resourcesArray) {
        if (resourcesArray == null) {
            return new ArrayList<>();
        }

        ArrayList<String> resources = new ArrayList<>();
        for (JsonValue value : resourcesArray) {
            resources.add(((JsonString) value).getString());
        }

        return resources;
    }

    private Quiz retrieveQuiz(JsonObject quizObj) {
        if (quizObj == null) {
            return new Quiz();
        }

        JsonArray questionsArray = quizObj.getJsonArray("questions");
        List<Question> questions = new ArrayList<>();

        if (questionsArray != null) {
            for (JsonValue value : questionsArray) {
                JsonObject questionObj = (JsonObject) value;
                String questionText = questionObj.getString("questionText");
                List<String> options = retrieveQuizOptions(questionObj.getJsonArray("options"));
                int correctAnswerIndex = questionObj.getInt("correctAnswerIndex");

                questions.add(new Question(questionText, options, correctAnswerIndex));
            }
        }

        return new Quiz(questions);
    }

    private List<String> retrieveQuizOptions(JsonArray answersArray) {
        if (answersArray == null) {
            return new ArrayList<>();
        }

        ArrayList<String> answers = new ArrayList<>();
        for (JsonValue value : answersArray) {
            answers.add(((JsonString) value).getString());
        }

        return answers;
    }

    // --------------------------------------------------------------------------------------------
    // build peripherals:

    private JsonArray buildLessonsArray(List<Lesson> lessons){
        JsonArrayBuilder builder = Json.createArrayBuilder();
        for (Lesson lesson : lessons) {
            builder.add(Json.createObjectBuilder()
                    .add("lessonID", lesson.getSearchKey())
                    .add("title", lesson.getTitle())
                    .add("content", lesson.getContent())
                    .add("resources", buildResourcesArray(lesson.getResources()))
                    .add("quiz", buildQuiz(lesson.getQuiz()))
                    .build());
        }
        return builder.build();
    }

    private JsonArray buildResourcesArray(List<String> resources) {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        if (resources != null) {
            for (String resource : resources) {
                builder.add(resource);
            }
        }
        return builder.build();
    }

    private JsonObject buildQuiz(Quiz quiz) {
        JsonObjectBuilder quizBuilder = Json.createObjectBuilder();
        if (quiz != null) {
            JsonArray questionsArray = buildQuestionsArray(quiz.getQuestions());
            quizBuilder.add("questions", questionsArray);
        }
        return quizBuilder.build();
    }

    private JsonArray buildQuestionsArray(List<Question> questions) {
        JsonArrayBuilder questionsArrayBuilder = Json.createArrayBuilder();
        if(questions != null) {
            for (Question question : questions) {
                questionsArrayBuilder.add(Json.createObjectBuilder()
                        .add("questionText", question.getQuestionText())
                        .add("options", buildOptionsArray(question.getOptions()))
                        .add("correctAnswerIndex", question.getAnswerIndex())
                        .build());
            }
        }

        return questionsArrayBuilder.build();
    }

    private JsonArray buildOptionsArray(List<String> options) {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        if (options != null) {
            for (String option : options) {
                builder.add(option);
            }
        }
        return builder.build();
    }
}

