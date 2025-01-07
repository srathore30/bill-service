package sfa.bill_service.dto.res;

import lombok.*;
import lombok.experimental.FieldDefaults;
import sfa.bill_service.constants.Status;
import sfa.bill_service.constants.UserRole;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRes {
    String name;
    Long id;
    String username;
    String email;
    Status userStatus;
    Long mobileNo;
}
