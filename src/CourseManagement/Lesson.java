package CourseManagement;

import Database.DataInfo;
import Utilities.Validation;
import java.util.*;

public class Lesson implements DataInfo {

    private String lessonID;
    private String title;
    private String content;
    private List<String> resources;
    private Quiz quiz;

    public Lesson(String lessonID, String title, String content, List<String> resources, Quiz quiz) {
        this.resources = new ArrayList<>();
        setContent(content);
        setLessonID(lessonID);
        setTitle(title);
        if(resources != null)
            this.resources.addAll(resources);
        else
            this.resources = new ArrayList<>();
        if(quiz != null)
            this.quiz = quiz;
        else
            this.quiz = new Quiz();
    }

    public Lesson(String lessonID, String title, String content, List<String> resources){
        this(lessonID, title, content, resources, new Quiz());
    }
    public Lesson(String lessonID, String title, String content) {
        this(lessonID, title, content, null, new Quiz());
    }
    public Lesson(String lessonID, String title, String content, Quiz quiz) {
        this(lessonID, title, content, null, quiz);
    }

    @Override
    public String getSearchKey() { return lessonID; }

    public void setLessonID(String lessonID) {
        if(!Validation.isValidString(lessonID)) {
            throw new IllegalArgumentException("Invalid Lesson ID");
        }

        this.lessonID = lessonID;
    }

    public void setTitle(String title) {
        if(!Validation.isValidString(title)) {
            throw new IllegalArgumentException("Invalid Title");
        }

        this.title = title;
    }

    public void setContent(String content) {
        if(!Validation.isValidString(content)) {
            throw new IllegalArgumentException("Invalid Content");
        }

        this.content = content;
    }

    public void addResource(String resource) {
        if(resources == null) {;
            return;
        }

        this.resources.add(resource);
    }


    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public List<String> getResources() {
        return resources;
    }

   /* public boolean searchResource(String resource) {
        for(String current : resources){
            if (resource != null && resource.equals(current))
                return true;
        }
        return false;
    }for later use lab8 inshallah*/

    public void removeResource(String resource) {
        if(!resources.contains(resource)) {
            return;
        }
        resources.remove(resource);
    }

}
