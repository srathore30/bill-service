package sfa.bill_service.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sfa.bill_service.dto.res.UserRes;
import sfa.bill_service.entities.MedicationEntity;

@Repository
public interface MedicationRepo extends JpaRepository<MedicationEntity, Long> {
}
