package app.ebr.controllers.parkinglot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.ebr.domains.models.Bicycle;
import app.ebr.domains.models.ParkingLot;
import app.ebr.repositories.ParkingLotRepository;

@RestController
@RequestMapping(value = "/api/parking_lots")
public class ParkingLotController {

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @GetMapping(value = "/")
    public ResponseEntity<List<ParkingLot>> getAll() {
        Iterable<ParkingLot> parkingLots = this.parkingLotRepository.findAll();
        List<ParkingLot> _parkingLots = new ArrayList<>();
        for (ParkingLot parkingLot : parkingLots) {
            for (Bicycle bicycle : parkingLot.getBicycles()) {
                bicycle.setParkingLot(null);
                if (bicycle.getUser() != null) {
                    bicycle.getUser().setBicycles(null);
                    bicycle.getUser().setBills(null);
                    bicycle.getUser().setPassword("");
                }
            }
            _parkingLots.add(parkingLot);
        }
        return new ResponseEntity<List<ParkingLot>>(_parkingLots, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ParkingLot> get(@PathVariable(required = true) int id) {
        Optional<ParkingLot> parkingLot = this.parkingLotRepository.findById(id);
        if (parkingLot.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ParkingLot _parkingLot = parkingLot.get();
        for (Bicycle bicycle : _parkingLot.getBicycles()) {
            bicycle.setParkingLot(null);
            if (bicycle.getUser() != null) {
                bicycle.getUser().setBicycles(null);
                bicycle.getUser().setBills(null);
                bicycle.getUser().setPassword("");
            }
        }
        return new ResponseEntity<ParkingLot>(_parkingLot, HttpStatus.OK);
    }
}
