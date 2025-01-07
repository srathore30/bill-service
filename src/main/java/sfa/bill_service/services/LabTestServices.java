package sfa.bill_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sfa.bill_service.constants.ApiErrorCodes;
import sfa.bill_service.constants.LabTestStatus;
import sfa.bill_service.constants.Status;
import sfa.bill_service.dto.req.LabTestReq;
import sfa.bill_service.dto.res.InvestigationRes;
import sfa.bill_service.dto.res.PaginatedResp;
import sfa.bill_service.dto.res.LabTestRes;
import sfa.bill_service.dto.res.PatientsRes;
import sfa.bill_service.entities.InvestigationEntity;
import sfa.bill_service.entities.LabTestEntity;
import sfa.bill_service.entities.PatientsEntity;
import sfa.bill_service.exceptions.NoSuchElementFoundException;
import sfa.bill_service.repositories.InvestigationRepo;
import sfa.bill_service.repositories.LabTestRepo;
import sfa.bill_service.repositories.PatientsRepo;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LabTestServices {

    private final LabTestRepo labTestRepo;
    private final PatientsRepo patientRepo;
    private final InvestigationRepo investigationRepo;

    public LabTestRes createLabTest(LabTestReq labTestReq){
        LabTestEntity labTestEntity = mapToEntity(labTestReq);
        return mapToDto(labTestRepo.save(labTestEntity));
    }

    public LabTestRes getLabTest(Long id){
        Optional<LabTestEntity> optionalLabTestEntity = labTestRepo.findById(id);
        if(optionalLabTestEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.LAB_TEST_NOT_FOUND.getErrorCode(), ApiErrorCodes.LAB_TEST_NOT_FOUND.getErrorMessage());
        }
        return mapToDto(optionalLabTestEntity.get());
    }

    public LabTestRes updateLabTestById(Long id, LabTestReq investigationReq){
        Optional<LabTestEntity> optionalLabTestEntity = labTestRepo.findById(id);
        if(optionalLabTestEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.LAB_TEST_NOT_FOUND.getErrorCode(), ApiErrorCodes.LAB_TEST_NOT_FOUND.getErrorMessage());
        }
        updateEntityFromDto(optionalLabTestEntity.get(), investigationReq);
        return mapToDto(labTestRepo.save(optionalLabTestEntity.get()));
    }

    public void deleteLabTestById(Long id){
        Optional<LabTestEntity> optionalLabTestEntity = labTestRepo.findById(id);
        if(optionalLabTestEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.LAB_TEST_NOT_FOUND.getErrorCode(), ApiErrorCodes.LAB_TEST_NOT_FOUND.getErrorMessage());
        }
        optionalLabTestEntity.get().setStatus(Status.InActive);
        labTestRepo.save(optionalLabTestEntity.get());
    }

    public void updateLabTestStatusById(Long id, LabTestStatus labTestStatus){
        Optional<LabTestEntity> optionalLabTestEntity = labTestRepo.findById(id);
        if(optionalLabTestEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.LAB_TEST_NOT_FOUND.getErrorCode(), ApiErrorCodes.LAB_TEST_NOT_FOUND.getErrorMessage());
        }
        optionalLabTestEntity.get().setLabTestStatus(labTestStatus);
        labTestRepo.save(optionalLabTestEntity.get());
    }

    public PaginatedResp<LabTestRes> getAllLabTestByUserId(Long userId, int page, int pageSize, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<LabTestEntity> labTestEntityPage = labTestRepo.findByUserId(userId, pageable);
        List<LabTestRes> labTestResList;
        labTestResList = labTestEntityPage.getContent().stream().filter(labTestEntity -> labTestEntity.getStatus() == Status.Active).map(this::mapToDto).toList();
        return new PaginatedResp<>(labTestEntityPage.getTotalElements(), labTestEntityPage.getTotalPages(), page, labTestResList);
    }

    public PaginatedResp<LabTestRes> getAllLabTestByUserIdAndLabTestStatus(Long userId, LabTestStatus labTestStatus, int page, int pageSize, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<LabTestEntity> labTestEntityPage = labTestRepo.findByUserIdAndLabTestStatus(userId,labTestStatus, pageable);
        List<LabTestRes> labTestResList;
        labTestResList = labTestEntityPage.getContent().stream().filter(labTestEntity -> labTestEntity.getStatus() == Status.Active).map(this::mapToDto).toList();
        return new PaginatedResp<>(labTestEntityPage.getTotalElements(), labTestEntityPage.getTotalPages(), page, labTestResList);
    }

    private LabTestEntity mapToEntity(LabTestReq req){
        Optional<PatientsEntity> optionalPatientsEntity = patientRepo.findById(req.getPatientId());
        if(optionalPatientsEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.PATIENTS_NOT_FOUND.getErrorCode(), ApiErrorCodes.PATIENTS_NOT_FOUND.getErrorMessage());
        }
        Optional<InvestigationEntity> optionalInvestigation = investigationRepo.findById(req.getInvestigationId());
        if(optionalInvestigation.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.INVESTIGATION_NOT_FOUND.getErrorCode(), ApiErrorCodes.INVESTIGATION_NOT_FOUND.getErrorMessage());
        }
        LabTestEntity entity = new LabTestEntity();
        entity.setLabTestStatus(LabTestStatus.Pending);
        entity.setStatus(Status.Active);
        entity.setUserId(req.getUserId());
        entity.setResult(req.getResult());
        entity.setDatePerformed(req.getDatePerformed());
        entity.setPatient(optionalPatientsEntity.get());
        entity.setInvestigation(optionalInvestigation.get());
        return entity;
    }

    private void updateEntityFromDto(LabTestEntity entity, LabTestReq req){
        Optional<PatientsEntity> optionalPatientsEntity = patientRepo.findById(req.getPatientId());
        if(optionalPatientsEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.PATIENTS_NOT_FOUND.getErrorCode(), ApiErrorCodes.PATIENTS_NOT_FOUND.getErrorMessage());
        }
        Optional<InvestigationEntity> optionalInvestigation = investigationRepo.findById(req.getInvestigationId());
        if(optionalInvestigation.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.INVESTIGATION_NOT_FOUND.getErrorCode(), ApiErrorCodes.INVESTIGATION_NOT_FOUND.getErrorMessage());
        }
        entity.setLabTestStatus(LabTestStatus.Pending);
        entity.setStatus(Status.Active);
        entity.setUserId(req.getUserId());
        entity.setResult(req.getResult());
        entity.setDatePerformed(req.getDatePerformed());
        entity.setPatient(optionalPatientsEntity.get());
        entity.setInvestigation(optionalInvestigation.get());
    }

    private LabTestRes mapToDto(LabTestEntity entity){
        LabTestRes res = new LabTestRes();
        res.setUserId(entity.getUserId());
        res.setId(entity.getId());
        res.setResult(entity.getResult());
        res.setPatientsRes(mapToPatient(entity.getPatient()));
        res.setInvestigationRes(mapToInvestigation(entity.getInvestigation()));
        res.setId(entity.getId());
        res.setDatePerformed(entity.getDatePerformed());
        return res;
    }
    private PatientsRes mapToPatient(PatientsEntity entity){
        PatientsRes res = new PatientsRes();
        res.setUserId(entity.getUserId());
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
    private InvestigationRes mapToInvestigation(InvestigationEntity entity){
        InvestigationRes res = new InvestigationRes();
        res.setInvestigationName(entity.getInvestigationName());
        res.setCost(entity.getCost());
        res.setUserId(entity.getUserId());
        res.setDescription(entity.getDescription());
        return res;
    }
}
