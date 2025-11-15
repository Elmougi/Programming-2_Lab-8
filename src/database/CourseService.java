package database;

import javax.json.JsonObject;
import java.util.ArrayList;

public class CourseService  <Obj extends DataInfo> extends JsonDatabaseManager<Obj> {
    public CourseService(){
        super("course")
    }

    @Override
    public ArrayList<Obj> recordsFromJson(JsonObject all){
        return null;
    }

    @Override
    public JsonObject recordsToJson(ArrayList<Obj> records){
        return new JsonObject() {
        }
    }
}
