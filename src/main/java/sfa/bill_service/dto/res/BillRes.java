package sfa.bill_service.dto.res;

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
public class BillRes {
    Long id;
    Long contactNumber;
    Date date;
    Double totalAmount;
    Double paidAmount;
    BillStatus billStatus;
    PatientsRes patient;
    Status status;
    List<BillEntryRes> billEntryList;
}

