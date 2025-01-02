package sfa.bill_service.dto.res;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicationRes {
    String medicineName;
    String description;
    String dosage;
    Long id;
}
