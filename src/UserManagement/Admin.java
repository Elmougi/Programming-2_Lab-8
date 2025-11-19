package UserManagement;

public class Admin extends User {
    public Admin(String name, String ID, String email, String password) {
        super(name, ID, email, password);
        this.role = "Admin";
    }


}
