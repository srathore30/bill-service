package sfa.bill_service.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sfa.bill_service.dto.req.MedicationReq;
import sfa.bill_service.dto.res.MedicationRes;
import sfa.bill_service.dto.res.PaginatedResp;
import sfa.bill_service.services.MedicationServices;

@RestController
@RequestMapping("/medications")
@RequiredArgsConstructor
public class MedicationController {

    private final MedicationServices medicationServices;

    @PostMapping
    public ResponseEntity<MedicationRes> createMedication(@RequestBody MedicationReq medicationReq) {
        MedicationRes medicationRes = medicationServices.createMedication(medicationReq);
        return new ResponseEntity<>(medicationRes, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicationRes> getMedicationById(@PathVariable Long id) {
        MedicationRes medicationRes = medicationServices.getMedicationById(id);
        return new ResponseEntity<>(medicationRes, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicationById(@PathVariable Long id) {
        medicationServices.deleteMedicationById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicationRes> updateMedication(@PathVariable Long id, @RequestBody MedicationReq medicationReq) {
        MedicationRes medicationRes = medicationServices.updateMedication(id, medicationReq);
        return new ResponseEntity<>(medicationRes, HttpStatus.OK);
    }

    @GetMapping("/getAllMedication")
    public ResponseEntity<PaginatedResp<MedicationRes>> getAllMedication(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        PaginatedResp<MedicationRes> paginatedResp = medicationServices.getAllMedication(page, pageSize, sortBy, sortDirection);
        return new ResponseEntity<>(paginatedResp, HttpStatus.OK);
    }
}
