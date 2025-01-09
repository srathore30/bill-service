package sfa.bill_service.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sfa.bill_service.dto.req.HospitalStaffReq;
import sfa.bill_service.dto.res.PaginatedResp;
import sfa.bill_service.dto.res.HospitalStaffRes;
import sfa.bill_service.services.HospitalStaffServices;

@RestController
@RequestMapping("/hospital-staff")
@RequiredArgsConstructor
public class HospitalStaffController {

    private final HospitalStaffServices hospitalStaffServices;

    @PostMapping
    public ResponseEntity<HospitalStaffRes> createHospitalStaff(@RequestBody HospitalStaffReq hospitalStaffReq) {
        HospitalStaffRes hospitalStaffRes = hospitalStaffServices.createHospitalStaff(hospitalStaffReq);
        return new ResponseEntity<>(hospitalStaffRes, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HospitalStaffRes> getHospitalStaffById(@PathVariable Long id) {
        HospitalStaffRes hospitalStaffRes = hospitalStaffServices.getHospitalStaff(id);
        return new ResponseEntity<>(hospitalStaffRes, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HospitalStaffRes> updateHospitalStaffById(@PathVariable Long id, @RequestBody HospitalStaffReq hospitalStaffReq) {
        HospitalStaffRes hospitalStaffRes = hospitalStaffServices.updateHospitalStaffById(id, hospitalStaffReq);
        return new ResponseEntity<>(hospitalStaffRes, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HospitalStaffRes> deleteHospitalStaffById(@PathVariable Long id) {
        HospitalStaffRes hospitalStaffRes = hospitalStaffServices.deleteHospitalStaffById(id);
        return new ResponseEntity<>(hospitalStaffRes, HttpStatus.OK);
    }

    @GetMapping("/getAllHospitalStaff/")
    public ResponseEntity<PaginatedResp<HospitalStaffRes>> getAllHospitalStaff(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        PaginatedResp<HospitalStaffRes> paginatedResp = hospitalStaffServices.getAllHospitalStaff(page, pageSize, sortBy, sortDirection);
        return new ResponseEntity<>(paginatedResp, HttpStatus.OK);
    }
}
