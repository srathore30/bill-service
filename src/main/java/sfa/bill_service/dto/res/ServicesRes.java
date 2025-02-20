package sfa.bill_service.dto.res;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServicesRes {
    String serviceName;
    Long id;
    String serviceCode;
    Date modifiedDate;
    Float nablRate;
    Float nonNablRate;
    String cghsLocation;
    String categoryName;
    Date createdTime;
}
