package sfa.bill_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sfa.bill_service.constants.ApiErrorCodes;
import sfa.bill_service.constants.Status;
import sfa.bill_service.dto.req.ServicesReq;
import sfa.bill_service.dto.res.PaginatedResp;
import sfa.bill_service.dto.res.ServicesRes;
import sfa.bill_service.entities.ServiceCategory;
import sfa.bill_service.entities.ServicesEntity;
import sfa.bill_service.exceptions.NoSuchElementFoundException;
import sfa.bill_service.repositories.RoomRepo;
import sfa.bill_service.repositories.ServiceCategoryRepo;
import sfa.bill_service.repositories.ServicesRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicalServicesImpl {

    private final ServicesRepo servicesRepo;
    private final ServiceCategoryRepo serviceCategoryRepo;

    public ServicesRes createServices(ServicesReq servicesReq){
        Optional<ServicesEntity> optionalServicesEntity = servicesRepo.findByServiceCodeAndCghsLocation(servicesReq.getServiceCode(), servicesReq.getCghsLocation());
        if(optionalServicesEntity.isPresent()){
            throw new NoSuchElementFoundException(4549841, "Service already created with this code " + servicesReq.getServiceCode());
        }
        ServicesEntity servicesEntity = mapToEntity(servicesReq);
        return mapToDto(servicesRepo.save(servicesEntity));
    }
    public List<ServicesRes> createServicesInBulk(List<ServicesReq> servicesReqList){
        List<ServicesRes> servicesResList = new ArrayList<>();
        for(ServicesReq servicesReq : servicesReqList) {
            Optional<ServicesEntity> optionalServicesEntity = servicesRepo.findByServiceCodeAndCghsLocation(servicesReq.getServiceCode(), servicesReq.getCghsLocation());
            if (optionalServicesEntity.isPresent()) {
                throw new NoSuchElementFoundException(4549841, "Service already created with this code " + servicesReq.getServiceCode());
            }
            ServicesEntity servicesEntity = mapToEntity(servicesReq);
            servicesResList.add(mapToDto(servicesRepo.save(servicesEntity)));
        }
        return servicesResList;
    }

    public ServicesRes getServices(Long id){
        Optional<ServicesEntity> optionalServicesEntity = servicesRepo.findById(id);
        if(optionalServicesEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.SERVICES_NOT_FOUND.getErrorCode(), ApiErrorCodes.SERVICES_NOT_FOUND.getErrorMessage());
        }
        return mapToDto(optionalServicesEntity.get());
    }

    public ServicesRes getServicesByServiceCodeAndCghLocation(String serviceCode, String cghsLocation){
        Optional<ServicesEntity> optionalServicesEntity = servicesRepo.findByServiceCodeAndCghsLocation(serviceCode, cghsLocation);
        if(optionalServicesEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.SERVICES_NOT_FOUND.getErrorCode(), ApiErrorCodes.SERVICES_NOT_FOUND.getErrorMessage());
        }
        return mapToDto(optionalServicesEntity.get());
    }

    public ServicesRes updateServicesById(Long id, ServicesReq investigationReq){
        Optional<ServicesEntity> optionalServicesEntity = servicesRepo.findById(id);
        if(optionalServicesEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.SERVICES_NOT_FOUND.getErrorCode(), ApiErrorCodes.SERVICES_NOT_FOUND.getErrorMessage());
        }
        updateEntityFromDto(optionalServicesEntity.get(), investigationReq);
        return mapToDto(servicesRepo.save(optionalServicesEntity.get()));
    }

    public ServicesRes deleteServicesById(Long id){
        Optional<ServicesEntity> optionalServicesEntity = servicesRepo.findById(id);
        if(optionalServicesEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.SERVICES_NOT_FOUND.getErrorCode(), ApiErrorCodes.SERVICES_NOT_FOUND.getErrorMessage());
        }
        optionalServicesEntity.get().setStatus(Status.InActive);
        return mapToDto(servicesRepo.save(optionalServicesEntity.get()));
    }

    public PaginatedResp<ServicesRes> getAllServices(int page, int pageSize, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<ServicesEntity> servicesEntityPage = servicesRepo.findAll(pageable);
        List<ServicesRes> servicesResList;
        servicesResList = servicesEntityPage.getContent().stream().filter(servicesEntity -> servicesEntity.getStatus() == Status.Active).map(this::mapToDto).toList();
        return new PaginatedResp<>(servicesEntityPage.getTotalElements(), servicesEntityPage.getTotalPages(), page, servicesResList);
    }
    public PaginatedResp<ServicesRes> getAllServicesByCategoryId(Long categoryId, int page, int pageSize, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<ServicesEntity> servicesEntityPage = servicesRepo.findByServiceCategoryId(categoryId, pageable);
        List<ServicesRes> servicesResList;
        servicesResList = servicesEntityPage.getContent().stream().filter(servicesEntity -> servicesEntity.getStatus() == Status.Active).map(this::mapToDto).toList();
        return new PaginatedResp<>(servicesEntityPage.getTotalElements(), servicesEntityPage.getTotalPages(), page, servicesResList);
    }

    private ServicesEntity mapToEntity(ServicesReq req){
        Optional<ServiceCategory> optionalServiceCategory = serviceCategoryRepo.findById(req.getCategoryId());
        if(optionalServiceCategory.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.CATEGORY_NOT_FOUND.getErrorCode(), ApiErrorCodes.CATEGORY_NOT_FOUND.getErrorMessage());
        }
        ServicesEntity entity = new ServicesEntity();
        entity.setStatus(Status.Active);
        entity.setServiceCategory(optionalServiceCategory.get());
        entity.setCghsLocation(req.getCghsLocation());
        entity.setServiceCode(req.getServiceCode());
        entity.setServiceName(req.getServiceName());
        entity.setNablRate(req.getNablRate());
        entity.setNonNablRate(req.getNonNablRate());
        return entity;
    }

    private void updateEntityFromDto(ServicesEntity entity, ServicesReq req){
        entity.setServiceCode(req.getServiceCode());
        entity.setServiceName(req.getServiceName());
        entity.setNablRate(req.getNablRate());
        entity.setNonNablRate(req.getNonNablRate());
    }

    private ServicesRes mapToDto(ServicesEntity entity){
        ServicesRes res = new ServicesRes();
        res.setServiceCode(entity.getServiceCode());
        res.setCghsLocation(entity.getCghsLocation());
        res.setServiceName(entity.getServiceName());
        res.setModifiedDate(entity.getModifiedDate());
        res.setCategoryName(entity.getServiceCategory().getName());
        res.setNablRate(entity.getNablRate());
        res.setNonNablRate(entity.getNonNablRate());
        res.setId(entity.getId());
        return res;
    }
}
