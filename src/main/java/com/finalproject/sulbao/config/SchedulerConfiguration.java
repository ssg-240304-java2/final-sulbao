package com.finalproject.sulbao.config;

import com.finalproject.sulbao.cart.domain.Order;
import com.finalproject.sulbao.cart.repository.OrderRepository;
import com.finalproject.sulbao.login.model.dto.EmailMessage;
import com.finalproject.sulbao.login.model.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfiguration {

    private final OrderRepository orderRepository;
    private final EmailService emailService;


    @Scheduled(cron = "0 0 1 * * *", zone = "Asia/Seoul")
    public void run(){

        // order 테이블에서 선물 상품중 배송지 입력이 안된 주문내역 조회
        List<Order> order = orderRepository.findByPresentInfo();
        log.info("Order List 사이즈 : {}", order.size());
        // 배송지 입력해달라는 메일발송
        for(Order orderItem : order){
            String link = "https://hansool.shop/validateOrder?token=" + orderItem.getToken();
            EmailMessage emailMessage = EmailMessage.builder()
                    .to("leeeunsol73@gmail.com") // 테스트로 임시로 넣어둠 수요일 로그확인 후 수정할예정
                    .subject("[술기로운한잔] 선물하기 주소 입력")
                    .build();
            emailService.presentDelaySendMail(emailMessage,link,"present-delay-email");
        }

    }

    //메일 발송 세팅
//    public void sendMail(Order order){
//        String type ="present-delay-email";
//        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//        String link = "https://hansool.shop/validateOrder?token=" + order.getToken();
//
//        try {
//            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
//            mimeMessageHelper.setTo("lucinda96@naver.com"); // 메일 수신자
//            mimeMessageHelper.setSubject("[🍶술기로운 한 잔] 선물 받은 상품의 배송지를 입력해주세요"); // 메일 제목
//
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
//    }
//
//    // thymeleaf를 통한 html 적용
//    public String setContext(String code, String type) {
//        Context context = new Context();
//        context.setVariable("code", code);
//        return templateEngine.process(type, context);
//    }
}
