package sfa.bill_service.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sfa.bill_service.constants.AppointmentStatus;
import sfa.bill_service.entities.AppointmentEntity;

@Repository
public interface AppointmentRepo extends JpaRepository<AppointmentEntity, Long> {
    Page<AppointmentEntity> findByAppointmentStatus(AppointmentStatus appointmentStatus, Pageable pageable);
}
