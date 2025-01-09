package sfa.bill_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sfa.bill_service.auth_utils.JwtHelper;
import sfa.bill_service.configs.AuthConfig;
import sfa.bill_service.constants.ApiErrorCodes;
import sfa.bill_service.constants.Status;
import sfa.bill_service.constants.UserRole;
import sfa.bill_service.dto.req.UserReq;
import sfa.bill_service.dto.res.UserRes;
import sfa.bill_service.entities.UserDetailsEntity;
import sfa.bill_service.entities.UserEntity;
import sfa.bill_service.exceptions.NoSuchElementFoundException;
import sfa.bill_service.exceptions.ValidationException;
import sfa.bill_service.repositories.UserRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServices implements UserDetailsService {

    private final UserRepo userRepo;
    private final AuthConfig authConfig;
    private final JwtHelper helper;

    public UserRes createUser(UserReq userReq){
        Optional<UserEntity> optionalUserEntity = userRepo.findByUsername(userReq.getUsername());
        if(optionalUserEntity.isPresent()){
            throw new ValidationException(ApiErrorCodes.ALREADY_EXIST.getErrorCode(), ApiErrorCodes.ALREADY_EXIST.getErrorMessage());
        }
        UserEntity user = mapToEntity(userReq);
        return mapToDto(userRepo.save(user));
    }

    public UserRes getUserById(Long id){
        Optional<UserEntity> optionalUserEntity = userRepo.findById(id);
        if(optionalUserEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.USER_NOT_FOUND.getErrorCode(), ApiErrorCodes.USER_NOT_FOUND.getErrorMessage());
        }
        return mapToDto(optionalUserEntity.get());
    }

    public UserRes updateUserById(Long id, UserReq userReq){
        Optional<UserEntity> optionalUserEntity = userRepo.findById(id);
        if(optionalUserEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.USER_NOT_FOUND.getErrorCode(), ApiErrorCodes.USER_NOT_FOUND.getErrorMessage());
        }
        updateEntityFromDto(optionalUserEntity.get(), userReq);
        userRepo.save(optionalUserEntity.get());
        return mapToDto(userRepo.save(optionalUserEntity.get()));
    }

    public String loginUser(String username, String password){
        UserDetails userDetails = loadUserByUsername(username);
        if(AuthConfig.matches(password, userDetails.getPassword())){
            return helper.generateToken(userDetails);
        }
        throw new NoSuchElementFoundException(ApiErrorCodes.INVALID_USERNAME_OR_PASSWORD.getErrorCode(), ApiErrorCodes.INVALID_USERNAME_OR_PASSWORD.getErrorMessage());
    }

    private UserRes mapToDto(UserEntity userEntity){
        UserRes userRes = new UserRes();
        userRes.setUserStatus(userEntity.getUserStatus());
        userRes.setName(userEntity.getName());
        userRes.setId(userEntity.getId());
        userRes.setEmail(userEntity.getEmail());
        userRes.setMobileNo(userEntity.getMobileNo());
        userRes.setUsername(userEntity.getUsername());
        return userRes;
    }

    private UserEntity mapToEntity(UserReq userReq){
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(authConfig.passwordEncoder().encode(userReq.getPassword()));
        userEntity.setUserRoleList(userReq.getUserRoleList());
        userEntity.setUsername(userReq.getUsername());
        userEntity.setName(userReq.getName());
        userEntity.setUserStatus(Status.Active);
        userEntity.setEmail(userReq.getEmail());
        userEntity.setMobileNo(userReq.getMobileNo());
        return userEntity;
    }

    private void updateEntityFromDto(UserEntity userEntity, UserReq userReq){
        userEntity.setName(userReq.getName());
        userEntity.setEmail(userReq.getEmail());
        userEntity.setUsername(userReq.getUsername());
        userEntity.setMobileNo(userReq.getMobileNo());
    }

    private UserDetailsEntity mapToUserDetails(UserEntity userEntity){
        UserDetailsEntity userDetails = new UserDetailsEntity();
        userDetails.setPassword(userEntity.getPassword());
        userDetails.setUsername(userEntity.getUsername());
        return userDetails;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> optionalUserEntity = userRepo.findByUsername(username);
        if(optionalUserEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.INVALID_USERNAME_OR_PASSWORD.getErrorCode(), ApiErrorCodes.INVALID_USERNAME_OR_PASSWORD.getErrorMessage());
        }
        return mapToUserDetails(optionalUserEntity.get());
    }
}
