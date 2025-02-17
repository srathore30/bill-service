package sfa.bill_service.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sfa.bill_service.constants.AppointmentStatus;
import sfa.bill_service.constants.UserRole;
import sfa.bill_service.dto.req.AppointmentReq;
import sfa.bill_service.dto.res.AppointmentRes;
import sfa.bill_service.dto.res.PaginatedResp;
import sfa.bill_service.interceptor.UserAuthorization;
import sfa.bill_service.services.AppointmentServices;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentServices appointmentServices;

    @PostMapping
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<AppointmentRes> createAppointment(@RequestBody AppointmentReq appointmentReq) {
        AppointmentRes appointmentRes = appointmentServices.createAppointment(appointmentReq);
        return new ResponseEntity<>(appointmentRes, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<AppointmentRes> getAppointmentById(@PathVariable Long id) {
        AppointmentRes appointmentRes = appointmentServices.getAppointment(id);
        return new ResponseEntity<>(appointmentRes, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<Void> deleteAppointmentById(@PathVariable Long id) {
        appointmentServices.deleteAppointmentById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/status")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<Void> changeAppointmentStatus(@PathVariable Long id, @RequestParam AppointmentStatus appointmentStatus) {
        appointmentServices.changeAppointmentStatus(id, appointmentStatus);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getAllAppointments")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<PaginatedResp<AppointmentRes>> getAllAppointments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "appointmentDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        PaginatedResp<AppointmentRes> paginatedResp = appointmentServices.getAllAppointment(page, pageSize, sortBy, sortDirection);
        return new ResponseEntity<>(paginatedResp, HttpStatus.OK);
    }

    @GetMapping("/getAllAppointmentsByStatus")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<PaginatedResp<AppointmentRes>> getAllAppointmentsByStatus(
            @RequestParam AppointmentStatus appointmentStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        PaginatedResp<AppointmentRes> paginatedResp = appointmentServices.getAllByAppointmentStatus(appointmentStatus, page, pageSize, sortBy, sortDirection);
        return new ResponseEntity<>(paginatedResp, HttpStatus.OK);
    }
}
