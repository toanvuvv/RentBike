package app.ebr.controllers.auth;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import app.ebr.controllers.auth.schema.MeResponseEntity;
import app.ebr.controllers.auth.schema.SignInRequestBody;
import app.ebr.controllers.auth.schema.SignInResponseEntity;
import app.ebr.controllers.auth.schema.SignUpRequestBody;
import app.ebr.domains.models.User;
import app.ebr.services.UserService;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @PostMapping(value = "/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequestBody body) {
        try {
            this.userService.signUpWithCertificate(body.getEmail(), body.getPassword());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/sign-in")
    public ResponseEntity<SignInResponseEntity> signIn(@Valid @RequestBody SignInRequestBody body) {
        try {
            this.userService.signInWithCertificate(body.getEmail(), body.getPassword());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = this.userService.viewUserByEmail(body.getEmail());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<SignInResponseEntity>(
                new SignInResponseEntity(user.encodeJwt(), user.getId(), user.getEmail()), HttpStatus.OK);
    }

    @GetMapping(value = "/me")
    public ResponseEntity<MeResponseEntity> me(
            @RequestHeader(required = true, name = HttpHeaders.AUTHORIZATION) String authorization) {
        String[] _authorization = authorization.split(" ");
        if (_authorization.length != 2 || !_authorization[0].equals("Bearer")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Algorithm algorithm = Algorithm.HMAC256("secret");
        JWTVerifier verifier = JWT.require(algorithm).build();
        try {
            DecodedJWT decodedJWT = verifier.verify(_authorization[1]);
            int uuid = decodedJWT.getClaim("uuid").asInt();
            User user = this.userService.viewUserById(uuid);
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<MeResponseEntity>(new MeResponseEntity(uuid, user.getEmail()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<MeResponseEntity>(HttpStatus.UNAUTHORIZED);
        }
    }

}
