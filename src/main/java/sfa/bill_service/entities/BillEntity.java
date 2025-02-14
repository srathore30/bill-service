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
public class BillEntity extends BaseEntity{
    Long contactNumber;

    @Temporal(TemporalType.DATE)
    Date date;

    Double totalAmount;
    Double paidAmount;

    BillStatus billStatus;

    @ManyToOne
    PatientsEntity patient;

    Status status;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL)
    List<BillEntryEntity> billEntryList;
}
