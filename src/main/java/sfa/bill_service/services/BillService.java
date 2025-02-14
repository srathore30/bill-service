package sfa.bill_service.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sfa.bill_service.constants.ApiErrorCodes;
import sfa.bill_service.constants.BillStatus;
import sfa.bill_service.constants.Status;
import sfa.bill_service.dto.req.BillReq;
import sfa.bill_service.dto.res.BillRes;
import sfa.bill_service.dto.res.PaginatedResp;
import sfa.bill_service.dto.res.PatientsRes;
import sfa.bill_service.entities.BillEntity;
import sfa.bill_service.entities.BillEntryEntity;
import sfa.bill_service.entities.PatientsEntity;
import sfa.bill_service.exceptions.NoSuchElementFoundException;
import sfa.bill_service.repositories.BillEntryRepo;
import sfa.bill_service.repositories.BillRepo;
import sfa.bill_service.repositories.ServicesRepo;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BillService {
    private final BillRepo billRepo;
    private final BillEntryRepo billEntryRepo;
    private final ServicesRepo servicesRepo;


    public BillRes createBill(BillReq billReq) {
        BillEntity billEntity = mapToEntity(billReq);
        return mapToDto(billRepo.save(billEntity));
    }

    public BillRes getBillById(Long id) {
        Optional<BillEntity> optionalBill = billRepo.findById(id);
        if (optionalBill.isEmpty()) {
            throw new NoSuchElementFoundException(ApiErrorCodes.BILL_NOT_FOUND.getErrorCode(), ApiErrorCodes.BILL_NOT_FOUND.getErrorMessage());
        }
        return mapToDto(optionalBill.get());
    }

    public BillRes updateBillById(Long id, BillReq billReq) {
        Optional<BillEntity> optionalBill = billRepo.findById(id);
        if (optionalBill.isEmpty()) {
            throw new NoSuchElementFoundException(ApiErrorCodes.BILL_NOT_FOUND.getErrorCode(), ApiErrorCodes.BILL_NOT_FOUND.getErrorMessage());
        }
        updateEntityFromDto(optionalBill.get(), billReq);
        return mapToDto(billRepo.save(optionalBill.get()));
    }

    public void deleteBillById(Long id) {
        Optional<BillEntity> optionalBill = billRepo.findById(id);
        if (optionalBill.isEmpty()) {
            throw new NoSuchElementFoundException(ApiErrorCodes.BILL_NOT_FOUND.getErrorCode(), ApiErrorCodes.BILL_NOT_FOUND.getErrorMessage());
        }
        optionalBill.get().setStatus(Status.InActive);
        billRepo.save(optionalBill.get());
    }

    public PaginatedResp<BillRes> getAllBills(int page, int pageSize, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<BillEntity> billPage = billRepo.findAll(pageable);
        List<BillRes> billResList = billPage.getContent().stream().filter(bill -> bill.getStatus() == Status.Active).map(this::mapToDto).toList();
        return new PaginatedResp<>(billPage.getTotalElements(), billPage.getTotalPages(), page, billResList);
    }

    private BillEntity mapToEntity(BillReq req) {
        BillEntity entity = new BillEntity();
        entity.setStatus(Status.Active);
        entity.setContactNumber(req.getContactNumber());
        entity.setDate(req.getDate());
        entity.setBillStatus(BillStatus.UNPAID);
        entity.setPaidAmount(req.getPaidAmount() != null ? req.getPaidAmount() : 0.0);

        double totalAmount = req.getBillEntryList().stream().mapToDouble(entry -> entry.getTotalAmount()).sum();
        entity.setTotalAmount(totalAmount);

        if (entity.getPaidAmount() >= totalAmount) {
            entity.setStatus(Status.InActive);
            entity.setBillStatus(BillStatus.PAID);
            entity.setPaidAmount(totalAmount);
        }
        return entity;
    }

    private void updateEntityFromDto(BillEntity entity, BillReq req) {
        entity.setContactNumber(req.getContactNumber());
        entity.setDate(req.getDate());
        entity.setPaidAmount(req.getPaidAmount());

        double totalAmount = req.getBillEntryList().stream().mapToDouble(entry -> entry.getTotalAmount()).sum();
        entity.setTotalAmount(totalAmount);

        if (entity.getPaidAmount() >= totalAmount) {
            entity.setStatus(Status.InActive);
            entity.setBillStatus(BillStatus.PAID);
            entity.setPaidAmount(totalAmount);
        } else {
            entity.setStatus(Status.Active);
            entity.setBillStatus(BillStatus.UNPAID);
        }
    }

    private BillRes mapToDto(BillEntity entity) {
        BillRes res = new BillRes();
        res.setId(entity.getId());
        res.setContactNumber(entity.getContactNumber());
        res.setDate(entity.getDate());
        res.setTotalAmount(entity.getTotalAmount());
        res.setPaidAmount(entity.getPaidAmount());
        res.setBillStatus(entity.getBillStatus());
        res.setPatient(mapPatientEntityToDto(entity.getPatient()));
        return res;
    }

    @Transactional
    public void updatePaidAmount(Long entryId, double paidAmount) {
        BillEntryEntity billEntry = billEntryRepo.findById(entryId)
                .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.BILL_ENTRY_NOT_FOUND.getErrorCode(),
                        ApiErrorCodes.BILL_ENTRY_NOT_FOUND.getErrorMessage()));
        billEntry.setPaidAmount(paidAmount);
        billEntryRepo.save(billEntry);
    }

    @Transactional
    public void finalizeBill(Long contactNumber) {
        List<BillEntity> bills = billRepo.findByContactNumberAndStatus(contactNumber, Status.Active);
        for (BillEntity bill : bills) {
            if (bill.getTotalAmount().equals(bill.getPaidAmount())) {
                bill.setBillStatus(BillStatus.PAID);
                bill.setStatus(Status.InActive);
            } else {
                bill.setBillStatus(BillStatus.UNPAID);
                bill.setStatus(Status.Active);
            }
            billRepo.save(bill);
        }
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
