package sfa.bill_service.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sfa.bill_service.dto.req.RoomReq;
import sfa.bill_service.dto.res.RoomRes;
import sfa.bill_service.dto.res.PaginatedResp;
import sfa.bill_service.services.RoomServices;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomServices roomServices;

    @PostMapping
    public ResponseEntity<RoomRes> createRoom(@RequestBody RoomReq roomReq) {
        RoomRes roomRes = roomServices.createRoom(roomReq);
        return new ResponseEntity<>(roomRes, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomRes> getRoomById(@PathVariable Long id) {
        RoomRes roomRes = roomServices.getRoom(id);
        return new ResponseEntity<>(roomRes, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomById(@PathVariable Long id) {
        roomServices.deleteRoomById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomRes> updateRoom(@PathVariable Long id, @RequestBody RoomReq roomReq) {
        RoomRes roomRes = roomServices.updateRoomById(id, roomReq);
        return new ResponseEntity<>(roomRes, HttpStatus.OK);
    }

    @GetMapping("/getAllRoom")
    public ResponseEntity<PaginatedResp<RoomRes>> getAllRoom(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        PaginatedResp<RoomRes> paginatedResp = roomServices.getAllRoom(page, pageSize, sortBy, sortDirection);
        return new ResponseEntity<>(paginatedResp, HttpStatus.OK);
    }
}
