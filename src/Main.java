import GUI.login;
import CourseManagement.*;
import Database.*;
import UserManagement.*;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        initializeSampleDataIfEmpty();

        SwingUtilities.invokeLater(() -> new login());
    }

    private static void initializeSampleDataIfEmpty() {
        try {
            CourseService courseService = new CourseService();
            UserService userService = new UserService();

            if (userService.numberOfRecords() == 0) {
                System.out.println("No users found. Creating sample accounts...\n");

                Student student1 = new Student("Ahmed Mohamed", "S001", "ahmed@student.com", "pass123");
                Student student2 = new Student("Sara Ali", "S002", "sara@student.com", "pass456");
                Instructor instructor1 = new Instructor("Dr. Hassan", "I001", "hassan@instructor.com", "teach123");

                userService.insertRecord(student1);
                userService.insertRecord(student2);
                userService.insertRecord(instructor1);


            } else {
                System.out.println("Found " + userService.numberOfRecords() + " existing users\n");
            }

            if (courseService.numberOfRecords() == 0) {
                System.out.println("No courses found. Creating sample courses...\n");

                Course javaCourse = new Course("CS101", "Java Programming",
                        "Learn Java from basics to advanced", "I001", null);

                javaCourse.addLesson(new Lesson("L001", "Introduction to Java",
                        "This lesson covers the basics of Java programming including syntax, variables, and data types."));
                javaCourse.addLesson(new Lesson("L002", "Object-Oriented Programming",
                        "Learn about classes, objects, inheritance, and polymorphism in Java."));
                javaCourse.addLesson(new Lesson("L003", "Java Collections Framework",
                        "Understanding ArrayList, HashMap, and other collection classes."));

                courseService.insertRecord(javaCourse);

                Course webCourse = new Course("WEB201", "Web Development",
                        "Build modern web applications", "I001", null);

                webCourse.addLesson(new Lesson("L101", "HTML & CSS Basics",
                        "Learn the fundamentals of HTML structure and CSS styling."));
                webCourse.addLesson(new Lesson("L102", "JavaScript Essentials",
                        "Introduction to JavaScript programming for web browsers."));

                courseService.insertRecord(webCourse);

                System.out.println("Sample courses created successfully\n");
            } else {
                System.out.println("Found " + courseService.numberOfRecords() + " existing courses\n");
            }

        } catch (Exception e) {
            System.err.println("Error during initialization: " + e.getMessage());

        }
    }
}