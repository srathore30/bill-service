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
import sfa.bill_service.dto.res.*;
import sfa.bill_service.entities.*;
import sfa.bill_service.exceptions.NoSuchElementFoundException;
import sfa.bill_service.exceptions.ValidationException;
import sfa.bill_service.repositories.*;
import sfa.bill_service.util.UuidGenerator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BillService {
    private final BillRepo billRepo;
    private final ServicesRepo servicesRepo;
    private final PatientsRepo patientsRepo;
    private final RoomRepo roomRepo;


    public BillRes createBill(BillReq billReq) {
        PatientsEntity patient = patientsRepo.findById(billReq.getPatientId())
                .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.PATIENTS_NOT_FOUND.getErrorCode(),
                        ApiErrorCodes.PATIENTS_NOT_FOUND.getErrorMessage()));
        RoomEntity roomEntity = roomRepo.findById(billReq.getRoomId())
                .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.ROOM_NOT_FOUND.getErrorCode(),
                        ApiErrorCodes.ROOM_NOT_FOUND.getErrorMessage()));

        if (billRepo.findByPatientIdAndStatus(billReq.getPatientId(),Status.Active).isPresent()) {
            throw new NoSuchElementFoundException(ApiErrorCodes.ACTIVE_BILL_EXISTS.getErrorCode(),
                    ApiErrorCodes.ACTIVE_BILL_EXISTS.getErrorMessage());
        }
        BillEntity bill = new BillEntity();
        bill.setStatus(Status.Active);
        bill.setPatient(patient);
        bill.setRoomEntity(roomEntity);
        bill.setAdmissionDate(billReq.getAdmissionDate());
        bill.setDischargeDate(billReq.getDischargeDate());
        bill.setBillNo(UuidGenerator.generateUniqueId());
        bill.setContactNumber(patient.getContactNumber());
        bill.setTotalAmount(0.0);
        bill.setReferBy(billReq.getReferBy());
        bill.setPaidAmount(0.0);
        bill.setBillStatus(BillStatus.UNPAID);

        return mapToDto(billRepo.save(bill));
    }

    public BillRes getBillById(Long id) {
        return mapToDto(billRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.BILL_NOT_FOUND.getErrorCode(),
                        ApiErrorCodes.BILL_NOT_FOUND.getErrorMessage())));
    }


    public PaginatedResp<BillRes> getAllBills(int page, int pageSize, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<BillEntity> billEntityPage = billRepo.findAll(pageable);
        List<BillRes> billResList;
        billResList = billEntityPage.getContent().stream().map(this::mapToDto).toList();
        return new PaginatedResp<>(billEntityPage.getTotalElements(), billEntityPage.getTotalPages(), page, billResList);
    }


    @Transactional
    public void finalizeBill(Long patientId) {
        BillEntity bill = billRepo.findByPatientIdAndStatus(patientId, Status.Active)
                .orElseThrow(() -> new ValidationException(ApiErrorCodes.BILL_NOT_FOUND.getErrorCode(),ApiErrorCodes.BILL_NOT_FOUND.getErrorMessage()));
            double totalAmount = 0.0;
            double paidAmount = 0.0;
            for (BillEntryEntity entry : bill.getBillEntries()) {
                totalAmount += entry.getTotalAmount();
                paidAmount += entry.getPaidAmount();
            }
            bill.setTotalAmount(totalAmount);
            bill.setPaidAmount(paidAmount);

            if (paidAmount == totalAmount) {
                bill.setBillStatus(BillStatus.PAID);
                bill.setStatus(Status.InActive);
            } else {
                bill.setBillStatus(BillStatus.UNPAID);
            }

            billRepo.save(bill);
    }

    public List<BillRes> getBillByContactNumber(Long contactNumber) {
        return billRepo.findByContactNumber(contactNumber).stream().map(this::mapToDto).toList();
    }

    private BillRes mapToDto(BillEntity bill){
        BillRes billRes = new BillRes();
        billRes.setId(bill.getId());
        billRes.setReferBy(bill.getReferBy());
        billRes.setBillNo(bill.getBillNo());
        billRes.setRoomRes(mapToRoomRes(bill.getRoomEntity()));
        billRes.setPatientsRes(mapPatientEntityToDto(bill.getPatient()));
        billRes.setAdmissionDate(bill.getAdmissionDate());
        billRes.setDischargeDate(bill.getDischargeDate());
        billRes.setTotalAmount(bill.getTotalAmount());
        billRes.setPaidAmount(bill.getPaidAmount());
        billRes.setBillEntryResList(bill.getBillEntries().stream().map(this::mapToBillEntryRes).toList());
        return billRes;
    }

    private BillEntryRes mapToBillEntryRes(BillEntryEntity entity) {
        BillEntryRes res = new BillEntryRes();
        res.setId(entity.getId());
        res.setContactNumber(entity.getContactNumber());
        res.setDate(new Date());
        res.setTotalAmount(entity.getTotalAmount());
        res.setPaidAmount(entity.getPaidAmount());
        res.setServiceList(entity.getServiceEntityList().stream().map(this::mapToServices).toList());
        res.setBillStatus(entity.getBillStatus());
        res.setRemainingAmount(entity.getTotalAmount() - entity.getPaidAmount());
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
    private RoomRes mapToRoomRes(RoomEntity entity){
        RoomRes res = new RoomRes();
        res.setId(entity.getId());
        res.setRoomType(entity.getRoomType());
        return res;
    }

    private ServicesRes mapToServices(MedicalServicesEntity entity){
        ServicesRes res = new ServicesRes();
        res.setServiceCode(entity.getServiceCode());
        res.setCghsLocation(entity.getServiceCategory().getCghsLocation());
        res.setServiceName(entity.getServiceName());
        res.setModifiedDate(entity.getModifiedDate());
        res.setCreatedTime(entity.getCreatedTime());
        res.setCategoryName(entity.getServiceCategory().getName());
        res.setNablRate(entity.getNablRate());
        res.setNonNablRate(entity.getNonNablRate());
        res.setId(entity.getId());
        return res;
    }
}
