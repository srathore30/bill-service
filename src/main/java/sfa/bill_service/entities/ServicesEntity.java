package sfa.bill_service.entities;


import jakarta.persistence.Column;
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
public class ServicesEntity extends BaseEntity{
    String serviceName;
    @Column(unique = true)
    String serviceCode;
    Float nablRate;
    Float nonNablRate;
    @Enumerated(EnumType.STRING)
    Status status;
    Long userId;
}
