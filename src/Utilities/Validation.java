package Utilities;

public class Validation {
    public static boolean isValidString(String str) {
        return (str != null && !str.trim().isEmpty());
    }

    public static boolean isValidInt(int studentId) {
        return studentId > 0;
    }

    public static boolean isValidAge(int age) {
        return age > 15 && age < 40;
    }

    public static boolean isValidGPA(float gpa) {
        return gpa >= 0.0f && gpa <= 4.0f;
    }

    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        } else if (!email.contains("@") || !email.contains(".")) {
            return false;
        }

        int ATindex = email.indexOf("@");
        int LastATindex = email.lastIndexOf("@");
        int DOTindex = email.lastIndexOf(".");

        if (ATindex > DOTindex) {
            return false;
        } else if (email.startsWith("@") || email.startsWith(".") ||
                email.endsWith("@") || email.endsWith(".")) {
            return false;
        } else if (ATindex != LastATindex) {
            return false;
        } else if (DOTindex - ATindex == 1) {
            return false;
        }

        return true;
    }
}
