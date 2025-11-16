package CourseManagement;

import Utilities.Validation;

public class Lesson {

    private String lessonID;
    private String title;
    private String content;
    private String[] resources;

    public Lesson(String lessonID, String title, String content, String[] resources) {
        this.lessonID = lessonID;
        this.title = title;
        this.content = content;
        this.resources = resources;
    }

    public Lesson(String lessonID, String title, String content) {
        this.lessonID = lessonID;
        this.title = title;
        this.content = content;
        this.resources = new String[0];
    }

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

    public void setResources(String[] resources) {
        if(resources == null) {
            this.resources = new String[0];
            return;
        }

        this.resources = resources;
    }

    public String getLessonID() {
        return lessonID;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String[] getResources() {
        return resources;
    }


}
