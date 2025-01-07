package sfa.bill_service.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sfa.bill_service.dto.req.LabTestReq;
import sfa.bill_service.dto.res.PaginatedResp;
import sfa.bill_service.dto.res.LabTestRes;
import sfa.bill_service.services.LabTestServices;
import sfa.bill_service.constants.LabTestStatus;

@RestController
@RequestMapping("/lab-tests")
@RequiredArgsConstructor
public class LabTestController {

    private final LabTestServices labTestServices;

    @PostMapping
    public ResponseEntity<LabTestRes> createLabTest(@RequestBody LabTestReq labTestReq) {
        LabTestRes labTestRes = labTestServices.createLabTest(labTestReq);
        return new ResponseEntity<>(labTestRes, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabTestRes> getLabTest(@PathVariable Long id) {
        LabTestRes labTestRes = labTestServices.getLabTest(id);
        return new ResponseEntity<>(labTestRes, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LabTestRes> updateLabTest(@PathVariable Long id, @RequestBody LabTestReq labTestReq) {
        LabTestRes labTestRes = labTestServices.updateLabTestById(id, labTestReq);
        return new ResponseEntity<>(labTestRes, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLabTest(@PathVariable Long id) {
        labTestServices.deleteLabTestById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/updateLabTestStatus/{id}")
    public ResponseEntity<LabTestRes> updateLabTestStatus(@PathVariable Long id, @RequestParam LabTestStatus labTestStatus) {
        labTestServices.updateLabTestStatusById(id, labTestStatus);
        LabTestRes labTestRes = labTestServices.getLabTest(id);
        return new ResponseEntity<>(labTestRes, HttpStatus.OK);
    }

    @GetMapping("/getAllLabTestsByUserId/{userId}")
    public ResponseEntity<PaginatedResp<LabTestRes>> getAllLabTestsByUserId(
            @PathVariable Long userId,
            @RequestParam int page,
            @RequestParam int pageSize,
            @RequestParam String sortBy,
            @RequestParam String sortDirection) {

        PaginatedResp<LabTestRes> response = labTestServices.getAllLabTestByUserId(
                userId, page, pageSize, sortBy, sortDirection);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getAllLabTestsByUserIdAndLabTestStatus/{userId}/{labTestStatus}")
    public ResponseEntity<PaginatedResp<LabTestRes>> getAllLabTestsByUserIdAndLabTestStatus(
            @PathVariable Long userId,
            @PathVariable LabTestStatus labTestStatus,
            @RequestParam int page,
            @RequestParam int pageSize,
            @RequestParam String sortBy,
            @RequestParam String sortDirection) {

        PaginatedResp<LabTestRes> response = labTestServices.getAllLabTestByUserIdAndLabTestStatus(
                userId, labTestStatus, page, pageSize, sortBy, sortDirection);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
