package sfa.bill_service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import sfa.bill_service.constants.ApiErrorCodes;
import sfa.bill_service.exceptions.NoSuchElementFoundException;

import java.io.File;


@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    public void sendEmailOtp(String otp, String toEmail){
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject("Email verification");
            helper.setText("Verification code for your account is " + otp);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new NoSuchElementFoundException(ApiErrorCodes.ERROR_WHILE_SENDING_EMAIL.getErrorCode(), ApiErrorCodes.ERROR_WHILE_SENDING_EMAIL.getErrorMessage());
        }
    }

}