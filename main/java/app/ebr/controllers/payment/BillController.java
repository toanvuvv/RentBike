package app.ebr.controllers.payment;

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

import app.ebr.domains.models.Bill;
import app.ebr.repositories.BillRepository;

@RestController
@RequestMapping(value = "/api/bills")
public class BillController {

    @Autowired
    private BillRepository billRepository;

    @GetMapping(value = "/")
    public ResponseEntity<List<Bill>> getAll() {
        Iterable<Bill> bills = this.billRepository.findAll();
        List<Bill> _bills = new ArrayList<>();
        for (Bill bill : bills) {
            _bills.add(bill);
        }
        return new ResponseEntity<List<Bill>>(_bills, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Bill> get(@PathVariable(required = true) int id) {
        Optional<Bill> bill = this.billRepository.findById(id);
        if (bill.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Bill>(bill.get(), HttpStatus.OK);
    }

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<List<Bill>> getOfUser(@PathVariable(required = true) int id) {
        Iterable<Bill> bills = this.billRepository.findAllByUserId(id);
        List<Bill> _bills = new ArrayList<>();
        for (Bill bill : bills) {
            _bills.add(bill);
            bill.getUser().setBills(null);
            bill.getUser().setBicycles(null);
            bill.getUser().setPassword("");
            bill.getBicycle().setParkingLot(null);
            bill.getBicycle().setUser(null);
        }
        return new ResponseEntity<List<Bill>>(_bills, HttpStatus.OK);
    }
}
