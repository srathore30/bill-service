package sfa.bill_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sfa.bill_service.entities.ServiceCategory;

import java.util.Optional;

@Repository
public interface ServiceCategoryRepo extends JpaRepository<ServiceCategory, Long> {
    Optional<ServiceCategory> findByName(String name);
}
