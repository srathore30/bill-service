package sfa.bill_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sfa.bill_service.constants.ApiErrorCodes;
import sfa.bill_service.constants.Status;
import sfa.bill_service.dto.req.HospitalStaffReq;
import sfa.bill_service.dto.res.PaginatedResp;
import sfa.bill_service.dto.res.HospitalStaffRes;
import sfa.bill_service.entities.HospitalStaffEntity;
import sfa.bill_service.exceptions.NoSuchElementFoundException;
import sfa.bill_service.repositories.HospitalStaffRepo;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HospitalStaffServices {

    private final HospitalStaffRepo hospitalStaffRepo;

    public HospitalStaffRes createHospitalStaff(HospitalStaffReq hospitalStaffReq){
        HospitalStaffEntity hospitalStaffEntity = mapToEntity(hospitalStaffReq);
        return mapToDto(hospitalStaffRepo.save(hospitalStaffEntity));
    }

    public HospitalStaffRes getHospitalStaff(Long id){
        Optional<HospitalStaffEntity> optionalHospitalStaffEntity = hospitalStaffRepo.findById(id);
        if(optionalHospitalStaffEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.STAFF_NOT_FOUND.getErrorCode(), ApiErrorCodes.STAFF_NOT_FOUND.getErrorMessage());
        }
        return mapToDto(optionalHospitalStaffEntity.get());
    }

    public HospitalStaffRes updateHospitalStaffById(Long id, HospitalStaffReq investigationReq){
        Optional<HospitalStaffEntity> optionalHospitalStaffEntity = hospitalStaffRepo.findById(id);
        if(optionalHospitalStaffEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.STAFF_NOT_FOUND.getErrorCode(), ApiErrorCodes.STAFF_NOT_FOUND.getErrorMessage());
        }
        updateEntityFromDto(optionalHospitalStaffEntity.get(), investigationReq);
        return mapToDto(hospitalStaffRepo.save(optionalHospitalStaffEntity.get()));
    }

    public HospitalStaffRes deleteHospitalStaffById(Long id){
        Optional<HospitalStaffEntity> optionalHospitalStaffEntity = hospitalStaffRepo.findById(id);
        if(optionalHospitalStaffEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.STAFF_NOT_FOUND.getErrorCode(), ApiErrorCodes.STAFF_NOT_FOUND.getErrorMessage());
        }
        optionalHospitalStaffEntity.get().setStatus(Status.InActive);
        return mapToDto(hospitalStaffRepo.save(optionalHospitalStaffEntity.get()));
    }

    public PaginatedResp<HospitalStaffRes> getAllHospitalStaff(int page, int pageSize, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<HospitalStaffEntity> hospitalStaffEntityPage = hospitalStaffRepo.findAll(pageable);
        List<HospitalStaffRes> HospitalStaffResList;
        HospitalStaffResList = hospitalStaffEntityPage.getContent().stream().filter(hospitalStaffEntity -> hospitalStaffEntity.getStatus() == Status.Active).map(this::mapToDto).toList();
        return new PaginatedResp<>(hospitalStaffEntityPage.getTotalElements(), hospitalStaffEntityPage.getTotalPages(), page, HospitalStaffResList);
    }

    private HospitalStaffEntity mapToEntity(HospitalStaffReq req){
        HospitalStaffEntity entity = new HospitalStaffEntity();
        entity.setStatus(Status.Active);
        entity.setAge(req.getAge());
        entity.setDepartment(req.getDepartment());
        entity.setDesignation(req.getDesignation());
        entity.setGender(req.getGender());
        entity.setStaffName(req.getStaffName());
        return entity;
    }

    private void updateEntityFromDto(HospitalStaffEntity entity, HospitalStaffReq req){
        entity.setAge(req.getAge());
        entity.setDepartment(req.getDepartment());
        entity.setDesignation(req.getDesignation());
        entity.setGender(req.getGender());
        entity.setStaffName(req.getStaffName());
    }

    private HospitalStaffRes mapToDto(HospitalStaffEntity entity){
        HospitalStaffRes res = new HospitalStaffRes();
        res.setId(entity.getId());
        res.setAge(entity.getAge());
        res.setDepartment(entity.getDepartment());
        res.setDesignation(entity.getDesignation());
        res.setGender(entity.getGender());
        res.setStaffName(entity.getStaffName());
        return res;
    }
}
