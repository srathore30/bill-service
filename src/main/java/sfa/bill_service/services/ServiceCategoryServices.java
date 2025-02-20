package sfa.bill_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sfa.bill_service.constants.ApiErrorCodes;
import sfa.bill_service.constants.Status;
import sfa.bill_service.dto.req.ServicesReq;
import sfa.bill_service.dto.res.ServiceCategoryRes;
import sfa.bill_service.dto.res.ServicesRes;
import sfa.bill_service.entities.ServiceCategory;
import sfa.bill_service.entities.ServicesEntity;
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

    public ServiceCategoryRes createCategory(String name){
        Optional<ServiceCategory> existingCategory = serviceCategoryRepo.findByName(name);
        if (existingCategory.isPresent()) {
            throw new ValidationException(ApiErrorCodes.CATEGORY_ALREADY_EXISTS.getErrorCode(), ApiErrorCodes.CATEGORY_ALREADY_EXISTS.getErrorMessage());
        }
        ServiceCategory serviceCategory = new ServiceCategory(name, Status.Active);
        serviceCategoryRepo.save(serviceCategory);
        return new ServiceCategoryRes(serviceCategory.getId(), serviceCategory.getName());
    }

    public List<ServiceCategoryRes> createCategoryInBulk(List<String> categoryNames) {
        List<ServiceCategoryRes> serviceCategoryResList = new ArrayList<>();
        for (String name : categoryNames) {
            Optional<ServiceCategory> existingCategory = serviceCategoryRepo.findByName(name);
            if (existingCategory.isPresent()) {
                throw new ValidationException(ApiErrorCodes.CATEGORY_ALREADY_EXISTS.getErrorCode(),ApiErrorCodes.CATEGORY_ALREADY_EXISTS.getErrorMessage());
            }

            ServiceCategory serviceCategory = new ServiceCategory(name, Status.Active);
            serviceCategoryRepo.save(serviceCategory);
            serviceCategoryResList.add(new ServiceCategoryRes(serviceCategory.getId(), serviceCategory.getName()));
        }

        return serviceCategoryResList;
    }

    public ServiceCategoryRes getCategoryById(Long id){
        Optional<ServiceCategory> optionalServiceCategory = serviceCategoryRepo.findById(id);
        if(optionalServiceCategory.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.CATEGORY_NOT_FOUND.getErrorCode(), ApiErrorCodes.CATEGORY_NOT_FOUND.getErrorMessage());
        }
        return new ServiceCategoryRes(optionalServiceCategory.get().getId(), optionalServiceCategory.get().getName());
    }

    public ServiceCategoryRes updateCategoryById(Long id, String name){
        Optional<ServiceCategory> optionalServiceCategory = serviceCategoryRepo.findById(id);
        if(optionalServiceCategory.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.CATEGORY_NOT_FOUND.getErrorCode(), ApiErrorCodes.CATEGORY_NOT_FOUND.getErrorMessage());
        }
        optionalServiceCategory.get().setName(name);
        serviceCategoryRepo.save(optionalServiceCategory.get());
        return new ServiceCategoryRes(optionalServiceCategory.get().getId(), optionalServiceCategory.get().getName());
    }

    public ServiceCategoryRes getCategoryByName(String name){
        Optional<ServiceCategory> optionalServiceCategory = serviceCategoryRepo.findByName(name);
        if(optionalServiceCategory.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.CATEGORY_NOT_FOUND.getErrorCode(), ApiErrorCodes.CATEGORY_NOT_FOUND.getErrorMessage());
        }
        return new ServiceCategoryRes(optionalServiceCategory.get().getId(), optionalServiceCategory.get().getName());
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
        return serviceCategoryRepo.findAll().stream().filter(serviceCategory -> serviceCategory.getStatus() != Status.InActive).map(serviceCategory -> new ServiceCategoryRes(serviceCategory.getId(), serviceCategory.getName())).toList();
    }
}
