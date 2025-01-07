package sfa.bill_service.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sfa.bill_service.constants.LabTestStatus;
import sfa.bill_service.entities.LabTestEntity;

@Repository
public interface LabTestRepo extends JpaRepository<LabTestEntity, Long> {
    Page<LabTestEntity> findByUserId(Long userId, Pageable pageable);
    Page<LabTestEntity> findByUserIdAndLabTestStatus(Long userId, LabTestStatus labTestStatus, Pageable pageable);
}
