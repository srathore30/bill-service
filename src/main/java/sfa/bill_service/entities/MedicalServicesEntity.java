package sfa.bill_service.entities;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import sfa.bill_service.constants.Status;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicalServicesEntity extends BaseEntity{
    String serviceName;
    String serviceCode;
    @ManyToOne
    ServiceCategory serviceCategory;
    Float nablRate;
    Float nonNablRate;
    @Enumerated(EnumType.STRING)
    Status status;
}
