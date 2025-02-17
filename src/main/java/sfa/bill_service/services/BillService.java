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
import sfa.bill_service.dto.res.PaginatedResp;
import sfa.bill_service.dto.res.PatientsRes;
import sfa.bill_service.entities.BillEntity;
import sfa.bill_service.entities.BillEntryEntity;
import sfa.bill_service.entities.PatientsEntity;
import sfa.bill_service.exceptions.NoSuchElementFoundException;
import sfa.bill_service.exceptions.ValidationException;
import sfa.bill_service.repositories.BillEntryRepo;
import sfa.bill_service.repositories.BillRepo;
import sfa.bill_service.repositories.PatientsRepo;
import sfa.bill_service.repositories.ServicesRepo;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BillService {
    private final BillRepo billRepo;
    private final ServicesRepo servicesRepo;
    private final PatientsRepo patientsRepo;


    public BillEntity createBill(Long patientId) {
        PatientsEntity patient = patientsRepo.findById(patientId)
                .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.PATIENTS_NOT_FOUND.getErrorCode(),
                        ApiErrorCodes.PATIENTS_NOT_FOUND.getErrorMessage()));

        if (billRepo.findByPatientIdAndStatus(patientId,Status.Active).isPresent()) {
            throw new NoSuchElementFoundException(ApiErrorCodes.ACTIVE_BILL_EXISTS.getErrorCode(),
                    ApiErrorCodes.ACTIVE_BILL_EXISTS.getErrorMessage());
        }
        BillEntity bill = new BillEntity();
        bill.setStatus(Status.Active);
        bill.setPatient(patient);
        bill.setContactNumber(patient.getContactNumber());
        bill.setTotalAmount(0.0);
        bill.setPaidAmount(0.0);
        bill.setBillStatus(BillStatus.UNPAID);

        return billRepo.save(bill);
    }

    public BillEntity getBillById(Long id) {
        return billRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.BILL_NOT_FOUND.getErrorCode(),
                        ApiErrorCodes.BILL_NOT_FOUND.getErrorMessage()));
    }


    public Page<BillEntity> getAllBills(int page, int pageSize, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        return billRepo.findAll(pageable);
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

    public List<BillEntity> getBillByContactNumber(Long contactNumber) {
        return billRepo.findByContactNumber(contactNumber);
    }

}
