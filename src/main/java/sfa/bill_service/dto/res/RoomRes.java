package sfa.bill_service.dto.res;

import lombok.*;
import lombok.experimental.FieldDefaults;
import sfa.bill_service.constants.RoomType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomRes {
    RoomType roomType;
    boolean isOccupied;
    Long id;
}
