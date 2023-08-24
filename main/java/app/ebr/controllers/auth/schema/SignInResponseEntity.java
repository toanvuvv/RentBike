package app.ebr.controllers.auth.schema;

public class SignInResponseEntity {

    private String jwt;
    private long id;
    private String email;

    public SignInResponseEntity(String jwt, long id, String email) {
        this.jwt = jwt;
        this.id = id;
        this.email = email;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
