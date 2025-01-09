package sfa.bill_service.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sfa.bill_service.constants.LabTestStatus;
import sfa.bill_service.constants.Status;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LabTestEntity extends BaseEntity{
    @ManyToOne
    @JsonBackReference
    PatientsEntity patient;

    @ManyToOne
    @JsonBackReference
    InvestigationEntity investigation;

    @Enumerated(EnumType.STRING)
    LabTestStatus labTestStatus;

    @Enumerated(EnumType.STRING)
    Status status;
    String result;
    Date datePerformed;
}
