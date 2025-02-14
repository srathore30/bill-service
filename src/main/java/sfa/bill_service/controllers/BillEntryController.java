package sfa.bill_service.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sfa.bill_service.dto.req.BillEntryReq;
import sfa.bill_service.dto.res.BillEntryRes;
import sfa.bill_service.services.BillEntryService;

import java.util.List;

@RestController
@RequestMapping("/bill-entries")
@RequiredArgsConstructor
class BillEntryController {
    private final BillEntryService billEntryService;

    @PostMapping("/create")
    public ResponseEntity<BillEntryRes> createBillEntry(@RequestBody BillEntryReq billEntryReq) {
        BillEntryRes billEntryRes = billEntryService.createBillEntry(billEntryReq);
        return new ResponseEntity<>(billEntryRes, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillEntryRes> getBillEntryById(@PathVariable Long id) {
        BillEntryRes billEntryRes = billEntryService.getBillEntryById(id);
        return new ResponseEntity<>(billEntryRes, HttpStatus.OK);
    }

    @GetMapping("/getAllBills")
    public ResponseEntity<List<BillEntryRes>> getAllBills() {
        return new ResponseEntity<>(billEntryService.getAllBillEntries(), HttpStatus.OK);
    }
    @PutMapping("/updatePaidAmount/{entryId}")
    public ResponseEntity<Void> updatePaidAmount(@PathVariable Long entryId, @RequestParam double paidAmount) {
        billEntryService.updatePaidAmount(entryId, paidAmount);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

