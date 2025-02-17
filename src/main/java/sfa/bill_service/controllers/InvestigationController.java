package sfa.bill_service.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sfa.bill_service.constants.UserRole;
import sfa.bill_service.dto.req.InvestigationReq;
import sfa.bill_service.dto.res.InvestigationRes;
import sfa.bill_service.dto.res.PaginatedResp;
import sfa.bill_service.interceptor.UserAuthorization;
import sfa.bill_service.services.InvestigationServices;

@RestController
@AllArgsConstructor
@RequestMapping("/investigations")
public class InvestigationController {

    private final InvestigationServices investigationServices;

    @PostMapping
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<InvestigationRes> createInvestigation(@RequestBody InvestigationReq investigationReq) {
        InvestigationRes investigationRes = investigationServices.createInvestigation(investigationReq);
        return new ResponseEntity<>(investigationRes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<InvestigationRes> getInvestigation(@PathVariable Long id) {
        InvestigationRes investigationRes = investigationServices.getInvestigation(id);
        return new ResponseEntity<>(investigationRes, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<InvestigationRes> updateInvestigation(@PathVariable Long id, @RequestBody InvestigationReq investigationReq) {
        InvestigationRes investigationRes = investigationServices.updateInvestigationById(id, investigationReq);
        return new ResponseEntity<>(investigationRes, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<InvestigationRes> deleteInvestigation(@PathVariable Long id) {
        InvestigationRes investigationRes = investigationServices.deleteInvestigationById(id);
        return new ResponseEntity<>(investigationRes, HttpStatus.OK);
    }

    @GetMapping("/getAllInvestigation")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<PaginatedResp<InvestigationRes>> getAllInvestigation(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        PaginatedResp<InvestigationRes> paginatedResp = investigationServices.getAllInvestigation(page, pageSize, sortBy, sortDirection);
        return new ResponseEntity<>(paginatedResp, HttpStatus.OK);
    }
}
