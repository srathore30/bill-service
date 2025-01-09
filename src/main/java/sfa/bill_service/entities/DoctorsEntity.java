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
public class DoctorsEntity extends BaseEntity{
    String name;
    @Enumerated(EnumType.STRING)
    Status status;
    String gender;
    String age;
    String specialization;
    String department;
    Long phoneNumber;
    String email;
}
