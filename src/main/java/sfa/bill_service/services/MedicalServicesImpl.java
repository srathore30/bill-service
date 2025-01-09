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
import sfa.bill_service.entities.ServicesEntity;
import sfa.bill_service.exceptions.NoSuchElementFoundException;
import sfa.bill_service.repositories.RoomRepo;
import sfa.bill_service.repositories.ServicesRepo;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicalServicesImpl {

    private final ServicesRepo servicesRepo;

    public ServicesRes createServices(ServicesReq servicesReq){
        ServicesEntity servicesEntity = mapToEntity(servicesReq);
        return mapToDto(servicesRepo.save(servicesEntity));
    }

    public ServicesRes getServices(Long id){
        Optional<ServicesEntity> optionalServicesEntity = servicesRepo.findById(id);
        if(optionalServicesEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.SERVICES_NOT_FOUND.getErrorCode(), ApiErrorCodes.SERVICES_NOT_FOUND.getErrorMessage());
        }
        return mapToDto(optionalServicesEntity.get());
    }

    public ServicesRes getServicesByServiceCode(String serviceCode){
        Optional<ServicesEntity> optionalServicesEntity = servicesRepo.findByServiceCode(serviceCode);
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

    private ServicesEntity mapToEntity(ServicesReq req){
        ServicesEntity entity = new ServicesEntity();
        entity.setStatus(Status.Active);
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
        res.setServiceName(entity.getServiceName());
        res.setNablRate(entity.getNablRate());
        res.setNonNablRate(entity.getNonNablRate());
        res.setId(entity.getId());
        return res;
    }
}
