package sfa.bill_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sfa.bill_service.constants.Status;
import sfa.bill_service.entities.BillEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepo extends JpaRepository<BillEntity,Long> {

    List<BillEntity> findByContactNumber(Long contactNumber);
    Optional<BillEntity> findByPatientIdAndStatus(Long patientId, Status status);


}
