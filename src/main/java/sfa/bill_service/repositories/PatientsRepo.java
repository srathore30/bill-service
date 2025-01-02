package sfa.bill_service.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sfa.bill_service.entities.PatientsEntity;

@Repository
public interface PatientsRepo extends JpaRepository<PatientsEntity, Long> {
    Page<PatientsEntity> findByUserId(Long userId, Pageable pageable);
}
