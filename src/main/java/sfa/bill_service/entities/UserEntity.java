package sfa.bill_service.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sfa.bill_service.constants.Status;
import sfa.bill_service.constants.UserRole;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Table(name = "hospital_admins")
public class UserEntity extends BaseEntity {
        String name;
        String username;
        String email;
        String password;
        @Enumerated(EnumType.STRING)
        List<UserRole> userRoleList;
        @Enumerated(EnumType.STRING)
        Status userStatus;
        Long mobileNo;
}