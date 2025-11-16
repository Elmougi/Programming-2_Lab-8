package Database;

import javax.json.*;
import javax.json.stream.JsonGenerator;
import java.util.*;
import java.io.*;

public abstract class JsonDatabaseManager<Obj extends DataInfo> {
    private ArrayList<Obj> records = new ArrayList<>();
    private String filename;

    public JsonDatabaseManager(String filename) throws IllegalArgumentException {
        records = new ArrayList<>();

        if (filename.equals("user")) {
            this.filename = "users.json";
        } else if (filename.equals("course")) {
            this.filename = "courses.json";
        } else{
            throw new IllegalArgumentException("Invalid filename");
        }

        loadFromFile();
    }

    public abstract ArrayList<Obj> recordsFromJson(JsonObject all);
    public abstract JsonObject recordsToJson(ArrayList<Obj> records);

    public void readFromFile() throws IOException {
        File file = new File(filename);

        if (!file.exists() || file.length() == 0) {
            return;
        }

        try (FileInputStream fis = new FileInputStream(file);
             JsonReader reader = Json.createReader(fis)) {

            JsonObject jsonObject = reader.readObject();
            records = recordsFromJson(jsonObject);

        } catch (JsonException e) {
            return; // no problem, empty array will continue
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

        saveToFile();
    }

    public void deleteRecord(String key) {

        for (int i = 0; i < records.size(); i++) {
            if (key != null && key.equals(records.get(i).getSearchKey())) {
                records.remove(i);
                return;
            }
        }
        System.out.println("RECORD (TO BE DELETED) NOT FOUND!");

        saveToFile();
    }

    public void saveToFile() {
        try (FileOutputStream fos = new FileOutputStream(filename)) {

            // Create pretty-printing configuration
            Map<String, Object> config = new HashMap<>();
            config.put(JsonGenerator.PRETTY_PRINTING, true);

            JsonWriterFactory writerFactory = Json.createWriterFactory(config);

            JsonWriter jsonWriter = writerFactory.createWriter(fos);

            jsonWriter.write(recordsToJson(records));

            jsonWriter.close();

        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        try {
            records.clear();
            readFromFile();
        } catch (IOException e) {
            System.out.println("An error occurred while trying to load files: " + e.getMessage());
        }

    }

    public int numberOfRecords() {
        return records.size();
    }
}
