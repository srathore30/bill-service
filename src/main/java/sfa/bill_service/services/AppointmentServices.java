package sfa.bill_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sfa.bill_service.constants.ApiErrorCodes;
import sfa.bill_service.constants.AppointmentStatus;
import sfa.bill_service.constants.Status;
import sfa.bill_service.dto.req.AppointmentReq;
import sfa.bill_service.dto.res.PaginatedResp;
import sfa.bill_service.dto.res.AppointmentRes;
import sfa.bill_service.entities.AppointmentEntity;
import sfa.bill_service.entities.DoctorsEntity;
import sfa.bill_service.entities.PatientsEntity;
import sfa.bill_service.exceptions.NoSuchElementFoundException;
import sfa.bill_service.repositories.AppointmentRepo;
import sfa.bill_service.repositories.DoctorRepo;
import sfa.bill_service.repositories.PatientsRepo;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentServices {

    private final AppointmentRepo appointmentRepo;
    private final PatientsRepo patientsRepo;
    private final DoctorRepo doctorRepo;

    public AppointmentRes createAppointment(AppointmentReq appointmentReq){
        AppointmentEntity appointmentEntity = mapToEntity(appointmentReq);
        return mapToDto(appointmentRepo.save(appointmentEntity));
    }

    public AppointmentRes getAppointment(Long id){
        Optional<AppointmentEntity> optionalAppointmentEntity = appointmentRepo.findById(id);
        if(optionalAppointmentEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.APPOINTMENT_NOT_FOUND.getErrorCode(), ApiErrorCodes.APPOINTMENT_NOT_FOUND.getErrorMessage());
        }
        return mapToDto(optionalAppointmentEntity.get());
    }

    public void deleteAppointmentById(Long id){
        Optional<AppointmentEntity> optionalAppointmentEntity = appointmentRepo.findById(id);
        if(optionalAppointmentEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.APPOINTMENT_NOT_FOUND.getErrorCode(), ApiErrorCodes.APPOINTMENT_NOT_FOUND.getErrorMessage());
        }
        optionalAppointmentEntity.get().setStatus(Status.InActive);
        appointmentRepo.save(optionalAppointmentEntity.get());
    }

    public void changeAppointmentStatus(Long id, AppointmentStatus appointmentStatus){
        Optional<AppointmentEntity> optionalAppointmentEntity = appointmentRepo.findById(id);
        if(optionalAppointmentEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.APPOINTMENT_NOT_FOUND.getErrorCode(), ApiErrorCodes.APPOINTMENT_NOT_FOUND.getErrorMessage());
        }
        optionalAppointmentEntity.get().setAppointmentStatus(appointmentStatus);
        appointmentRepo.save(optionalAppointmentEntity.get());
    }

    public PaginatedResp<AppointmentRes> getAllRAppointmentByUserId(Long userId, int page, int pageSize, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<AppointmentEntity> appointmentEntityPage = appointmentRepo.findByUserId(userId, pageable);
        List<AppointmentRes> appointmentResList;
        appointmentResList = appointmentEntityPage.getContent().stream().filter(appointmentEntity -> appointmentEntity.getStatus() == Status.Active).map(this::mapToDto).toList();
        return new PaginatedResp<>(appointmentEntityPage.getTotalElements(), appointmentEntityPage.getTotalPages(), page, appointmentResList);
    }

    public PaginatedResp<AppointmentRes> getAllAppointmentByUserIdAndStatus(Long userId, AppointmentStatus appointmentStatus,int page, int pageSize, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<AppointmentEntity> appointmentEntityPage = appointmentRepo.findByUserIdAndAppointmentStatus(userId, appointmentStatus, pageable);
        List<AppointmentRes> appointmentResList;
        appointmentResList = appointmentEntityPage.getContent().stream().filter(appointmentEntity -> appointmentEntity.getStatus() == Status.Active).map(this::mapToDto).toList();
        return new PaginatedResp<>(appointmentEntityPage.getTotalElements(), appointmentEntityPage.getTotalPages(), page, appointmentResList);
    }

    private AppointmentEntity mapToEntity(AppointmentReq req){
        Optional<PatientsEntity> optionalPatientsEntity = patientsRepo.findById(req.getPatientId());
        if(optionalPatientsEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.PATIENTS_NOT_FOUND.getErrorCode(), ApiErrorCodes.PATIENTS_NOT_FOUND.getErrorMessage());
        }
        Optional<DoctorsEntity> optionalDoctorsEntity = doctorRepo.findById(req.getDoctorId());
        if(optionalDoctorsEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.DOCTOR_NOT_FOUND.getErrorCode(), ApiErrorCodes.DOCTOR_NOT_FOUND.getErrorMessage());
        }
        AppointmentEntity entity = new AppointmentEntity();
        entity.setStatus(Status.Active);
        entity.setUserId(req.getUserId());
        entity.setAppointmentDate(req.getAppointmentDate());
        entity.setAppointmentStatus(req.getAppointmentStatus());
        entity.setDoctor(optionalDoctorsEntity.get());
        entity.setPatient(optionalPatientsEntity.get());
        return entity;
    }

    private AppointmentRes mapToDto(AppointmentEntity entity){
        AppointmentRes res = new AppointmentRes();
        res.setUserId(entity.getUserId());
        res.setId(entity.getId());
        res.setAppointmentDate(entity.getAppointmentDate());
        res.setAppointmentStatus(entity.getAppointmentStatus());
        res.setId(entity.getId());
        res.setDoctor(entity.getDoctor());
        res.setPatient(entity.getPatient());
        return res;
    }
}
