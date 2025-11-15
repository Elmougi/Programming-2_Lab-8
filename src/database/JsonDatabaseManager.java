package database;

import javax.json.*;
import java.util.*;

public abstract class JsonDatabaseManager<Obj extends DataInfo> {
    private ArrayList<Obj> records = new ArrayList<>();
    private String filename;



}
