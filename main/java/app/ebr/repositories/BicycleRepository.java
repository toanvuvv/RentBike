package app.ebr.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import app.ebr.domains.models.Bicycle;

@Repository
public interface BicycleRepository extends CrudRepository<Bicycle, Integer> {

}
