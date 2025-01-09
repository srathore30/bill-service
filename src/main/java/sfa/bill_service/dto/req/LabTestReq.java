package sfa.bill_service.dto.req;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LabTestReq {
    Long patientId;
    Long investigationId;
    String result;
    Date datePerformed;
}
