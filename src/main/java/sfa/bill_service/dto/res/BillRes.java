package sfa.bill_service.dto.res;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BillRes {
    PatientsRes patientsRes;
    RoomRes roomRes;
    Date admissionDate;
    Date dischargeDate;
    String billNo;

    String referBy;
    Double totalAmount;
    Double paidAmount;
    Long id;
    List<BillEntryRes> billEntryResList;
}
