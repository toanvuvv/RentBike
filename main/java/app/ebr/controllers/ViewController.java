package app.ebr.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import app.ebr.domains.enums.BicycleType;
import app.ebr.domains.models.Bicycle;
import app.ebr.domains.models.ParkingLot;
import app.ebr.repositories.BicycleRepository;
import app.ebr.repositories.ParkingLotRepository;

@Controller
public class ViewController {

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Autowired
    private BicycleRepository bicycleRepository;

    @RequestMapping(value = "/", method = { RequestMethod.GET })
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/signin", method = { RequestMethod.GET })
    public String signin() {
        return "signin";
    }

    @RequestMapping(value = "/signup", method = { RequestMethod.GET })
    public String signup() {
        return "signup";
    }

    @RequestMapping(value = "/payment/{id}", method = { RequestMethod.GET })
    public String payment(@RequestParam(required = true) long id) {
        return "payment";
    }

    @RequestMapping(value = "/initial", method = { RequestMethod.GET })
    public ResponseEntity<?> initial() {
        ParkingLot parkingLot = this.parkingLotRepository.save(new ParkingLot("Trung Kinh, Cau Giay, Ha Noi"));
        Bicycle bicycle1 = new Bicycle(BicycleType.COUPLE, "29 - B3 23426");
        bicycle1.setParkingLot(parkingLot);
        Bicycle bicycle2 = new Bicycle(BicycleType.ELECTRONIC, "29 - B3 67433");
        bicycle2.setParkingLot(parkingLot);
        Bicycle bicycle3 = new Bicycle(BicycleType.NORMAL, "29 - B3 43785");
        bicycle3.setParkingLot(parkingLot);
        Bicycle bicycle4 = new Bicycle(BicycleType.COUPLE, "29 - B3 43647");
        bicycle4.setParkingLot(parkingLot);
        Bicycle bicycle5 = new Bicycle(BicycleType.NORMAL, "29 - B3 74745");
        bicycle5.setParkingLot(parkingLot);
        List<Bicycle> bicycles = new ArrayList<>();
        bicycles.add(bicycle1);
        bicycles.add(bicycle2);
        bicycles.add(bicycle3);
        bicycles.add(bicycle4);
        bicycles.add(bicycle5);
        this.bicycleRepository.saveAll(bicycles);

        ParkingLot parkingLot1 = this.parkingLotRepository.save(new ParkingLot("Quan Nhan, Nhan Chinh, Ha Noi"));
        Bicycle bicycle6 = new Bicycle(BicycleType.COUPLE, "29 - B3 34534");
        bicycle6.setParkingLot(parkingLot1);
        Bicycle bicycle7 = new Bicycle(BicycleType.ELECTRONIC, "29 - B3 79534");
        bicycle7.setParkingLot(parkingLot1);
        Bicycle bicycle8 = new Bicycle(BicycleType.NORMAL, "29 - B3 43566");
        bicycle8.setParkingLot(parkingLot1);
        Bicycle bicycle9 = new Bicycle(BicycleType.COUPLE, "29 - B3 12432");
        bicycle9.setParkingLot(parkingLot1);
        Bicycle bicycle10 = new Bicycle(BicycleType.NORMAL, "29 - B3 07465");
        bicycle10.setParkingLot(parkingLot1);
        List<Bicycle> bicycles1 = new ArrayList<>();
        bicycles1.add(bicycle6);
        bicycles1.add(bicycle7);
        bicycles1.add(bicycle8);
        bicycles1.add(bicycle9);
        bicycles1.add(bicycle10);
        this.bicycleRepository.saveAll(bicycles1);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
