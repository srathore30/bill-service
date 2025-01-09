package sfa.bill_service.services;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sfa.bill_service.constants.ApiErrorCodes;
import sfa.bill_service.constants.Status;
import sfa.bill_service.dto.req.DoctorReq;
import sfa.bill_service.dto.res.PaginatedResp;
import sfa.bill_service.dto.res.DoctorRes;
import sfa.bill_service.entities.DoctorsEntity;
import sfa.bill_service.entities.DoctorsEntity;
import sfa.bill_service.exceptions.NoSuchElementFoundException;
import sfa.bill_service.repositories.DoctorRepo;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorServices {
    
    private final DoctorRepo doctorRepo;

    public DoctorRes createDoctor(DoctorReq doctorReq){
        DoctorsEntity doctorEntity = mapToEntity(doctorReq);
        return mapToDto(doctorRepo.save(doctorEntity));
    }

    public DoctorRes getDoctor(Long id){
        Optional<DoctorsEntity> optionalDoctorsEntity = doctorRepo.findById(id);
        if(optionalDoctorsEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.DOCTOR_NOT_FOUND.getErrorCode(), ApiErrorCodes.DOCTOR_NOT_FOUND.getErrorMessage());
        }
        return mapToDto(optionalDoctorsEntity.get());
    }

    public DoctorRes updateDoctorById(Long id, DoctorReq investigationReq){
        Optional<DoctorsEntity> optionalDoctorsEntity = doctorRepo.findById(id);
        if(optionalDoctorsEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.DOCTOR_NOT_FOUND.getErrorCode(), ApiErrorCodes.DOCTOR_NOT_FOUND.getErrorMessage());
        }
        updateEntityFromDto(optionalDoctorsEntity.get(), investigationReq);
        return mapToDto(doctorRepo.save(optionalDoctorsEntity.get()));
    }

    public void deleteDoctorById(Long id){
        Optional<DoctorsEntity> optionalDoctorsEntity = doctorRepo.findById(id);
        if(optionalDoctorsEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.DOCTOR_NOT_FOUND.getErrorCode(), ApiErrorCodes.DOCTOR_NOT_FOUND.getErrorMessage());
        }
        optionalDoctorsEntity.get().setStatus(Status.InActive);
        doctorRepo.save(optionalDoctorsEntity.get());
    }

    public PaginatedResp<DoctorRes> getAllDoctor(int page, int pageSize, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<DoctorsEntity> doctorEntityPage = doctorRepo.findAll(pageable);
        List<DoctorRes> doctorResList;
        doctorResList = doctorEntityPage.getContent().stream().filter(doctorEntity -> doctorEntity.getStatus() == Status.Active).map(this::mapToDto).toList();
        return new PaginatedResp<>(doctorEntityPage.getTotalElements(), doctorEntityPage.getTotalPages(), page, doctorResList);
    }

    private DoctorsEntity mapToEntity(DoctorReq req){
        DoctorsEntity entity = new DoctorsEntity();
        entity.setStatus(Status.Active);
        entity.setAge(req.getAge());
        entity.setGender(req.getGender());
        entity.setEmail(req.getEmail());
        entity.setDepartment(req.getDepartment());
        entity.setSpecialization(req.getSpecialization());
        entity.setName(req.getName());
        entity.setPhoneNumber(req.getPhoneNumber());
        return entity;
    }

    private void updateEntityFromDto(DoctorsEntity entity, DoctorReq req){
        entity.setAge(req.getAge());
        entity.setGender(req.getGender());
        entity.setEmail(req.getEmail());
        entity.setDepartment(req.getDepartment());
        entity.setSpecialization(req.getSpecialization());
        entity.setName(req.getName());
        entity.setPhoneNumber(req.getPhoneNumber());
    }

    private DoctorRes mapToDto(DoctorsEntity entity){
        DoctorRes res = new DoctorRes();
        res.setId(entity.getId());
        res.setAge(entity.getAge());
        res.setGender(entity.getGender());
        res.setEmail(entity.getEmail());
        res.setDepartment(entity.getDepartment());
        res.setSpecialization(entity.getSpecialization());
        res.setName(entity.getName());
        res.setPhoneNumber(entity.getPhoneNumber());
        return res;
    }
}
