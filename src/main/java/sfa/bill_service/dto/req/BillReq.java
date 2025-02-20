package sfa.bill_service.dto.req;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BillReq {
    Long patientId;
    Long roomId;
    Date admissionDate;
    Date dischargeDate;
    String referBy;
}
