package com.esquad.esquadbe.smtp.controller;


import com.esquad.esquadbe.smtp.dto.MailDTO;
import com.esquad.esquadbe.smtp.service.MailService;
import com.esquad.esquadbe.user.exception.UserNumberException;
import com.esquad.esquadbe.user.exception.UserUsernameException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class MailController {
    private final MailService mailService;

    @PostMapping("/recover-username")
    public String emailSend(@RequestBody MailDTO mailDTO) throws MessagingException {
        String email = mailService.sendIdResetMail(mailDTO.getEmail());
        return email;
    }

    @PostMapping("/verify-username")
    public String numCheck(@RequestBody MailDTO mailDTO) throws MessagingException {
        boolean isVerified = mailService.checkVerificationNumber(mailDTO.getEmail(), mailDTO.getNumber());
        if (isVerified) {
            String maskedId = mailService.maskedUsername(mailDTO.getEmail());
            if (maskedId != null) {
                return maskedId;
            } else {
                throw new UserUsernameException();
            }
        } else {
            throw new UserNumberException();
        }
    }

    @PostMapping("/recover-password")
    public String requestPasswordReset(@RequestBody MailDTO mailDTO) throws MessagingException {
        String email = mailService.sendPasswordResetMail(mailDTO.getEmail(), mailDTO.getUsername());
        return email;
    }

    @PostMapping("/verify-password")
    public String confirmPasswordReset(@RequestBody MailDTO mailDTO) {
        String email = mailService.resetPassword(mailDTO.getEmail(), mailDTO.getNumber(), mailDTO.getNewPassword(), mailDTO.getConfirmPassword());
        return email;
    }
}