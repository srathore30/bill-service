package sfa.bill_service.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sfa.bill_service.dto.req.ServicesReq;
import sfa.bill_service.dto.res.PaginatedResp;
import sfa.bill_service.dto.res.ServicesRes;
import sfa.bill_service.services.MedicalServicesImpl;

@RestController
@RequestMapping("/services")
@RequiredArgsConstructor
public class MedicalServicesController {

    private final MedicalServicesImpl medicalServices;

    @PostMapping
    public ResponseEntity<ServicesRes> createService(@RequestBody ServicesReq servicesReq) {
        ServicesRes servicesRes = medicalServices.createServices(servicesReq);
        return new ResponseEntity<>(servicesRes, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicesRes> getServiceById(@PathVariable Long id) {
        ServicesRes servicesRes = medicalServices.getServices(id);
        return new ResponseEntity<>(servicesRes, HttpStatus.OK);
    }

    @GetMapping("/byServiceCode/{serviceCode}")
    public ResponseEntity<ServicesRes> getServiceByServiceCode(@PathVariable String serviceCode) {
        ServicesRes servicesRes = medicalServices.getServicesByServiceCode(serviceCode);
        return new ResponseEntity<>(servicesRes, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicesRes> updateServiceById(@PathVariable Long id, @RequestBody ServicesReq servicesReq) {
        ServicesRes servicesRes = medicalServices.updateServicesById(id, servicesReq);
        return new ResponseEntity<>(servicesRes, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceById(@PathVariable Long id) {
        medicalServices.deleteServicesById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getAllServicesByUserId/{userId}")
    public ResponseEntity<PaginatedResp<ServicesRes>> getAllServicesByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        PaginatedResp<ServicesRes> paginatedResp = medicalServices.getAllServicesByUserId(userId, page, pageSize, sortBy, sortDirection);
        return new ResponseEntity<>(paginatedResp, HttpStatus.OK);
    }
}
