package sfa.bill_service.dto.res;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServicesRes {
    String serviceName;
    Long id;
    String serviceCode;
    Float nablRate;
    Float nonNablRate;
    Long userId;
}
