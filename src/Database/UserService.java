package Database;

import javax.json.*;
import java.util.*;
import UserManagement.*;

// Note to remember
/*
    JsonObject = like a map -> [ key1:value1, key2:value2 ]
    JsonArray = like a list -> [ value1, value2, value3 ]
 */

public class UserService extends JsonDatabaseManager<User> {
    public UserService(){
        super("user");
    }

    @Override
    public ArrayList<User> recordsFromJson(JsonObject all){
        ArrayList<User> users = new ArrayList<>();
        JsonArray usersArray = all.getJsonArray("users");

        if (usersArray != null) {
            for (JsonValue value : usersArray) {
                users.add(retrieveUser(value));
            }
        }
        return users;
    }

    @Override
    public JsonObject recordsToJson(ArrayList<User> users){
        // object of arrays -> key:array_of_users
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for (User user : users) {
            JsonObjectBuilder userBuilder = Json.createObjectBuilder()
                    .add("id", user.getSearchKey())
                    .add("name", user.getName())
                    .add("email", user.getEmail())
                    .add("password", user.getPasswordHash())
                    .add("role", user.getRole());


            if (user instanceof Student) {
                userBuilder.add("progress", buildProgress((Student)  user));
                userBuilder.add("certificates", buildCertificates((Student) user));
            }

            arrayBuilder.add(userBuilder.build());
        }

        return Json.createObjectBuilder()
                .add("users", arrayBuilder.build())
                .build();
    }

    // --------------------------------------------------------------------------------------------
    // retrieve peripherals:

    private User retrieveUser(JsonValue value){
        User user;
        JsonObject userObj = (JsonObject) value;

        if(userObj.getString("role").equals("Student")){
            Student student = new Student(
                    userObj.getString("name"),
                    userObj.getString("id"),
                    userObj.getString("email"),
                    userObj.getString("password")
            );

            student.setProgress(retrieveProgress(userObj));
            retrieveCertificates(userObj, student);

            user = student;
        } else {
            user = new Instructor(
                    userObj.getString("name"),
                    userObj.getString("id"),
                    userObj.getString("email"),
                    userObj.getString("password")
            );
        }
        return user;
    }

    private Map<String, Map<String, Boolean>> retrieveProgress(JsonObject userObj) {
        Map<String, Map<String, Boolean>> progress = new HashMap<>();

        if (userObj.containsKey("progress")) {
            JsonObject progressObj = userObj.getJsonObject("progress");

            for (String courseId : progressObj.keySet()) {
                progress.put(courseId, retrieveLessonProgress(progressObj, courseId));
            }
        }

        return progress;
    }

    private Map<String, Boolean> retrieveLessonProgress(JsonObject progressObj, String courseId){
        JsonObject lessonProgressObj = progressObj.getJsonObject(courseId);
        Map<String, Boolean> lessonProgress = new HashMap<>();

        for (String lessonId : lessonProgressObj.keySet()) {
            lessonProgress.put(lessonId, lessonProgressObj.getBoolean(lessonId));
        }

        return lessonProgress;
    }

    private void retrieveCertificates(JsonObject userObj, Student student){
        if (userObj.containsKey("certificates")) {
            JsonObject certificatesObj = userObj.getJsonObject("certificates");
            JsonArray certificatesArray = certificatesObj.getJsonArray("certificates");

            for (JsonValue certificateValue : certificatesArray) {
                JsonObject certificateObj = (JsonObject) certificateValue;
                student.addCertificate(new Certificate(
                        certificateObj.getString("id"),
                        certificateObj.getString("courseId"),
                        student.getSearchKey(),
                        certificateObj.getString("dateIssued")
                ));
            }
        }
    }

    // --------------------------------------------------------------------------------------------
    // build peripherals:

    private JsonObject buildProgress(Student student) {
        // object of objects
        JsonObjectBuilder progressBuilder = Json.createObjectBuilder();
        Map<String, Map<String, Boolean>> progress = student.getProgress();

        for (Map.Entry<String, Map<String, Boolean>> courseEntry : progress.entrySet()) {
            progressBuilder.add(courseEntry.getKey(), buildLessonProgress(courseEntry.getValue()));
        }

        return progressBuilder.build();
    }

    private JsonObject buildLessonProgress(Map<String, Boolean> lessonProgress) {
        JsonObjectBuilder lessonProgressBuilder = Json.createObjectBuilder();

        for (Map.Entry<String, Boolean> entry : lessonProgress.entrySet()) {
            lessonProgressBuilder.add(entry.getKey(), entry.getValue());
        }

        return lessonProgressBuilder.build();
    }

    private JsonObject buildCertificates(Student student) {
        // array of objects
        JsonArrayBuilder certificatesArrayBuilder = Json.createArrayBuilder();

        for (Certificate certificate : student.getCertificates()) {
            certificatesArrayBuilder.add(Json.createObjectBuilder()
                    .add("id", certificate.getCertificateId())
                    .add("courseId", certificate.getCourseId())
                    .add("dateIssued", certificate.getIssueDate())
                    .build());
        }

        return Json.createObjectBuilder()
                .add("certificates", certificatesArrayBuilder.build())
                .build();
    }
}