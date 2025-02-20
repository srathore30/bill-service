package sfa.bill_service.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sfa.bill_service.constants.UserRole;
import sfa.bill_service.dto.req.BillReq;
import sfa.bill_service.dto.res.BillRes;
import sfa.bill_service.entities.BillEntryEntity;
import sfa.bill_service.interceptor.UserAuthorization;
import sfa.bill_service.services.BillService;
import sfa.bill_service.dto.res.PaginatedResp;

import java.util.List;

@RestController
@RequestMapping("/bills")
@RequiredArgsConstructor
class BillController {
    private final BillService billService;

    @PostMapping("/create")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<BillRes> createBill(@RequestBody BillReq billReq) {
        BillRes bill = billService.createBill(billReq);
        return new ResponseEntity<>(bill, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<BillRes> getBillById(@PathVariable Long id) {
        BillRes bill = billService.getBillById(id);
        return new ResponseEntity<>(bill, HttpStatus.OK);
    }

    @GetMapping("/getAllBills")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<PaginatedResp<BillRes>> getAllBills(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        PaginatedResp<BillRes> paginatedBills = billService.getAllBills(page, pageSize, sortBy, sortDirection);
        return new ResponseEntity<>(paginatedBills, HttpStatus.OK);
    }

    @PostMapping("/finalizeBill/{patientId}")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<Void> finalizeBill(@PathVariable Long patientId) {
        billService.finalizeBill(patientId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/findByContactNumber/{contactNumber}")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<List<BillRes>> getBillByContactNumber(@PathVariable Long contactNumber) {
        List<BillRes> bills = billService.getBillByContactNumber(contactNumber);
        return new ResponseEntity<>(bills, HttpStatus.OK);
    }
}


