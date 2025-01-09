package sfa.bill_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sfa.bill_service.constants.ApiErrorCodes;
import sfa.bill_service.constants.Status;
import sfa.bill_service.dto.req.PatientsReq;
import sfa.bill_service.dto.res.PaginatedResp;
import sfa.bill_service.dto.res.PatientsRes;
import sfa.bill_service.entities.PatientsEntity;
import sfa.bill_service.exceptions.NoSuchElementFoundException;
import sfa.bill_service.repositories.PatientsRepo;
import sfa.bill_service.repositories.RoomRepo;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientServices {

    private final PatientsRepo patientRepo;

    public PatientsRes createPatients(PatientsReq patientsReq){
        PatientsEntity patientsEntity = mapToEntity(patientsReq);
        return mapToDto(patientRepo.save(patientsEntity));
    }

    public PatientsRes getPatients(Long id){
        Optional<PatientsEntity> optionalPatientsEntity = patientRepo.findById(id);
        if(optionalPatientsEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.PATIENTS_NOT_FOUND.getErrorCode(), ApiErrorCodes.PATIENTS_NOT_FOUND.getErrorMessage());
        }
        return mapToDto(optionalPatientsEntity.get());
    }

    public PatientsRes updatePatientsById(Long id, PatientsReq investigationReq){
        Optional<PatientsEntity> optionalPatientsEntity = patientRepo.findById(id);
        if(optionalPatientsEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.PATIENTS_NOT_FOUND.getErrorCode(), ApiErrorCodes.PATIENTS_NOT_FOUND.getErrorMessage());
        }
        updateEntityFromDto(optionalPatientsEntity.get(), investigationReq);
        return mapToDto(patientRepo.save(optionalPatientsEntity.get()));
    }

    public void deletePatientsById(Long id){
        Optional<PatientsEntity> optionalPatientsEntity = patientRepo.findById(id);
        if(optionalPatientsEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.PATIENTS_NOT_FOUND.getErrorCode(), ApiErrorCodes.PATIENTS_NOT_FOUND.getErrorMessage());
        }
        optionalPatientsEntity.get().setStatus(Status.InActive);
        patientRepo.save(optionalPatientsEntity.get());
    }

    public PaginatedResp<PatientsRes> getAllPatients(int page, int pageSize, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<PatientsEntity> patientsEntityPage = patientRepo.findAll(pageable);
        List<PatientsRes> patientsResList;
        patientsResList = patientsEntityPage.getContent().stream().filter(patientsEntity -> patientsEntity.getStatus() == Status.Active).map(this::mapToDto).toList();
        return new PaginatedResp<>(patientsEntityPage.getTotalElements(), patientsEntityPage.getTotalPages(), page, patientsResList);
    }

    private PatientsEntity mapToEntity(PatientsReq req){
        PatientsEntity entity = new PatientsEntity();
        entity.setStatus(Status.Active);
        entity.setAge(req.getAge());
        entity.setGender(req.getGender());
        entity.setUserRoleList(req.getUserRoleList());
        entity.setEmail(req.getEmail());
        entity.setAllergies(req.getAllergies());
        entity.setAge(req.getAge());
        entity.setBloodGroup(req.getBloodGroup());
        entity.setName(req.getName());
        entity.setContactNumber(req.getContactNumber());
        entity.setEmergencyContactName(req.getEmergencyContactName());
        entity.setEmergencyContactNumber(req.getEmergencyContactNumber());
        return entity;
    }

    private void updateEntityFromDto(PatientsEntity entity, PatientsReq req){
        entity.setAge(req.getAge());
        entity.setGender(req.getGender());
        entity.setUserRoleList(req.getUserRoleList());
        entity.setEmail(req.getEmail());
        entity.setAllergies(req.getAllergies());
        entity.setAge(req.getAge());
        entity.setBloodGroup(req.getBloodGroup());
        entity.setName(req.getName());
        entity.setContactNumber(req.getContactNumber());
        entity.setEmergencyContactName(req.getEmergencyContactName());
        entity.setEmergencyContactNumber(req.getEmergencyContactNumber());
    }

    private PatientsRes mapToDto(PatientsEntity entity){
        PatientsRes res = new PatientsRes();
        res.setAge(entity.getAge());
        res.setGender(entity.getGender());
        res.setUserRoleList(entity.getUserRoleList());
        res.setEmail(entity.getEmail());
        res.setAllergies(entity.getAllergies());
        res.setId(entity.getId());
        res.setAge(entity.getAge());
        res.setBloodGroup(entity.getBloodGroup());
        res.setName(entity.getName());
        res.setContactNumber(entity.getContactNumber());
        res.setEmergencyContactName(entity.getEmergencyContactName());
        res.setEmergencyContactNumber(entity.getEmergencyContactNumber());
        return res;
    }
}
