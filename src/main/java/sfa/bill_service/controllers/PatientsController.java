package sfa.bill_service.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sfa.bill_service.constants.UserRole;
import sfa.bill_service.dto.req.PatientsReq;
import sfa.bill_service.dto.res.PaginatedResp;
import sfa.bill_service.dto.res.PatientsRes;
import sfa.bill_service.interceptor.UserAuthorization;
import sfa.bill_service.services.PatientServices;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientsController {

    private final PatientServices patientServices;

    @PostMapping
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<PatientsRes> createPatient(@RequestBody PatientsReq patientsReq) {
        PatientsRes patientsRes = patientServices.createPatients(patientsReq);
        return new ResponseEntity<>(patientsRes, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<PatientsRes> getPatientById(@PathVariable Long id) {
        PatientsRes patientsRes = patientServices.getPatients(id);
        return new ResponseEntity<>(patientsRes, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<PatientsRes> updatePatientById(@PathVariable Long id, @RequestBody PatientsReq patientsReq) {
        PatientsRes patientsRes = patientServices.updatePatientsById(id, patientsReq);
        return new ResponseEntity<>(patientsRes, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<Void> deletePatientById(@PathVariable Long id) {
        patientServices.deletePatientsById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/getAllPatients")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<PaginatedResp<PatientsRes>> getAllPatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        PaginatedResp<PatientsRes> paginatedResp = patientServices.getAllPatients(page, pageSize, sortBy, sortDirection);
        return new ResponseEntity<>(paginatedResp, HttpStatus.OK);
    }
}
