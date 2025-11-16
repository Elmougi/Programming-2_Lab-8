package Users;

import database.DataInfo;

public abstract class User implements DataInfo {
    private String name;
    private String ID;
    private String email;
    private String password;
    protected String role;

    public User(String name, String ID, String email, String password) {
        this.name = name;
        this.ID = ID;
        this.email = email;
        this.password = password;
    }

    public void setName(String name) {
        

        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getSearchKey(){
        return this.ID;
    }

    public void changeID(String ID){
        this.ID = ID;
    }

    public String getEmail(){
        return this.email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return this.password;
    }

    public String getRole(){
        return this.role;
    }




}
