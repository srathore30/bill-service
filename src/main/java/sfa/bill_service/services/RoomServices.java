package sfa.bill_service.services;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sfa.bill_service.constants.ApiErrorCodes;
import sfa.bill_service.constants.Status;
import sfa.bill_service.dto.req.RoomReq;
import sfa.bill_service.dto.res.RoomRes;
import sfa.bill_service.dto.res.PaginatedResp;
import sfa.bill_service.entities.RoomEntity;
import sfa.bill_service.exceptions.NoSuchElementFoundException;
import sfa.bill_service.repositories.RoomRepo;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServices {

    private final RoomRepo roomRepo;

    public RoomRes createRoom(RoomReq roomReq){
        RoomEntity roomEntity = mapToEntity(roomReq);
        return mapToDto(roomRepo.save(roomEntity));
    }

    public RoomRes getRoom(Long id){
        Optional<RoomEntity> optionalRoomEntity = roomRepo.findById(id);
        if(optionalRoomEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.ROOM_NOT_FOUND.getErrorCode(), ApiErrorCodes.ROOM_NOT_FOUND.getErrorMessage());
        }
        return mapToDto(optionalRoomEntity.get());
    }

    public RoomRes updateRoomById(Long id, RoomReq investigationReq){
        Optional<RoomEntity> optionalRoomEntity = roomRepo.findById(id);
        if(optionalRoomEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.ROOM_NOT_FOUND.getErrorCode(), ApiErrorCodes.ROOM_NOT_FOUND.getErrorMessage());
        }
        updateEntityFromDto(optionalRoomEntity.get(), investigationReq);
        return mapToDto(roomRepo.save(optionalRoomEntity.get()));
    }

    public void deleteRoomById(Long id){
        Optional<RoomEntity> optionalRoomEntity = roomRepo.findById(id);
        if(optionalRoomEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.ROOM_NOT_FOUND.getErrorCode(), ApiErrorCodes.ROOM_NOT_FOUND.getErrorMessage());
        }
        optionalRoomEntity.get().setStatus(Status.InActive);
        roomRepo.save(optionalRoomEntity.get());
    }

    public PaginatedResp<RoomRes> getAllRoom(int page, int pageSize, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<RoomEntity> roomEntityPage = roomRepo.findAll(pageable);
        List<RoomRes> roomResList;
        roomResList = roomEntityPage.getContent().stream().filter(roomEntity -> roomEntity.getStatus() == Status.Active).map(this::mapToDto).toList();
        return new PaginatedResp<>(roomEntityPage.getTotalElements(), roomEntityPage.getTotalPages(), page, roomResList);
    }

    private RoomEntity mapToEntity(RoomReq req){
        RoomEntity entity = new RoomEntity();
        entity.setOccupied(false);
        entity.setStatus(Status.Active);
        entity.setRoomType(req.getRoomType());
        return entity;
    }

    private void updateEntityFromDto(RoomEntity entity, RoomReq req){
        entity.setRoomType(req.getRoomType());
    }

    private RoomRes mapToDto(RoomEntity entity){
        RoomRes res = new RoomRes();
        res.setId(entity.getId());
        res.setRoomType(entity.getRoomType());
        return res;
    }
}
