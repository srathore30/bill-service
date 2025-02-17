package sfa.bill_service.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sfa.bill_service.constants.UserRole;
import sfa.bill_service.dto.res.ServiceCategoryRes;
import sfa.bill_service.interceptor.UserAuthorization;
import sfa.bill_service.services.ServiceCategoryServices;

import java.util.List;

@RestController
@RequestMapping("/service-categories")
@RequiredArgsConstructor
public class ServiceCategoryController {

    private final ServiceCategoryServices serviceCategoryServices;

    @PostMapping
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<ServiceCategoryRes> createCategory(@RequestParam String name) {
        ServiceCategoryRes response = serviceCategoryServices.createCategory(name);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<ServiceCategoryRes> getCategoryById(@PathVariable Long id) {
        ServiceCategoryRes response = serviceCategoryServices.getCategoryById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<ServiceCategoryRes> updateCategoryById(@PathVariable Long id, @RequestParam String name) {
        ServiceCategoryRes response = serviceCategoryServices.updateCategoryById(id, name);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<Void> deleteCategoryById(@PathVariable Long id) {
        serviceCategoryServices.deleteCategoryById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getAllCategory")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<List<ServiceCategoryRes>> getAllCategories() {
        List<ServiceCategoryRes> response = serviceCategoryServices.getAllCategory();
        return ResponseEntity.ok(response);
    }
}
