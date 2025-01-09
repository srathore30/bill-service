package sfa.bill_service.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;
import sfa.bill_service.constants.RoomType;
import sfa.bill_service.constants.Status;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomEntity extends BaseEntity{
    @Enumerated(EnumType.STRING)
    RoomType roomType;
    @Enumerated(EnumType.STRING)
    Status status;
    boolean isOccupied;
}
