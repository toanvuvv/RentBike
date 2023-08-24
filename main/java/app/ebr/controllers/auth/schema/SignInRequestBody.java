package app.ebr.controllers.auth.schema;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class SignInRequestBody {
    @Email
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    public SignInRequestBody() {
    }

    public SignInRequestBody(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
