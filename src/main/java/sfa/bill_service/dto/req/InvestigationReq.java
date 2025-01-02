package sfa.bill_service.dto.req;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvestigationReq {
    String investigationName;
    String description;
    String cost;
    Long userId;
}
