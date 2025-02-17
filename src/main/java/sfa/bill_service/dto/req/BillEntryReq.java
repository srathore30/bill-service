package sfa.bill_service.dto.req;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BillEntryReq {
    List<Long> serviceIds;
    Long billId;
}

