package Users;

public class Student extends User{
    public Student(String name, String ID, String email, String password){
        super(name, ID, email, password);
        this.role = "Student";
    }
}
