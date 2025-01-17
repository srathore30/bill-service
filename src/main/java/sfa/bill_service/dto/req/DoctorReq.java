package sfa.bill_service.dto.req;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DoctorReq {
    String name;
    String gender;
    String age;
    String specialization;
    String department;
    Long phoneNumber;
    String email;
}
