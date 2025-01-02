package sfa.bill_service.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;
import sfa.bill_service.constants.AppointmentStatus;
import sfa.bill_service.constants.Status;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class AppointmentEntity extends BaseEntity{
    @ManyToOne
    @JsonBackReference
    PatientsEntity patient;

    @ManyToOne
    @JsonBackReference
    DoctorsEntity doctor;

    Long userId;
    Date appointmentDate;

    @Enumerated(EnumType.STRING)
    Status status;
    @Enumerated
    AppointmentStatus appointmentStatus;
}
