package sfa.bill_service.dto.res;

import lombok.*;
import lombok.experimental.FieldDefaults;
import sfa.bill_service.constants.AppointmentStatus;
import sfa.bill_service.entities.DoctorsEntity;
import sfa.bill_service.entities.PatientsEntity;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentRes {
    DoctorsEntity doctor;
    PatientsEntity patient;
    Long id;
    Long userId;
    Date appointmentDate;
    AppointmentStatus appointmentStatus;
}
