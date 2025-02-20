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
    String referBy;

    String billNo;
    Date admissionDate;
    Date dischargeDate;
    Double totalAmount;
    Double paidAmount;
    @Enumerated(EnumType.STRING)
    BillStatus billStatus;

    @ManyToOne
    RoomEntity roomEntity;

    @ManyToOne
    PatientsEntity patient;
    @Enumerated(EnumType.STRING)
    Status status;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL)
    List<BillEntryEntity> billEntries;
}
