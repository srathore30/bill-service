package sfa.bill_service.constants;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;


public enum ApiErrorCodes implements Error {

    SUCCESS(0, "Success"),

    INVALID_INPUT(11101, "Invalid request input"),
    INVALID_MOBILE_NUMBER(11102, "Invalid mobile number"),
    USER_NOT_FOUND(11104, "User not found"),
    INVALID_USERNAME_OR_PASSWORD(11105, "Invalid username or password" ),
    ALREADY_EXIST(11108, "user already exist"),
    MEDICATION_NOT_FOUND(11110, "Medication not found"),
    INVESTIGATION_NOT_FOUND(11112, "Investigation not found"),
    ROOM_NOT_FOUND(11152, "Room not found"),
    SERVICES_NOT_FOUND(11152, "Services not found"),
    STAFF_NOT_FOUND(11245, "Staff not found"),
    DOCTOR_NOT_FOUND(11245, "Doctor not found"),
    PATIENTS_NOT_FOUND(11245, "Patients not found"),
    APPOINTMENT_NOT_FOUND(11245, "Appointment not found");




    private int errorCode;
   private String errorMessage;
    private  HttpStatus status;
    private  String message;
    private  LocalDateTime timestamp;


    ApiErrorCodes(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }
}
