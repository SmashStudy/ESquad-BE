package com.esquad.esquadbe.smtp.controller;


import com.esquad.esquadbe.smtp.dto.MailDTO;
import com.esquad.esquadbe.smtp.service.MailService;
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
                return "사용자 아이디를 찾을 수 없습니다.";
            }
        } else {
            return "인증번호가 일치하지 않습니다.";
        }
    }

    // 비밀번호 재설정 메일 전송 요청
    @PostMapping("/recover-password")
    public String requestPasswordReset(@RequestBody MailDTO mailDTO) throws MessagingException {
        String email = mailService.sendPasswordResetMail(mailDTO.getEmail(), mailDTO.getUsername());
        return email;
    }

    // 비밀번호 재설정 처리
    @PostMapping("/verify-password")
    public String confirmPasswordReset(@RequestBody MailDTO mailDTO) throws MessagingException {
        mailService.resetPassword(mailDTO.getEmail(), mailDTO.getNumber(), mailDTO.getNewPassword(), mailDTO.getConfirmPassword());
        return "비밀번호가 성공적으로 재설정되었습니다.";
    }
}