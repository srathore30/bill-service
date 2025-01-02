package sfa.bill_service.dto.res;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DoctorRes {
    String name;
    String gender;
    String age;
    String specialization;
    String department;
    Long phoneNumber;
    String email;
    String availability;
    Long id;
    Long userId;
}
