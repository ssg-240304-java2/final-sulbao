package com.finalproject.sulbao.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfiguration {

    @Scheduled(cron = "0 30 9 * * *", zone = "Asia/Seoul")
    public void run(){

        log.info("Running Scheduler");
        // order 테이블에서 선물 상품중 배송지 입력이 안된 주문내역 조회

        // 배송지 입력해달라는 메일발송



    }
}
