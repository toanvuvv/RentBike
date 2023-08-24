package app.ebr.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import app.ebr.domains.models.Bill;

@Repository
public interface BillRepository extends CrudRepository<Bill, Integer> {
    Iterable<Bill> findAllByUserId(int userId);
}
