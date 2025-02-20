package sfa.bill_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sfa.bill_service.constants.ApiErrorCodes;
import sfa.bill_service.constants.Status;
import sfa.bill_service.dto.req.CategoryReq;
import sfa.bill_service.dto.res.ServiceCategoryRes;
import sfa.bill_service.entities.ServiceCategory;
import sfa.bill_service.exceptions.NoSuchElementFoundException;
import sfa.bill_service.exceptions.ValidationException;
import sfa.bill_service.repositories.ServiceCategoryRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServiceCategoryServices {

    private final ServiceCategoryRepo serviceCategoryRepo;

    public ServiceCategoryRes createCategory(CategoryReq categoryReq){
        Optional<ServiceCategory> existingCategory = serviceCategoryRepo.findByNameAndCghsLocation(categoryReq.getName(), categoryReq.getCghsLocation());
        if (existingCategory.isPresent()) {
            throw new ValidationException(ApiErrorCodes.CATEGORY_ALREADY_EXISTS.getErrorCode(), ApiErrorCodes.CATEGORY_ALREADY_EXISTS.getErrorMessage());
        }
        ServiceCategory serviceCategory = new ServiceCategory(categoryReq.getName(), Status.Active, categoryReq.getCghsLocation(), categoryReq.getLastUpdated());
        return mapToDto(serviceCategoryRepo.save(serviceCategory));
    }

    public List<ServiceCategoryRes> createCategoryInBulk(List<CategoryReq> reqList) {
        List<ServiceCategory> serviceCategories = new ArrayList<>();
        for (CategoryReq categoryReq : reqList) {
            Optional<ServiceCategory> existingCategory = serviceCategoryRepo.findByNameAndCghsLocation(categoryReq.getName(), categoryReq.getCghsLocation());
            if (existingCategory.isPresent()) {
                throw new ValidationException(ApiErrorCodes.CATEGORY_ALREADY_EXISTS.getErrorCode(),ApiErrorCodes.CATEGORY_ALREADY_EXISTS.getErrorMessage());
            }

            ServiceCategory serviceCategory = new ServiceCategory(categoryReq.getName(), Status.Active, categoryReq.getCghsLocation(), categoryReq.getLastUpdated());
            serviceCategories.add(serviceCategory);
        }
        return serviceCategoryRepo.saveAll(serviceCategories).stream().map(this::mapToDto).toList();
    }

    public ServiceCategoryRes getCategoryById(Long id){
        Optional<ServiceCategory> optionalServiceCategory = serviceCategoryRepo.findById(id);
        if(optionalServiceCategory.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.CATEGORY_NOT_FOUND.getErrorCode(), ApiErrorCodes.CATEGORY_NOT_FOUND.getErrorMessage());
        }
        return mapToDto(optionalServiceCategory.get());
    }

    public ServiceCategoryRes updateCategoryById(Long id, CategoryReq categoryReq){
        Optional<ServiceCategory> optionalServiceCategory = serviceCategoryRepo.findById(id);
        if(optionalServiceCategory.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.CATEGORY_NOT_FOUND.getErrorCode(), ApiErrorCodes.CATEGORY_NOT_FOUND.getErrorMessage());
        }
        optionalServiceCategory.get().setName(categoryReq.getName());
        optionalServiceCategory.get().setLastUpdated(categoryReq.getLastUpdated());
        optionalServiceCategory.get().setCghsLocation(categoryReq.getCghsLocation());
        serviceCategoryRepo.save(optionalServiceCategory.get());
        return mapToDto(serviceCategoryRepo.save(optionalServiceCategory.get()));
    }

    public ServiceCategoryRes getCategoryByName(String name, String cgshLocation){
        Optional<ServiceCategory> optionalServiceCategory = serviceCategoryRepo.findByNameAndCghsLocation(name, cgshLocation);
        if(optionalServiceCategory.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.CATEGORY_NOT_FOUND.getErrorCode(), ApiErrorCodes.CATEGORY_NOT_FOUND.getErrorMessage());
        }
        return mapToDto(optionalServiceCategory.get());
    }

    public void deleteCategoryById(Long id){
        Optional<ServiceCategory> optionalServiceCategory = serviceCategoryRepo.findById(id);
        if(optionalServiceCategory.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.CATEGORY_NOT_FOUND.getErrorCode(), ApiErrorCodes.CATEGORY_NOT_FOUND.getErrorMessage());
        }
        optionalServiceCategory.get().setStatus(Status.InActive);
        serviceCategoryRepo.save(optionalServiceCategory.get());
    }

    public List<ServiceCategoryRes> getAllCategory(){
        return serviceCategoryRepo.findAll().stream().filter(serviceCategory -> serviceCategory.getStatus() != Status.InActive).map(this::mapToDto).toList();
    }

    public ServiceCategoryRes mapToDto(ServiceCategory serviceCategory){
        ServiceCategoryRes serviceCategoryRes = new ServiceCategoryRes();
        serviceCategoryRes.setId(serviceCategory.getId());
        serviceCategoryRes.setName(serviceCategory.getName());
        serviceCategoryRes.setLastUpdated(serviceCategory.getLastUpdated());
        serviceCategoryRes.setCghsLocation(serviceCategory.getCghsLocation());
        return serviceCategoryRes;
    }
}
