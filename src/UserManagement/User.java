package UserManagement;

import Database.DataInfo;
import Utilities.Validation;
import Database.JsonDatabaseManager;

public abstract class User implements DataInfo {
    private String name;
    private String ID;
    private String email;
    private String password;
    protected String role;

    public User(String name, String ID, String email, String password) {
        setName(name);
        setID(ID);
        setEmail(email);
        setPassword(password);
    }

    public void setName(String name) {
        if(!Validation.isValidString(name)){
            throw new IllegalArgumentException("Invalid name format.");
        }

        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getSearchKey(){
        return this.ID;
    }

    public void setID(String ID){
        if(!Validation.isValidString(ID)){
            throw new IllegalArgumentException("Invalid ID format.");
        }

        this.ID = ID;
    }

    public String getEmail(){
        return this.email;
    }

    public void setEmail(String email){
        if(!Validation.isValidEmail(email)){
            throw new IllegalArgumentException("Invalid email format.");
        }

        this.email = email;
    }

    public void setPassword(String password){
        if(!Validation.isValidString(password)){
            throw new IllegalArgumentException("Invalid password format.");
        }

        this.password = password;
    }

    public String getPassword(){
        return this.password;
    }

    public String getRole(){
        return this.role;
    }




}
