package sfa.bill_service.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sfa.bill_service.constants.ApiErrorCodes;
import sfa.bill_service.constants.BillStatus;
import sfa.bill_service.constants.Status;
import sfa.bill_service.dto.req.BillEntryReq;
import sfa.bill_service.dto.res.BillEntryRes;
import sfa.bill_service.dto.res.PatientsRes;
import sfa.bill_service.entities.*;
import sfa.bill_service.exceptions.NoSuchElementFoundException;
import sfa.bill_service.repositories.BillEntryRepo;
import sfa.bill_service.repositories.BillRepo;
import sfa.bill_service.repositories.ServicesRepo;
import sfa.bill_service.util.UuidGenerator;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BillEntryService {
    private final BillEntryRepo billEntryRepo;
    private final ServicesRepo servicesRepo;
    private final BillRepo billRepo;

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

    public List<BillEntryRes> getAllBillEntries() {
        return billEntryRepo.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    private BillEntryEntity mapToEntity(BillEntryReq req) {
        BillEntryEntity entity = new BillEntryEntity();
        entity.setPaidAmount(0.0);
        BillEntity billEntity = billRepo.findById(req.getBillId())
                .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.BILL_NOT_FOUND.getErrorCode(),ApiErrorCodes.BILL_NOT_FOUND.getErrorMessage()));
        entity.setPatient(billEntity.getPatient());

        boolean isNabl = billEntity.getPatient().isNabl();
        List<MedicalServicesEntity> services = servicesRepo.findAllById(req.getServiceIds());
        double totalAmount = services.stream()
                .mapToDouble(service -> isNabl ? service.getNablRate() : service.getNonNablRate())
                .sum();

        entity.setTotalAmount(totalAmount);
        entity.setServiceEntityList(services);
        entity.setStatus(Status.Active);
        entity.setBillNo(UuidGenerator.generateUniqueId());
        entity.setBillStatus(BillStatus.UNPAID);
        return entity;
    }


    @Transactional
    public void updatePaidAmount(Long entryId, double paidAmount) {
        BillEntryEntity billEntry = billEntryRepo.findById(entryId)
                .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.BILL_ENTRY_NOT_FOUND.getErrorCode(),
                        ApiErrorCodes.BILL_ENTRY_NOT_FOUND.getErrorMessage()));

        billEntry.setPaidAmount(paidAmount);
        if (paidAmount == billEntry.getTotalAmount()) {
            billEntry.setBillStatus(BillStatus.PAID);
        } else {
            billEntry.setBillStatus(BillStatus.UNPAID);
        }

        billEntryRepo.save(billEntry);
    }


    private BillEntryRes mapToDto(BillEntryEntity entity) {
        BillEntryRes res = new BillEntryRes();
        res.setId(entity.getId());
        res.setContactNumber(entity.getContactNumber());
        res.setDate(new Date());
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
