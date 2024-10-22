package com.esquad.esquadbe.smtp.service;

import com.esquad.esquadbe.user.entity.User;
import com.esquad.esquadbe.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    private final Map<String, String> verificationNumber = new HashMap<>();

    private final JavaMailSender javaMailSender;

    private static final String senderEmail = "E-squad@megazon.com";

    public String createNumber() {
        Random random = new Random();
        StringBuilder number = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            int num = random.nextInt(10);
            number.append(num);
        }
        return number.toString();
    }

    public MimeMessage createIdMimeMail(String mail, String number) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();


        String senderName = "E-squad";
        String senderNameWithEmail = String.format(senderEmail, senderName);

        message.setFrom(new InternetAddress(senderNameWithEmail));
        message.setRecipients(MimeMessage.RecipientType.TO, mail);
        message.setSubject("아이디 조회 인증번호 입니다.");
        message.setText(number);
        return message;

    }

    public void storeVerificationNumber(String email, String number) {
        verificationNumber.put(email, number);
    }

    public String sendIdResetMail(String sendEmail) throws MessagingException {

        String number = createNumber();

        MimeMessage mimeMessage = createIdMimeMail(sendEmail, number);
        try {
            javaMailSender.send(mimeMessage);
            storeVerificationNumber(sendEmail, number);
        } catch (MailException e) {
            e.printStackTrace();
            throw new MessagingException();
        }
        return number;
    }

    public boolean checkVerificationNumber(String email, String number) {
        String savedNumber = verificationNumber.get(email);
        return savedNumber != null && savedNumber.equals(number);
    }


    public String maskedUsername(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null || user.getUsername() == null) {
            return null;
        }
        String username = user.getUsername();
        return username.substring(0, username.length() - 2) + "**";
    }

    public MimeMessage createPasswdMimeMail(String email, String number) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        String senderName = "E-squad";
        String senderNameWithEmail = String.format(senderEmail, senderName);

        message.setFrom(new InternetAddress(senderNameWithEmail));
        message.setRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("비밀번호 재설정 인증번호 입니다.");
        message.setText(number);
        return message;

    }

    // 비밀번호 재설정용 이메일 전송
    public String sendPasswordResetMail(String email, String username) throws MessagingException {
        User user = userRepository.findByEmailAndUsername(email, username);
        if (user == null) {
            throw new IllegalArgumentException("해당 이메일로 등록된 사용자를 찾을 수 없습니다.");
        }

        String number = createNumber();
        MimeMessage mimeMessage = createPasswdMimeMail(email, number);
        javaMailSender.send(mimeMessage);

        storeVerificationNumber(email, number);
        return number;
    }


    // 비밀번호 재설정
    public void resetPassword(String email, String number, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }
        if (!checkVerificationNumber(email, number)) {
            throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
        }
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("해당 이메일로 등록된 사용자를 찾을 수 없습니다.");
        }

        String encordedPassword = bCryptPasswordEncoder.encode(newPassword);

        // 비밀번호 암호화 필요
        user = User.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(encordedPassword)  // 실제 사용 시에는 비밀번호를 암호화해야 합니다.
                .address(user.getAddress())
                .birthDay(user.getBirthDay())
                .phoneNumber(user.getPhoneNumber())
                .nickname(user.getNickname())
                .build();

        userRepository.save(user);
    }
}