

//public class Main {
//    public static void main(String[] args) {
//        System.out.println("Hello World");
//    }
//}

public class Main {
    public static void main(String[] args) {
        try {
            // Test if JSONObject is available
            Class<?> jsonClass = Class.forName("org.json.JSONObject");
            System.out.println("org.json.JSONObject is available!");

            // If we get here, try to use it
            Object jsonObj = jsonClass.newInstance();
            System.out.println("Can create JSONObject instance");

        } catch (ClassNotFoundException e) {
            System.out.println("org.json.JSONObject not found in classpath");
            System.out.println("You need to add the dependency or use manual methods");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}