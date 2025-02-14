package sfa.bill_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sfa.bill_service.constants.Status;
import sfa.bill_service.entities.BillEntity;

import java.util.List;

@Repository
public interface BillRepo extends JpaRepository<BillEntity,Long> {

    List<BillEntity> findByContactNumber(Long contactNumber);
    boolean existsByPatientId(Long patientId);

}
