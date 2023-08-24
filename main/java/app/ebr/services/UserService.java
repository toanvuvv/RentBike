package app.ebr.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.ebr.domains.models.User;
import app.ebr.exceptions.AssetExisted;
import app.ebr.exceptions.NotFound;
import app.ebr.exceptions.PasswordIsWrong;
import app.ebr.repositories.UserRepository;
import app.ebr.services.interfaces.IUserService;
import at.favre.lib.crypto.bcrypt.BCrypt;

@Service
public class UserService implements IUserService {

    @Autowired
    UserRepository userRepository;

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Override
    public void signInWithCertificate(String email, String password) {
        Optional<User> user = this.userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new NotFound();
        }
        logger.debug(user.toString());
        if (!BCrypt.verifyer(BCrypt.Version.VERSION_2X).verify(password.toCharArray(),
                user.get().getPassword().getBytes()).verified) {
            throw new PasswordIsWrong();
        }
    }

    @Override
    public void signUpWithCertificate(String email, String password) {
        Optional<User> user = this.userRepository.findByEmail(email.toLowerCase());
        if (!user.isEmpty()) {
            logger.debug(user.toString());
            throw new AssetExisted();
        }
        this.userRepository.save(new User(email.toLowerCase(), password));
    }

    @Override
    public User viewUserByEmail(String email) {
        Optional<User> user = this.userRepository.findByEmail(email.toLowerCase());
        if (user.isEmpty()) {
            return null;
        }
        return user.get();
    }

    @Override
    public User viewUserById(int id) {
        Optional<User> user = this.userRepository.findById(id);
        if (user.isEmpty()) {
            return null;
        }
        return user.get();
    }

}
