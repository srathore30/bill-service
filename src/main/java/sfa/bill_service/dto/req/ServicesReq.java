package sfa.bill_service.dto.req;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServicesReq {
    String serviceName;
    String serviceCode;
    Long categoryId;
    String cghsLocation;
    Float nablRate;
    Float nonNablRate;
}
