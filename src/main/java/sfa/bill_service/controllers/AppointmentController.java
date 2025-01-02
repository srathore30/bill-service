package sfa.bill_service.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sfa.bill_service.constants.AppointmentStatus;
import sfa.bill_service.dto.req.AppointmentReq;
import sfa.bill_service.dto.res.AppointmentRes;
import sfa.bill_service.dto.res.PaginatedResp;
import sfa.bill_service.services.AppointmentServices;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentServices appointmentServices;

    @PostMapping
    public ResponseEntity<AppointmentRes> createAppointment(@RequestBody AppointmentReq appointmentReq) {
        AppointmentRes appointmentRes = appointmentServices.createAppointment(appointmentReq);
        return new ResponseEntity<>(appointmentRes, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentRes> getAppointmentById(@PathVariable Long id) {
        AppointmentRes appointmentRes = appointmentServices.getAppointment(id);
        return new ResponseEntity<>(appointmentRes, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointmentById(@PathVariable Long id) {
        appointmentServices.deleteAppointmentById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> changeAppointmentStatus(@PathVariable Long id, @RequestParam AppointmentStatus appointmentStatus) {
        appointmentServices.changeAppointmentStatus(id, appointmentStatus);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getAllAppointmentsByUserId/{userId}")
    public ResponseEntity<PaginatedResp<AppointmentRes>> getAllAppointmentsByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "appointmentDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        PaginatedResp<AppointmentRes> paginatedResp = appointmentServices.getAllRAppointmentByUserId(userId, page, pageSize, sortBy, sortDirection);
        return new ResponseEntity<>(paginatedResp, HttpStatus.OK);
    }

    @GetMapping("/getAllAppointmentsByUserIdAndStatus/{userId}")
    public ResponseEntity<PaginatedResp<AppointmentRes>> getAllAppointmentsByUserIdAndStatus(
            @PathVariable Long userId,
            @RequestParam AppointmentStatus appointmentStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "appointmentDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        PaginatedResp<AppointmentRes> paginatedResp = appointmentServices.getAllAppointmentByUserIdAndStatus(userId, appointmentStatus, page, pageSize, sortBy, sortDirection);
        return new ResponseEntity<>(paginatedResp, HttpStatus.OK);
    }
}
