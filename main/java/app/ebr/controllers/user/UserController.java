package app.ebr.controllers.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import app.ebr.domains.enums.BicycleType;
import app.ebr.domains.models.Bicycle;
import app.ebr.domains.models.Bill;
import app.ebr.domains.models.ParkingLot;
import app.ebr.domains.models.User;
import app.ebr.repositories.BicycleRepository;
import app.ebr.repositories.BillRepository;
import app.ebr.repositories.ParkingLotRepository;
import app.ebr.repositories.UserRepository;
import app.ebr.services.UserService;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BicycleRepository bicycleRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Autowired
    private UserService userService;

    @GetMapping(value = "/")
    public ResponseEntity<List<User>> getAll() {
        Iterable<User> users = this.userRepository.findAll();
        List<User> _users = new ArrayList<>();
        for (User user : users) {
            _users.add(user);
        }
        return new ResponseEntity<List<User>>(_users, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> get(@PathVariable(required = true) int id) {
        Optional<User> user = this.userRepository.findById(id);
        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        for (Bicycle bicycle : user.get().getBicycles()) {
            bicycle.setUser(null);
            bicycle.setParkingLot(null);
        }
        user.get().setBills(null);
        return new ResponseEntity<User>(user.get(), HttpStatus.OK);
    }

    @PostMapping(value = "/bicycle/{id}/return/{parking_lot_id}")
    public ResponseEntity<?> bicycleReturn(
            @RequestHeader(required = true, name = HttpHeaders.AUTHORIZATION) String authorization,
            @PathVariable(required = true) int id,
            @PathVariable(required = true, name = "parking_lot_id") int parkingLotId) {
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
            Optional<ParkingLot> newParkingLot = this.parkingLotRepository.findById(parkingLotId);
            if (newParkingLot.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            ParkingLot _newParkingLot = newParkingLot.get();
            /// Replace
            Optional<Bicycle> bicycle = this.bicycleRepository.findById(id);
            if (bicycle.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Bicycle _bicycle = bicycle.get();
            if (_bicycle.getUser() == null && _bicycle.getUser().getId() != user.getId()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Date timeEnded = new Date();
            Date timeStarted = _bicycle.getTimeStarted();
            long totalMinutes = ((timeEnded.getTime() - timeStarted.getTime()) / 1000) / 60;
            float total = 0f;
            if (totalMinutes <= 10) {
                total = 0;
            } else if (10 < totalMinutes && totalMinutes < 30) {
                total = 10000;
            } else {
                total = 10000 + Math.round((totalMinutes - 30) / 15) * 3000;
            }
            if (_bicycle.getBicycleType() != BicycleType.NORMAL) {
                total *= 1.5f;
            }

            // New bill
            this.billRepository.save(new Bill(user, _bicycle, total, timeStarted, timeEnded));
            _bicycle.setUser(null);
            _bicycle.setTimeStarted(null);
            _bicycle.setParkingLot(_newParkingLot);
            this.bicycleRepository.save(_bicycle);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
