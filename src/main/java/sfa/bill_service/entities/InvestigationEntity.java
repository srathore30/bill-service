package sfa.bill_service.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;
import sfa.bill_service.constants.Status;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvestigationEntity extends BaseEntity{
    String investigationName;
    @Enumerated(EnumType.STRING)
    Status status;
    String description;
    String cost;
    Long userId;
}
