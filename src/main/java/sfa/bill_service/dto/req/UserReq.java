package sfa.bill_service.dto.req;

import lombok.*;
import lombok.experimental.FieldDefaults;
import sfa.bill_service.constants.Status;
import sfa.bill_service.constants.UserRole;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserReq {
    String name;
    String username;
    String password;
    String email;
    Long mobileNo;
}
