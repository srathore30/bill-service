package sfa.bill_service.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import sfa.bill_service.constants.BillStatus;
import sfa.bill_service.constants.Status;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BillEntryEntity extends BaseEntity{
    Long contactNumber;
    @Temporal(TemporalType.DATE)
    Date date;
    Double totalAmount;
    Double paidAmount;
    @Enumerated(EnumType.STRING)
    BillStatus billStatus;
    @ManyToOne
    PatientsEntity patient;
    @Enumerated(EnumType.STRING)
    Status status;
    @OneToMany(cascade = CascadeType.ALL)
    List<ServicesEntity> serviceEntityList;
    @ManyToOne
    BillEntity bill;

}
