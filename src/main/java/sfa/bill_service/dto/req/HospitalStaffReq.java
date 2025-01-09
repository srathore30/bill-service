package sfa.bill_service.dto.req;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.w3c.dom.stylesheets.LinkStyle;
import sfa.bill_service.constants.Status;
import sfa.bill_service.constants.UserRole;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HospitalStaffReq {
    String staffName;
    String gender;
    String age;
    String department;
    String designation;
    List<UserRole> userRole;
}
