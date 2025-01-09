package sfa.bill_service.dto.req;

import lombok.*;
import lombok.experimental.FieldDefaults;
import sfa.bill_service.constants.UserRole;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientsReq {
    List<UserRole> userRoleList;
    String name;
    String gender;
    String age;
    String address;
    Long contactNumber;
    String email;
    String bloodGroup;
    String allergies;
    Long emergencyContactNumber;
    String emergencyContactName;
}
