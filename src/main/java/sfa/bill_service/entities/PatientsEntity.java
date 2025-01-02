package sfa.bill_service.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;
import sfa.bill_service.constants.Status;
import sfa.bill_service.constants.UserRole;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class PatientsEntity extends BaseEntity{
    @Enumerated(EnumType.STRING)
    Status status;
    @Enumerated(EnumType.STRING)
    List<UserRole> userRoleList;
    String name;
    String gender;
    String age;
    String address;
    Long contactNumber;
    String email;
    String bloodGroup;
    String allergies;
    Long userId;
    Long emergencyContactNumber;
    String emergencyContactName;

}
