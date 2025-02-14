package sfa.bill_service.dto.req;

import lombok.*;
import lombok.experimental.FieldDefaults;
import sfa.bill_service.constants.BillStatus;
import sfa.bill_service.constants.Status;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BillReq {
    Long contactNumber;
    Date date;
    Double totalAmount;
    Double paidAmount;
    BillStatus billStatus;
    Long patientId;
    Status status;
    List<BillEntryReq> billEntryList;
}

