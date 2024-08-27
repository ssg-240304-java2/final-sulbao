package com.finalproject.sulbao.login.model.service;


import com.finalproject.sulbao.cart.dto.CartDTO;
import com.finalproject.sulbao.login.model.dto.EmailMessage;
import com.finalproject.sulbao.login.model.entity.EmailVerify;
import com.finalproject.sulbao.login.model.repository.EmailRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final EmailRepository emailRepository;

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
        String photo = "https://kr.object.ncloudstorage.com/sulbao-file/main/sulbao-blue.png";
        context.setVariable("code", code);
        context.setVariable("photo", photo);
        return templateEngine.process(type, context);
    }

    public String presentSendMail(EmailMessage emailMessage, List<CartDTO> cartLists,String nickname ,String type) {
        try {
            InternetAddress emailAddr = new InternetAddress(emailMessage.getTo());
            emailAddr.validate();
        } catch (AddressException ex) {
            System.out.println(emailMessage.getTo() + "=========================================이메일 서버 false");
        }



        String token = UUID.randomUUID().toString();
        System.out.println(nickname);
        System.out.println(cartLists + "아약스아약스"); // 카트리스트 번호

//        String link = "http://localhost:8080/validateOrder?token=" + token;
        String link = "https://hansool.shop/validateOrder?token=" + token;
        String photo = "https://kr.object.ncloudstorage.com/sulbao-file/main/sulbao-blue.png";
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo()); // 메일 수신자
            mimeMessageHelper.setSubject(emailMessage.getSubject()); // 메일 제목

//            String htmlContent = "<a id='code' href='" + link + "'>링크를 눌러 배송지를 등록해주세요.</a>";
//
//            mimeMessageHelper.setText(htmlContent, true); // 메일 본문 내용, HTML 여부
//            mimeMessageHelper.setText(setContext(link, type), true); // 메일 본문 내용, HTML 여부

            Context context = new Context();
            context.setVariable("nickname", nickname);
            context.setVariable("cartLists", cartLists);
            context.setVariable("link", link);
            context.setVariable("photo", photo);
            // Thymeleaf 템플릿 처리
            String htmlContent = templateEngine.process("present-email", context);
            mimeMessageHelper.setText(htmlContent, true); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);

            return token;

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }
    // 메일 내용 적용
    public String sendMail(EmailMessage emailMessage, String type) {

        try {
            InternetAddress emailAddr = new InternetAddress(emailMessage.getTo());
            emailAddr.validate();
            System.out.println(emailMessage.getTo() + "========================================= true email address");
        } catch (AddressException ex) {
            System.out.println(emailMessage.getTo() + "========================================= false email address");
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

    @Transactional
    public void saveEmailConfirm(Map<String, String> emailMap) {
        String email = emailMap.get("id");
        String code = emailMap.get("code");

        EmailVerify existingEmailVerify = emailRepository.findByEmail(email);

        if (existingEmailVerify != null) {
            existingEmailVerify.setCode(code);
            emailRepository.save(existingEmailVerify);
        } else {
            EmailVerify newEmailVerify = new EmailVerify();
            newEmailVerify.setEmail(email);
            newEmailVerify.setCode(code);
            emailRepository.save(newEmailVerify);
        }
    }

    @Transactional
    public Boolean confirmEmailByCode(EmailVerify emailVerify) {
        String email = emailVerify.getEmail();
        String code = emailVerify.getCode();

        String originCode = emailRepository.findCodeByEmail(email);

        boolean isVerified = false;
        isVerified = originCode.equals(code);

        if(isVerified){

            EmailVerify emailVerify2 = emailRepository.findByEmail(email);

            emailVerify2.confirmedCode();
            return isVerified;
        }

        return isVerified;
    }


    // 권한 승인 메일 전송
    public void sendApproveMail(EmailMessage emailMessage, String type) {

        try {
            InternetAddress emailAddr = new InternetAddress(emailMessage.getTo());
            emailAddr.validate();
        } catch (AddressException ex) {}

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo()); // 메일 수신자
            mimeMessageHelper.setSubject(emailMessage.getSubject()); // 메일 제목
            mimeMessageHelper.setText(setContext("", type), true); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    // 배송지 등록요청 메일
    public void presentDelaySendMail(EmailMessage emailMessage, String link, String type) {

//        try {
//            InternetAddress emailAddr = new InternetAddress(emailMessage.getTo());
//            emailAddr.validate();
//        } catch (AddressException ex) {
//            System.out.println(emailMessage.getTo() + "=========================================이메일 서버 false");
//        }
//
//        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//
//        try {
//            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
//            mimeMessageHelper.setTo("lucinda96@naver.com"); // 메일 수신자
//            mimeMessageHelper.setSubject("[🍶술기로운 한 잔] 선물 받은 상품의 배송지를 입력해주세요"); // 메일 제목
//
//            String htmlContent = "<a id='code' href='" + link + "'>링크를 눌러 배송지를 등록해주세요.</a>";
//            mimeMessageHelper.setText(htmlContent, true);
//            mimeMessageHelper.setText(setContext(link, type), true);
//            javaMailSender.send(mimeMessage);
//
//            log.info("Maile Send Success");
//
//        } catch (MessagingException e) {
//            log.info("Maile Send fail");
//            throw new RuntimeException(e);
//        }
        try {
            InternetAddress emailAddr = new InternetAddress(emailMessage.getTo());
            emailAddr.validate();
        } catch (AddressException ex) {
            System.out.println(emailMessage.getTo() + "=========================================이메일 서버 false");
        }


        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo()); // 메일 수신자
            mimeMessageHelper.setSubject(emailMessage.getSubject()); // 메일 제목

            String htmlContent = "<a id='code' href='" + link + "'>링크를 눌러 배송지를 등록해주세요.</a>";

            mimeMessageHelper.setText(htmlContent, true); // 메일 본문 내용, HTML 여부
            mimeMessageHelper.setText(setContext(link, type), true); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);


        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
