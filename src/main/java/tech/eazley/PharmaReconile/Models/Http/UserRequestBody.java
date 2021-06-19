package tech.eazley.PharmaReconile.Models.Http;

public class UserRequestBody {
    private String email;
    private String password;
    private String userName;
    public UserRequestBody()
    {

    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }
}
