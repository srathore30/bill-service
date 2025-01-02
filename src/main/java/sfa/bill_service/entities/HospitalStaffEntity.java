package sfa.bill_service.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;
import sfa.bill_service.constants.Status;
import sfa.bill_service.constants.UserRole;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HospitalStaffEntity extends BaseEntity{
    String staffName;
    @Enumerated(EnumType.STRING)
    Status status;
    String gender;
    String age;
    String department;
    String designation;
    @Enumerated(EnumType.STRING)
    List<UserRole> userRoleList;
    Long userId;
}
