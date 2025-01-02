package sfa.bill_service.dto.res;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvestigationRes {
    String investigationName;
    String description;
    String cost;
    Long id;
    Long userId;
}
