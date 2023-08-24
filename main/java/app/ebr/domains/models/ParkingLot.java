package app.ebr.domains.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "parking_lot")
public class ParkingLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "location")
    private String location;

    @OneToMany(mappedBy = "parkingLot", targetEntity = Bicycle.class)
    private List<Bicycle> bicycles;

    public ParkingLot() {

    }

    public ParkingLot(String location) {
        this.location = location;
    }

    public ParkingLot(String location, List<Bicycle> bicycles) {
        this.location = location;
        this.bicycles = bicycles;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setBicycles(List<Bicycle> bicycles) {
        this.bicycles = bicycles;
    }

    public List<Bicycle> getBicycles() {
        return bicycles;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

}
