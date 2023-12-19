package firebase.gopool.model.response;

import java.io.Serializable;

public class LoginResponse implements Serializable {

    private String jwt;
    /*
        1: admin
        2: driver
        3: passenger
     */
    private int role;
    private String fullName;

    public LoginResponse(String jwt, int role, String fullName) {
        this.jwt = jwt;
        this.role = role;
        this.fullName = fullName;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
