package com.finalproject.sulbao.login.model.service;

import com.finalproject.sulbao.login.model.dto.EmailMessage;
import com.finalproject.sulbao.login.model.entity.EmailVerify;
import com.finalproject.sulbao.login.model.repository.EmailRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final EmailRepository emailRepository;

    private static final String senderEmail= "이메일 입";

    // 인증번호 및 임시 비밀번호 생성 메서드
    public String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(4);

            switch (index) {
                case 0: key.append((char) ((int) random.nextInt(26) + 97)); break;
                case 1: key.append((char) ((int) random.nextInt(26) + 65)); break;
                default: key.append(random.nextInt(9));
            }
        }
        return key.toString();
    }

    // thymeleaf를 통한 html 적용
    public String setContext(String code, String type) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process(type, context);
    }

    // 메일 내용 적용
    public String sendMail(EmailMessage emailMessage, String type) {

        try {
            InternetAddress emailAddr = new InternetAddress(emailMessage.getTo());
            emailAddr.validate();
            System.out.println(emailMessage.getTo() + "========================================= true");
        } catch (AddressException ex) {
            System.out.println(emailMessage.getTo() + "========================================= false");
        }
        String code = createCode();

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo()); // 메일 수신자
            mimeMessageHelper.setSubject(emailMessage.getSubject()); // 메일 제목
            mimeMessageHelper.setText(setContext(code, type), true); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);

            return code;

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
//
//    public void saveEmailConfirm(Map<String, String> emailMap) {
//        String email = emailMap.get("id");
//        String code = emailMap.get("code");
//
//        EmailVerify existingEmailVerify = emailRepository.findByEmail(email);
//
//        if (existingEmailVerify != null) {
//            // Update the existing code
//            existingEmailVerify.setCode(code);
//            emailRepository.save(existingEmailVerify);
//        } else {
//            // Insert new entry
//            EmailVerify newEmailVerify = new EmailVerify();
//            newEmailVerify.setEmail(email);
//            newEmailVerify.setCode(code);
//            emailRepository.save(newEmailVerify);
//        }
//    }
}
