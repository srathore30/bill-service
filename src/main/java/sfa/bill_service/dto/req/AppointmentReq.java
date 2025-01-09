package sfa.bill_service.dto.req;

import lombok.*;
import lombok.experimental.FieldDefaults;
import sfa.bill_service.constants.AppointmentStatus;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentReq {
    Long doctorId;
    Long patientId;
    Date appointmentDate;
    AppointmentStatus appointmentStatus;
}
