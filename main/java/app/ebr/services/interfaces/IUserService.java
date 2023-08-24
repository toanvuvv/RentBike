package app.ebr.services.interfaces;

import app.ebr.domains.models.User;

public interface IUserService {

    void signInWithCertificate(String email, String password);

    void signUpWithCertificate(String email, String password);

    User viewUserByEmail(String email);

    User viewUserById(int id);

}
