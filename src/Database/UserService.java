package database;

import javax.json.*;
import java.util.*;
import Users.*;

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

    private User retrieveUser(JsonValue value){
        User user;
        JsonObject userObj = (JsonObject) value;
        if(userObj.getString("role").equals("Student")){
            user = new Student(
                    userObj.getString("name"),
                    userObj.getString("id"),
                    userObj.getString("email"),
                    userObj.getString("password")
            );
        } else{
            user = new Instructor(
                    userObj.getString("name"),
                    userObj.getString("id"),
                    userObj.getString("email"),
                    userObj.getString("password")
            );
        }
        return user;
    }

    @Override
    public JsonObject recordsToJson(ArrayList<User> users){
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for (User user : users) {
            arrayBuilder.add(Json.createObjectBuilder()
                    .add("id", user.getSearchKey())
                    .add("name", user.getName())
                    .add("email", user.getEmail())
                    .add("password", user.getPassword())
                    .add("role", user.getRole())
                    .build());
        }

        return Json.createObjectBuilder()
                .add("users", arrayBuilder.build())
                .build();
    }
}
