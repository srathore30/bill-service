package sfa.bill_service.dto.req;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServicesReq {
    String serviceName;
    String serviceCode;
    Float nablRate;
    Float nonNablRate;
}
