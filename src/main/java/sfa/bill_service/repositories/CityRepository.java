package sfa.bill_service.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sfa.bill_service.entities.City;
import sfa.bill_service.entities.State;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City,Long> {
    Page<City> findByStateId(Long stateId, Pageable pageable);
    Optional<City> findByCityName(String cityName);
}
