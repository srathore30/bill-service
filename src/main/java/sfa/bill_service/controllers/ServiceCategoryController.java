package sfa.bill_service.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sfa.bill_service.constants.UserRole;
import sfa.bill_service.dto.req.CategoryReq;
import sfa.bill_service.dto.req.ServicesReq;
import sfa.bill_service.dto.res.ServiceCategoryRes;
import sfa.bill_service.dto.res.ServicesRes;
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
    public ResponseEntity<ServiceCategoryRes> createCategory(@RequestBody CategoryReq categoryReq) {
        ServiceCategoryRes response = serviceCategoryServices.createCategory(categoryReq);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/bulk")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<List<ServiceCategoryRes>> createCategoryInBulk(@RequestBody List<CategoryReq> categoryReqList) {
        List<ServiceCategoryRes> serviceCategoryRes = serviceCategoryServices.createCategoryInBulk(categoryReqList);
        return new ResponseEntity<>(serviceCategoryRes, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<ServiceCategoryRes> getCategoryById(@PathVariable Long id) {
        ServiceCategoryRes response = serviceCategoryServices.getCategoryById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getCategoryByName")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<ServiceCategoryRes> getCategoryByName(@RequestParam String name, @RequestParam String cghsLocation) {
        ServiceCategoryRes response = serviceCategoryServices.getCategoryByName(name, cghsLocation);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @UserAuthorization(allowedRoles = {UserRole.Super_Admin,UserRole.Admin,UserRole.User,UserRole.Receptionist})
    public ResponseEntity<ServiceCategoryRes> updateCategoryById(@PathVariable Long id, @RequestBody CategoryReq req) {
        ServiceCategoryRes response = serviceCategoryServices.updateCategoryById(id, req);
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
