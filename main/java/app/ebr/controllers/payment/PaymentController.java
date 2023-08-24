package app.ebr.controllers.payment;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import app.ebr.domains.enums.BillStatus;
import app.ebr.domains.models.Bill;
import app.ebr.domains.models.User;
import app.ebr.repositories.BillRepository;
import app.ebr.repositories.UserRepository;
import app.ebr.services.UserService;

@RestController
@RequestMapping(value = "/api/payments")
public class PaymentController {
    @Autowired
    private UserService userService;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = "/{id}")
    public ResponseEntity<?> pay(
            @RequestHeader(required = true, name = HttpHeaders.AUTHORIZATION) String authorization,
            @PathVariable(required = true) int id) {
        String[] _authorization = authorization.split(" ");
        if (_authorization.length != 2 && !_authorization[0].equals("Bearer")) {
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
            /// Replace
            Optional<Bill> bill = this.billRepository.findById(id);
            if (bill.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Bill _bill = bill.get();
            if (_bill.getUser().getId() != user.getId() || _bill.getStatus() == BillStatus.PAID) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            float money = user.getMoney();
            if (_bill.getTotal() > user.getMoney()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                user.setMoney(money - _bill.getTotal());
                this.userRepository.save(user);
            }
            _bill.setStatus(BillStatus.PAID);
            _bill.setPaidAt(new Date());
            this.billRepository.save(_bill);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
