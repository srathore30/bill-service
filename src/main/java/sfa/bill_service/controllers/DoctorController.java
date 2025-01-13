package sfa.bill_service.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sfa.bill_service.dto.req.DoctorReq;
import sfa.bill_service.dto.res.DoctorRes;
import sfa.bill_service.dto.res.PaginatedResp;
import sfa.bill_service.services.DoctorServices;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorServices doctorServices;

    @PostMapping
    public ResponseEntity<DoctorRes> createDoctor(@RequestBody DoctorReq doctorReq) {
        DoctorRes doctorRes = doctorServices.createDoctor(doctorReq);
        return new ResponseEntity<>(doctorRes, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorRes> getDoctorById(@PathVariable Long id) {
        DoctorRes doctorRes = doctorServices.getDoctor(id);
        return new ResponseEntity<>(doctorRes, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorRes> updateDoctorById(@PathVariable Long id, @RequestBody DoctorReq doctorReq) {
        DoctorRes doctorRes = doctorServices.updateDoctorById(id, doctorReq);
        return new ResponseEntity<>(doctorRes, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctorById(@PathVariable Long id) {
        doctorServices.deleteDoctorById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/getAllDoctors")
    public ResponseEntity<PaginatedResp<DoctorRes>> getAllDoctors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        PaginatedResp<DoctorRes> paginatedResp = doctorServices.getAllDoctor(page, pageSize, sortBy, sortDirection);
        return new ResponseEntity<>(paginatedResp, HttpStatus.OK);
    }
}
