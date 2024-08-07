package com.finalproject.sulbao.cart.domain;

import com.finalproject.sulbao.cart.vo.OrderItem;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;


@Entity
@Table(name = "tbl_order")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderCode")
    private Long orderCode;   // 주문코드
    @Column(name = "orderDate")
    private LocalDate orderDate; // 주문날짜
    @Column(name = "delivery")
    private String delivery;  // 배송상태
    @Column(name = "isReviewed")
    private boolean isReviewed; // 리뷰작성여부
    @Column(name = "isPresent")
    private boolean isPresent; // 선결제여부
    @Column(name = "presentEmail")
    private String presentEmail; // 선물받는사람 이메일
    @Column(name = "names")
    private String names; // 수신자 이름
    @Column(name = "phoneNumber")
    private String phoneNumber; // 수신자 핸드폰번호
    @Column(name = "address1")
    private String address1; // 주소1
    @Column(name = "address2")
    private String address2; // 주소2
    @Column(name = "zipCode")
    private String zipCode; // 우편번호

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "tbl_order_itme",
            joinColumns = @JoinColumn(name = "orderCode", referencedColumnName = "orderCode")
    )
    private Set<OrderItem> orderItems;
}
