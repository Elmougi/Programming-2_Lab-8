package Users;

public class Instructor extends User {
    public Instructor(String name, String ID, String email, String password){
        super(name, ID, email, password);
        this.role = "Student";
    }
}
