package database;

import javax.json.*;
import java.util.*;
import java.io.*;

import utilities.Validation;

public abstract class JsonDatabaseManager<Obj extends DataInfo> {
    private ArrayList<Obj> records = new ArrayList<>();
    private String filename;

    public JsonDatabaseManager(String filename) throws IllegalArgumentException {
        if (filename.equals("user")) {
            this.filename = "user.json";
        } else if (filename.equals("course")) {
            this.filename = "courses.json";
        } else{
            throw new IllegalArgumentException("Invalid filename");
        }
    }

    public abstract ArrayList<Obj> recordsFromJson(JsonObject all);
    public abstract JsonObject recordsToJson(ArrayList<Obj> records);

    public void readFromFile() throws IOException {
        try (FileInputStream fis = new FileInputStream(filename);
             JsonReader reader = Json.createReader(fis)) {

            records = recordsFromJson(reader.readObject());
        }
    }

    public ArrayList<Obj> returnAllRecords() {
        return records;
    }

    public boolean contains(String key) {

        for (int i = 0; i < records.size(); i++) {
            if (key != null && key.equals(records.get(i).getSearchKey()))
                return true;
        }
        return false;
    }

    public Obj getRecord(String key) {

        for (int i = 0; i < records.size(); i++) {
            if (key != null && key.equals(records.get(i).getSearchKey()))
                return records.get(i);
        }
        System.out.println("RECORD (TO BE RETURNED) NOT FOUND!");
        return null;
    }

    public void insertRecord(Obj record) {
        for (int i = 0; i < records.size(); i++) {
            if (record != null && record.getSearchKey().equals(records.get(i).getSearchKey())) {
                System.out.println("RECORD (TO BE INSERTED) ALREADY EXISTS!");
                return;
            }
        }
        records.add(record);
    }

    public void deleteRecord(String key) {

        for (int i = 0; i < records.size(); i++) {
            if (key != null && key.equals(records.get(i).getSearchKey())) {
                records.remove(i);
                return;
            }
        }
        System.out.println("RECORD (TO BE DELETED) NOT FOUND!");
    }

    public void saveToFile() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filename);
             JsonWriter writer = Json.createWriter(fos)) {
            JsonObject jsonObject = recordsToJson(records);

            writer.writeObject(jsonObject);
            System.out.println("JSON written to: " + filename);
        }
    }

    public int numberOfRecords() {
        return records.size();
    }
}
