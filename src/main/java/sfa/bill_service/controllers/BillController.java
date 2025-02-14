package sfa.bill_service.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sfa.bill_service.dto.req.BillReq;
import sfa.bill_service.dto.res.BillRes;
import sfa.bill_service.services.BillService;
import sfa.bill_service.dto.res.PaginatedResp;

@RestController
@RequestMapping("/bills")
@RequiredArgsConstructor
class BillController {
    private final BillService billService;

    @PostMapping
    public ResponseEntity<BillRes> createBill(@RequestBody BillReq billReq) {
        BillRes billRes = billService.createBill(billReq);
        return new ResponseEntity<>(billRes, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillRes> getBillById(@PathVariable Long id) {
        BillRes billRes = billService.getBillById(id);
        return new ResponseEntity<>(billRes, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BillRes> updateBillById(@PathVariable Long id, @RequestBody BillReq billReq) {
        BillRes billRes = billService.updateBillById(id, billReq);
        return new ResponseEntity<>(billRes, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBillById(@PathVariable Long id) {
        billService.deleteBillById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getAllBills")
    public ResponseEntity<PaginatedResp<BillRes>> getAllBills(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        PaginatedResp<BillRes> paginatedResp = billService.getAllBills(page, pageSize, sortBy, sortDirection);
        return new ResponseEntity<>(paginatedResp, HttpStatus.OK);
    }
    @PutMapping("/updatePaidAmount/{entryId}")
    public ResponseEntity<Void> updatePaidAmount(@PathVariable Long entryId, @RequestParam double paidAmount) {
        billService.updatePaidAmount(entryId, paidAmount);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/finalizeBill/{contactNumber}")
    public ResponseEntity<Void> finalizeBill(@PathVariable Long contactNumber) {
        billService.finalizeBill(contactNumber);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

