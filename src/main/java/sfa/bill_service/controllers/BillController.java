package sfa.bill_service.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sfa.bill_service.entities.BillEntity;
import sfa.bill_service.entities.BillEntryEntity;
import sfa.bill_service.services.BillService;
import sfa.bill_service.dto.res.PaginatedResp;

import java.util.List;

@RestController
@RequestMapping("/bills")
@RequiredArgsConstructor
class BillController {
    private final BillService billService;

    @PostMapping("/create")
    public ResponseEntity<BillEntity> createBill(@RequestParam Long patientId, @RequestBody List<BillEntryEntity> billEntries) {
        BillEntity bill = billService.createBill(patientId, billEntries);
        return new ResponseEntity<>(bill, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillEntity> getBillById(@PathVariable Long id) {
        BillEntity bill = billService.getBillById(id);
        return new ResponseEntity<>(bill, HttpStatus.OK);
    }

    @GetMapping("/getAllBills")
    public ResponseEntity<Page<BillEntity>> getAllBills(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        Page<BillEntity> paginatedBills = billService.getAllBills(page, pageSize, sortBy, sortDirection);
        return new ResponseEntity<>(paginatedBills, HttpStatus.OK);
    }

    @PostMapping("/finalizeBill/{contactNumber}")
    public ResponseEntity<Void> finalizeBill(@PathVariable Long contactNumber) {
        billService.finalizeBill(contactNumber);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/findByContactNumber/{contactNumber}")
    public ResponseEntity<List<BillEntity>> getBillByContactNumber(@PathVariable Long contactNumber) {
        List<BillEntity> bills = billService.getBillByContactNumber(contactNumber);
        return new ResponseEntity<>(bills, HttpStatus.OK);
    }
}


