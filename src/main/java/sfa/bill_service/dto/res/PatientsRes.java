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
public class PatientsRes {
    List<UserRole> userRoleList;
    String name;
    String gender;
    String age;
    Long id;
    String address;
    Long contactNumber;
    String email;
    String bloodGroup;
    String allergies;
    Long userId;
    Long emergencyContactNumber;
    String emergencyContactName;
}
