package sfa.bill_service.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sfa.bill_service.constants.UserRole;
import sfa.bill_service.dto.req.LabTestReq;
import sfa.bill_service.dto.res.PaginatedResp;
import sfa.bill_service.dto.res.LabTestRes;
import sfa.bill_service.interceptor.UserAuthorization;
import sfa.bill_service.services.LabTestServices;
import sfa.bill_service.constants.LabTestStatus;

@RestController
@RequestMapping("/lab-tests")
@RequiredArgsConstructor
public class LabTestController {

    private final LabTestServices labTestServices;

    @PostMapping
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<LabTestRes> createLabTest(@RequestBody LabTestReq labTestReq) {
        LabTestRes labTestRes = labTestServices.createLabTest(labTestReq);
        return new ResponseEntity<>(labTestRes, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<LabTestRes> getLabTest(@PathVariable Long id) {
        LabTestRes labTestRes = labTestServices.getLabTest(id);
        return new ResponseEntity<>(labTestRes, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<LabTestRes> updateLabTest(@PathVariable Long id, @RequestBody LabTestReq labTestReq) {
        LabTestRes labTestRes = labTestServices.updateLabTestById(id, labTestReq);
        return new ResponseEntity<>(labTestRes, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<Void> deleteLabTest(@PathVariable Long id) {
        labTestServices.deleteLabTestById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/updateLabTestStatus/{id}")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<LabTestRes> updateLabTestStatus(@PathVariable Long id, @RequestParam LabTestStatus labTestStatus) {
        labTestServices.updateLabTestStatusById(id, labTestStatus);
        LabTestRes labTestRes = labTestServices.getLabTest(id);
        return new ResponseEntity<>(labTestRes, HttpStatus.OK);
    }

    @GetMapping("/getAllLabTest")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<PaginatedResp<LabTestRes>> getAllLabTest(
            @RequestParam int page,
            @RequestParam int pageSize,
            @RequestParam String sortBy,
            @RequestParam String sortDirection) {

        PaginatedResp<LabTestRes> response = labTestServices.getAllLabTest(
                page, pageSize, sortBy, sortDirection);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getAllLabTestByLabTestStatus/{labTestStatus}")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<PaginatedResp<LabTestRes>> getAllLabTestByLabTestStatus(
            @PathVariable LabTestStatus labTestStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        PaginatedResp<LabTestRes> response = labTestServices.getAllLabTestByLabTestStatus(
                labTestStatus, page, pageSize, sortBy, sortDirection);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
