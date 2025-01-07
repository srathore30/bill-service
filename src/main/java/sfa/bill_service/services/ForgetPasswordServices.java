package sfa.bill_service.services;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sfa.bill_service.configs.AuthConfig;
import sfa.bill_service.constants.ApiErrorCodes;
import sfa.bill_service.email.EmailOtpService;
import sfa.bill_service.entities.UserEntity;
import sfa.bill_service.exceptions.NoSuchElementFoundException;
import sfa.bill_service.repositories.UserRepo;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ForgetPasswordServices {

    private final AuthConfig authConfig;
    private final EmailOtpService emailOtpService;
    private final UserRepo userRepo;

    public String sendOtpToEmail(String username) {
        Optional<UserEntity> optionalUserEntity = userRepo.findByUsername(username);
        if(optionalUserEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.USER_NOT_FOUND.getErrorCode(), ApiErrorCodes.USER_NOT_FOUND.getErrorMessage());
        }
        emailOtpService.sendEmailCode(optionalUserEntity.get().getEmail());
        return "otp send";
    }


    public Long verifyEmailOtp(String username, String otp) {
        Optional<UserEntity> optionalUserEntity = userRepo.findByUsername(username);
        if(optionalUserEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.USER_NOT_FOUND.getErrorCode(), ApiErrorCodes.USER_NOT_FOUND.getErrorMessage());
        }
        boolean isValid = emailOtpService.verifyEmailCode(username, otp);
        if(!isValid){
            throw new NoSuchElementFoundException(ApiErrorCodes.INVALID_EMAIL_CODE.getErrorCode(), ApiErrorCodes.INVALID_EMAIL_CODE.getErrorMessage());
        }
        return optionalUserEntity.get().getId();
    }

    public String resetPassword(String newPassword, Long userId) {
        Optional<UserEntity> optionalUserEntity = userRepo.findById(userId);
        if(optionalUserEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.USER_NOT_FOUND.getErrorCode(), ApiErrorCodes.USER_NOT_FOUND.getErrorMessage());
        }
        try{
            UserEntity members = optionalUserEntity.get();
            members.setPassword(authConfig.passwordEncoder().encode(newPassword));
            userRepo.save(members);
        }catch (Exception e){
            throw new NoSuchElementFoundException(ApiErrorCodes.CANNOT_RESET_PASSWORD.getErrorCode(), ApiErrorCodes.CANNOT_RESET_PASSWORD.getErrorMessage());
        }
        return "password changed";
    }




}
