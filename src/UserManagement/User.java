package UserManagement;

import Database.DataInfo;
import Utilities.Validation;

public abstract class User implements DataInfo {
    protected String name;
    protected String ID;
    protected String email;
    protected String passwordHash;
    protected String role;

    public User(String name, String ID, String email, String password) {
        setName(name);
        setID(ID);
        setEmail(email);
        setPasswordHash(password);
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

    public void setPasswordHash(String passwordHash){
        if(!Validation.isValidString(passwordHash)){
            throw new IllegalArgumentException("Invalid password format.");
        }
        System.out.println(passwordHash);
        this.passwordHash = passwordHash;
    }

    public String getPasswordHash(){
        return this.passwordHash;
    }

    public String getRole(){
        return this.role;
    }




}
