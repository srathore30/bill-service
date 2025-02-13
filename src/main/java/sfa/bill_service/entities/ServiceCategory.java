package sfa.bill_service.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.checkerframework.checker.units.qual.A;
import sfa.bill_service.constants.Status;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class ServiceCategory extends BaseEntity{
    String name;
    @Enumerated(EnumType.STRING)
    Status status;
}
