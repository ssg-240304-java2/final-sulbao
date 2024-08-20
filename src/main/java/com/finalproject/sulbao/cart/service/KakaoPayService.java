package com.finalproject.sulbao.cart.service;

import com.finalproject.sulbao.cart.dto.ApproveResponse;
import com.finalproject.sulbao.cart.dto.ReadyResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class KakaoPayService {
        public ReadyResponse payReady(String name, int totalPrice,String userId, int orderCode, int quantity) {

                Map<String, String> parameters = new HashMap<>();
                parameters.put("cid", "TC0ONETIME");                                    // 가맹점 코드(테스트용)
                parameters.put("partner_order_id", String.valueOf(orderCode));                       // 주문번호
                parameters.put("partner_user_id", userId);                          // 회원 아이디
                parameters.put("item_name", name);                                      // 상품명
                parameters.put("quantity", String.valueOf(quantity));                                        // 상품 수량
                parameters.put("total_amount", String.valueOf(totalPrice));             // 상품 총액
                parameters.put("tax_free_amount", "0");                                 // 상품 비과세 금액
                parameters.put("approval_url", "http://localhost:8080/payments/completed"); // 결제 성공 시 URL
                parameters.put("cancel_url", "http://localhost:8080/orders/cart/order_form");      // 결제 취소 시 URL
                parameters.put("fail_url", "http://localhost:8080/orders/cart/order_form");          // 결제 실패 시 URL

                // HttpEntity : HTTP 요청 또는 응답에 해당하는 Http Header와 Http Body를 포함하는 클래스
                HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

                // RestTemplate
                // : Rest 방식 API를 호출할 수 있는 Spring 내장 클래스
                //   REST API 호출 이후 응답을 받을 때까지 기다리는 동기 방식 (json, xml 응답)
                RestTemplate template = new RestTemplate();
                String url = "https://open-api.kakaopay.com/online/v1/payment/ready";
                // RestTemplate의 postForEntity : POST 요청을 보내고 ResponseEntity로 결과를 반환받는 메소드
                ResponseEntity<ReadyResponse> responseEntity = template.postForEntity(url, requestEntity, ReadyResponse.class);
                log.info("결제준비 응답객체: " + responseEntity.getBody());

                return responseEntity.getBody();
        }

        public ApproveResponse payApprove(String tid, String pgToken, String userId, int orderCode) {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("cid", "TC0ONETIME");              // 가맹점 코드(테스트용)
                parameters.put("tid", tid);                       // 결제 고유번호
                parameters.put("partner_order_id", String.valueOf(orderCode)); // 주문번호
                parameters.put("partner_user_id", userId);    // 회원 아이디
                parameters.put("pg_token", pgToken);              // 결제승인 요청을 인증하는 토큰

                HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

                RestTemplate template = new RestTemplate();
                String url = "https://open-api.kakaopay.com/online/v1/payment/approve";
                ApproveResponse approveResponse = template.postForObject(url, requestEntity, ApproveResponse.class);
                log.info("결제승인 응답객체: " + approveResponse);

                return approveResponse;
        }

        // 카카오페이 측에 요청 시 헤더부에 필요한 값
        private HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "DEV_SECRET_KEY DEV092BBF98D492503CCE6919631C06AC4AD8323");
                headers.set("Content-type", "application/json");

                return headers;
        }
}
