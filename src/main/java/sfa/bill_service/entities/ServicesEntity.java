package sfa.bill_service.entities;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import sfa.bill_service.constants.Status;

import java.util.Date;

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
    String cghsLocation;
    @ManyToOne
    ServiceCategory serviceCategory;
    Float nablRate;
    Float nonNablRate;
    @Enumerated(EnumType.STRING)
    Status status;
}
