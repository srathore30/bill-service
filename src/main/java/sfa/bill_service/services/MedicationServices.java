package sfa.bill_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sfa.bill_service.constants.ApiErrorCodes;
import sfa.bill_service.constants.Status;
import sfa.bill_service.dto.req.MedicationReq;
import sfa.bill_service.dto.res.MedicationRes;
import sfa.bill_service.dto.res.PaginatedResp;
import sfa.bill_service.entities.MedicationEntity;
import sfa.bill_service.exceptions.NoSuchElementFoundException;
import sfa.bill_service.repositories.MedicationRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicationServices {

    private final MedicationRepo medicationRepo;

    public MedicationRes createMedication(MedicationReq medicationReq){
        MedicationEntity medicationEntity = mapToEntity(medicationReq);
        return mapToDto(medicationRepo.save(medicationEntity));
    }

    public MedicationRes getMedicationById(Long id){
        Optional<MedicationEntity> optionalMedicationEntity = medicationRepo.findById(id);
        if(optionalMedicationEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.MEDICATION_NOT_FOUND.getErrorCode(), ApiErrorCodes.MEDICATION_NOT_FOUND.getErrorMessage());
        }
        return mapToDto(optionalMedicationEntity.get());
    }

    public void deleteMedicationById(Long id){
        Optional<MedicationEntity> optionalMedicationEntity = medicationRepo.findById(id);
        if(optionalMedicationEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.MEDICATION_NOT_FOUND.getErrorCode(), ApiErrorCodes.MEDICATION_NOT_FOUND.getErrorMessage());
        }
        optionalMedicationEntity.get().setStatus(Status.InActive);
        medicationRepo.save(optionalMedicationEntity.get());
    }

    public PaginatedResp<MedicationRes> getAllMedicationByUserId(Long userId, int page, int pageSize, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<MedicationEntity> medicationEntityPage = medicationRepo.findByUserId(userId, pageable);
        List<MedicationRes> medicationResList = new ArrayList<>();
        medicationResList = medicationEntityPage.getContent().stream().filter(medicationEntity -> medicationEntity.getStatus() == Status.Active).map(this::mapToDto).toList();
        return new PaginatedResp<>(medicationEntityPage.getTotalElements(), medicationEntityPage.getTotalPages(), page, medicationResList);
    }


    public MedicationRes updateMedication(Long id, MedicationReq medicationReq){
        Optional<MedicationEntity> optionalMedicationEntity = medicationRepo.findById(id);
        if(optionalMedicationEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.MEDICATION_NOT_FOUND.getErrorCode(), ApiErrorCodes.MEDICATION_NOT_FOUND.getErrorMessage());
        }
        updateEntityFromDto(optionalMedicationEntity.get(), medicationReq);
        return mapToDto(medicationRepo.save(optionalMedicationEntity.get()));
    }


    private MedicationRes mapToDto(MedicationEntity medicationEntity){
        MedicationRes medicationRes = new MedicationRes();
        medicationRes.setId(medicationEntity.getId());
        medicationRes.setDosage(medicationEntity.getDosage());
        medicationRes.setDescription(medicationEntity.getDescription());
        medicationRes.setMedicineName(medicationEntity.getMedicineName());
        return medicationRes;
    }

    private MedicationEntity mapToEntity(MedicationReq medicationReq){
        MedicationEntity medicationEntity = new MedicationEntity();
        medicationEntity.setDosage(medicationReq.getDosage());
        medicationEntity.setStatus(Status.Active);
        medicationEntity.setUserId(medicationReq.getUserId());
        medicationEntity.setDescription(medicationReq.getDescription());
        medicationEntity.setMedicineName(medicationReq.getMedicineName());
        return medicationEntity;
    }

    private void updateEntityFromDto(MedicationEntity medicationEntity, MedicationReq medicationReq){
        medicationEntity.setDosage(medicationReq.getDosage());
        medicationEntity.setDescription(medicationReq.getDescription());
        medicationEntity.setMedicineName(medicationReq.getMedicineName());
    }
}
