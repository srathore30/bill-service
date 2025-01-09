package sfa.bill_service.dto.res;

import lombok.*;
import lombok.experimental.FieldDefaults;
import sfa.bill_service.constants.UserRole;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HospitalStaffRes {
    String staffName;
    String department;
    String designation;
    String gender;
    String age;
    Long id;
    List<UserRole> userRole;
}
