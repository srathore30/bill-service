package sfa.bill_service.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sfa.bill_service.entities.City;
import sfa.bill_service.entities.State;

import java.util.Optional;

@Repository
public interface StateRepository extends JpaRepository<State,Long> {
    Optional<State> findByStateName(String stateName);

}
