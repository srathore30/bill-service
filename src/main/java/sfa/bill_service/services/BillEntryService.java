package sfa.bill_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sfa.bill_service.constants.ApiErrorCodes;
import sfa.bill_service.constants.BillStatus;
import sfa.bill_service.constants.Status;
import sfa.bill_service.dto.req.BillEntryReq;
import sfa.bill_service.dto.res.BillEntryRes;
import sfa.bill_service.dto.res.PatientsRes;
import sfa.bill_service.entities.BillEntryEntity;
import sfa.bill_service.entities.PatientsEntity;
import sfa.bill_service.entities.ServiceCategory;
import sfa.bill_service.entities.ServicesEntity;
import sfa.bill_service.exceptions.NoSuchElementFoundException;
import sfa.bill_service.repositories.BillEntryRepo;
import sfa.bill_service.repositories.ServicesRepo;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BillEntryService {
    private final BillEntryRepo billEntryRepo;
    private final ServicesRepo servicesRepo;

    public BillEntryRes createBillEntry(BillEntryReq billEntryReq) {
        BillEntryEntity billEntryEntity = mapToEntity(billEntryReq);
        return mapToDto(billEntryRepo.save(billEntryEntity));
    }

    public BillEntryRes getBillEntryById(Long id) {
        Optional<BillEntryEntity> optionalBillEntry = billEntryRepo.findById(id);
        if (optionalBillEntry.isEmpty()) {
            throw new NoSuchElementFoundException(ApiErrorCodes.BILL_ENTRY_NOT_FOUND.getErrorCode(), ApiErrorCodes.BILL_ENTRY_NOT_FOUND.getErrorMessage());
        }
        return mapToDto(optionalBillEntry.get());
    }

    public BillEntryRes updateBillEntryById(Long id, BillEntryReq billEntryReq) {
        Optional<BillEntryEntity> optionalBillEntry = billEntryRepo.findById(id);
        if (optionalBillEntry.isEmpty()) {
            throw new NoSuchElementFoundException(ApiErrorCodes.BILL_ENTRY_NOT_FOUND.getErrorCode(), ApiErrorCodes.BILL_ENTRY_NOT_FOUND.getErrorMessage());
        }
        updateEntityFromDto(optionalBillEntry.get(), billEntryReq);
        return mapToDto(billEntryRepo.save(optionalBillEntry.get()));
    }

    public List<BillEntryRes> getAllBillEntries() {
        return billEntryRepo.findAll().stream()
                .filter(entry -> entry.getStatus() == Status.Active)
                .map(this::mapToDto)
                .toList();
    }

    private BillEntryEntity mapToEntity(BillEntryReq req) {
        BillEntryEntity entity = new BillEntryEntity();
        entity.setContactNumber(req.getContactNumber());
        entity.setDate(req.getDate());
        entity.setPaidAmount(req.getPaidAmount() != null ? req.getPaidAmount() : 0.0);

        PatientsEntity patient = new PatientsEntity();
        patient.setId(req.getPatientId());
        entity.setPatient(patient);
        boolean isNabl = patient != null && patient.isNabl();

        List<ServicesEntity> services = servicesRepo.findAllById(req.getServiceIds());
        double totalAmount = services.stream()
                .mapToDouble(service -> {
                    ServiceCategory category = service.getServiceCategory();
                    if (service.getStatus() == Status.Active && category != null) {
                        return category.getStatus() == Status.Active ? service.getNablRate() : service.getNonNablRate();
                    }
                    return 0.0;
                })
                .sum();

        entity.setTotalAmount(totalAmount);
        entity.setServiceEntityList(services);

        if (entity.getPaidAmount() >= totalAmount) {
            entity.setStatus(Status.InActive);
            entity.setBillStatus(BillStatus.PAID);
            entity.setPaidAmount(totalAmount);
        } else {
            entity.setStatus(Status.Active);
            entity.setBillStatus(BillStatus.UNPAID);
        }

        return entity;
    }

    private void updateEntityFromDto(BillEntryEntity entity, BillEntryReq req) {
        entity.setContactNumber(req.getContactNumber());
        entity.setDate(req.getDate());
        entity.setTotalAmount(req.getTotalAmount());
        entity.setPaidAmount(req.getPaidAmount());
        entity.setBillStatus(req.getBillStatus());
    }

    private BillEntryRes mapToDto(BillEntryEntity entity) {
        BillEntryRes res = new BillEntryRes();
        res.setId(entity.getId());
        res.setContactNumber(entity.getContactNumber());
        res.setDate(entity.getDate());
        res.setTotalAmount(entity.getTotalAmount());
        res.setPaidAmount(entity.getPaidAmount());
        res.setBillStatus(entity.getBillStatus());
        res.setRemainingAmount(entity.getTotalAmount() - entity.getPaidAmount());
        res.setPatient(mapPatientEntityToDto(entity.getPatient()));

        return res;
    }

    private PatientsRes mapPatientEntityToDto(PatientsEntity entity){
        PatientsRes res = new PatientsRes();
        res.setAge(entity.getAge());
        res.setGender(entity.getGender());
        res.setUserRoleList(entity.getUserRoleList());
        res.setEmail(entity.getEmail());
        res.setAllergies(entity.getAllergies());
        res.setId(entity.getId());
        res.setAddress(entity.getAddress());
        res.setAge(entity.getAge());
        res.setBloodGroup(entity.getBloodGroup());
        res.setName(entity.getName());
        res.setContactNumber(entity.getContactNumber());
        res.setEmergencyContactName(entity.getEmergencyContactName());
        res.setEmergencyContactNumber(entity.getEmergencyContactNumber());
        res.setNabl(entity.isNabl());
        return res;
    }
}
