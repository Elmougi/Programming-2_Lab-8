

//public class Main {
//    public static void main(String[] args) {
//        System.out.println("Hello World");
//    }
//}

import CourseManagement.*;
import java.util.*;
import Database.*;
import UserManagement.*;
import Utilities.Validation;

public class Main {
    public static void main(String[] args) {
        try {

            CourseService coursesData = new CourseService();
            UserService userData = new UserService();

            userData.insertRecord(new Student("as", "123", "abd@gma.com", "394872"));
            userData.insertRecord(new Instructor("agw", "12ewg3", "abdweg@gma.com", "394eg872"));

            Course one = new Course("2ed", "sojs", "this is desc", "12ewg3", null);
            one.addLesson(new Lesson("32", "aef", "I am uebfue iyefbew ibweivf"));
            coursesData.insertRecord(one);
        } catch (Exception e) {
            System.out.println("error; \"" + e.getMessage() + "\"");
        }

    }
}