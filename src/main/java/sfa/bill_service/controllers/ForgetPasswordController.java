package sfa.bill_service.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sfa.bill_service.services.ForgetPasswordServices;

@RestController
@RequiredArgsConstructor
@RequestMapping("/forgetPassword")
public class ForgetPasswordController {

    private final ForgetPasswordServices forgetPasswordModule;

    @PostMapping("/sendOtp")
    public ResponseEntity<String> verifyEmail(@RequestParam String email) {
        String res = forgetPasswordModule.sendOtpToEmail(email);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/user/verifyEmailOtp")
    public ResponseEntity<Long> verifyEmailOtp(@RequestParam String username, @RequestParam String otp) {
        Long resp =  forgetPasswordModule.verifyEmailOtp(username, otp);
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @PostMapping("/user/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestParam Long userId, @RequestParam String newPassword) {
        String resp = forgetPasswordModule.resetPassword(newPassword, userId);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

}
