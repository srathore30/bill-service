package sfa.bill_service.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sfa.bill_service.constants.UserRole;
import sfa.bill_service.dto.req.ServicesReq;
import sfa.bill_service.dto.res.PaginatedResp;
import sfa.bill_service.dto.res.ServicesRes;
import sfa.bill_service.interceptor.UserAuthorization;
import sfa.bill_service.services.MedicalServicesImpl;

import java.util.List;

@RestController
@RequestMapping("/services")
@RequiredArgsConstructor
public class MedicalServicesController {

    private final MedicalServicesImpl medicalServices;

    @PostMapping
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<ServicesRes> createService(@RequestBody ServicesReq servicesReq) {
        ServicesRes servicesRes = medicalServices.createServices(servicesReq);
        return new ResponseEntity<>(servicesRes, HttpStatus.CREATED);
    }
    @PostMapping("/bulk")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<List<ServicesRes>> createServicesInBulk(@RequestBody List<ServicesReq> servicesReq) {
        List<ServicesRes> servicesRes = medicalServices.createServicesInBulk(servicesReq);
        return new ResponseEntity<>(servicesRes, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<ServicesRes> getServiceById(@PathVariable Long id) {
        ServicesRes servicesRes = medicalServices.getServices(id);
        return new ResponseEntity<>(servicesRes, HttpStatus.OK);
    }

    @GetMapping("/byServiceCodeAndCghsLocation/{serviceCode}/{cghsLocation}")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<ServicesRes> getServicesByServiceCodeAndCghLocation(@PathVariable String serviceCode, @PathVariable String cghsLocation) {
        ServicesRes servicesRes = medicalServices.getServicesByServiceCodeAndCghLocation(serviceCode, cghsLocation);
        return new ResponseEntity<>(servicesRes, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<ServicesRes> updateServiceById(@PathVariable Long id, @RequestBody ServicesReq servicesReq) {
        ServicesRes servicesRes = medicalServices.updateServicesById(id, servicesReq);
        return new ResponseEntity<>(servicesRes, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<Void> deleteServiceById(@PathVariable Long id) {
        medicalServices.deleteServicesById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getAllServices")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<PaginatedResp<ServicesRes>> getAllServices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        PaginatedResp<ServicesRes> paginatedResp = medicalServices.getAllServices(page, pageSize, sortBy, sortDirection);
        return new ResponseEntity<>(paginatedResp, HttpStatus.OK);
    }

    @GetMapping("/getAllServicesByCategoryId")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<PaginatedResp<ServicesRes>> getAllServicesByCategoryId(
            @RequestParam Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        PaginatedResp<ServicesRes> paginatedResp = medicalServices.getAllServicesByCategoryId(categoryId,page, pageSize, sortBy, sortDirection);
        return new ResponseEntity<>(paginatedResp, HttpStatus.OK);
    }
}
