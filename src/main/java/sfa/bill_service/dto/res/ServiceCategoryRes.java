package sfa.bill_service.dto.res;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceCategoryRes {
    Long id;
    String name;
    String cghsLocation;
    Date lastUpdated;
}
