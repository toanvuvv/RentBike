package app.ebr.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import app.ebr.domains.models.ParkingLot;

@Repository
public interface ParkingLotRepository extends CrudRepository<ParkingLot, Integer> {

}
