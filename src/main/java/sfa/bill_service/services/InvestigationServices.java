package sfa.bill_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sfa.bill_service.constants.ApiErrorCodes;
import sfa.bill_service.constants.Status;
import sfa.bill_service.dto.req.InvestigationReq;
import sfa.bill_service.dto.res.InvestigationRes;
import sfa.bill_service.dto.res.PaginatedResp;
import sfa.bill_service.entities.InvestigationEntity;
import sfa.bill_service.exceptions.NoSuchElementFoundException;
import sfa.bill_service.repositories.InvestigationRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InvestigationServices {

    private final InvestigationRepo investigationRepo;

    public InvestigationRes createInvestigation(InvestigationReq investigationReq){
        InvestigationEntity investigationEntity = mapToEntity(investigationReq);
        return mapToDto(investigationRepo.save(investigationEntity));
    }

    public InvestigationRes getInvestigation(Long id){
        Optional<InvestigationEntity> optionalInvestigation = investigationRepo.findById(id);
        if(optionalInvestigation.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.INVESTIGATION_NOT_FOUND.getErrorCode(), ApiErrorCodes.INVESTIGATION_NOT_FOUND.getErrorMessage());
        }
        return mapToDto(optionalInvestigation.get());
    }

    public InvestigationRes updateInvestigationById(Long id, InvestigationReq investigationReq){
        Optional<InvestigationEntity> optionalInvestigation = investigationRepo.findById(id);
        if(optionalInvestigation.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.INVESTIGATION_NOT_FOUND.getErrorCode(), ApiErrorCodes.INVESTIGATION_NOT_FOUND.getErrorMessage());
        }
        updateEntityFromDto(optionalInvestigation.get(), investigationReq);
        return mapToDto(investigationRepo.save(optionalInvestigation.get()));
    }

    public InvestigationRes deleteInvestigationById(Long id){
        Optional<InvestigationEntity> optionalInvestigation = investigationRepo.findById(id);
        if(optionalInvestigation.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.INVESTIGATION_NOT_FOUND.getErrorCode(), ApiErrorCodes.INVESTIGATION_NOT_FOUND.getErrorMessage());
        }
        optionalInvestigation.get().setStatus(Status.InActive);
        return mapToDto(investigationRepo.save(optionalInvestigation.get()));
    }

    public PaginatedResp<InvestigationRes> getAllInvestigation(int page, int pageSize, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<InvestigationEntity> investigationEntityPage = investigationRepo.findAll(pageable);
        List<InvestigationRes> investigationResArrayList;
        investigationResArrayList = investigationEntityPage.getContent().stream().filter(investigationEntity -> investigationEntity.getStatus() == Status.Active).map(this::mapToDto).toList();
        return new PaginatedResp<>(investigationEntityPage.getTotalElements(), investigationEntityPage.getTotalPages(), page, investigationResArrayList);
    }

    private InvestigationEntity mapToEntity(InvestigationReq req){
        InvestigationEntity entity = new InvestigationEntity();
        entity.setInvestigationName(req.getInvestigationName());
        entity.setCost(req.getCost());
        entity.setStatus(Status.Active);
        entity.setDescription(req.getDescription());
        return entity;
    }

    private void updateEntityFromDto(InvestigationEntity entity, InvestigationReq req){
        entity.setInvestigationName(req.getInvestigationName());
        entity.setCost(req.getCost());
        entity.setDescription(req.getDescription());
    }

    private InvestigationRes mapToDto(InvestigationEntity entity){
        InvestigationRes res = new InvestigationRes();
        res.setInvestigationName(entity.getInvestigationName());
        res.setCost(entity.getCost());
        res.setDescription(entity.getDescription());
        return res;
    }
}
