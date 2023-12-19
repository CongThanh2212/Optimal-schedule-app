package firebase.gopool.model.request;

import java.io.Serializable;

public class PasswordRequest implements Serializable {

    private String email;
    private String newPasswrod;

    public PasswordRequest(String email, String newPasswrod) {
        this.email = email;
        this.newPasswrod = newPasswrod;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNewPasswrod() {
        return newPasswrod;
    }

    public void setNewPasswrod(String newPasswrod) {
        this.newPasswrod = newPasswrod;
    }
}
