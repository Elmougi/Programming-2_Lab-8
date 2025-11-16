package database;

import CourseManagement.Course;
import Users.Instructor;
import Users.Student;
import Users.User;

import javax.json.*;
import java.util.ArrayList;

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

        return null;
    }

    @Override
    public JsonObject recordsToJson(ArrayList<Course> courses) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for (Course courserHolder : courses) {
            arrayBuilder.add( Json.createObjectBuilder()
                    .add("Course ID", courserHolder.getSearchKey())
                    .add("Title", courserHolder.getTitle())
                    .add("Description", courserHolder.getDescription())
                    .add("Instructor ID", courserHolder.getInstructorId() )
                    .build());
        }

        return Json.createObjectBuilder().add("users", arrayBuilder.build() ).build();
    }



    private Course retrieveCourse(JsonValue value){
        Course course = null;
        JsonObject courseObj = (JsonObject) value;
        if(courseObj != null){
            course = new Course(
                    courseObj.getString("Course ID"),
                    courseObj.getString("Title"),
                    courseObj.getString("Description"),
                    courseObj.getString("Instructor ID")
            );
        }
        return course;
    }


}

