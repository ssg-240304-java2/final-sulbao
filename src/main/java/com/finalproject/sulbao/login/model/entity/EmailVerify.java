package com.finalproject.sulbao.login.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_email_verify")
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerify {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;
    @Column(unique = true, nullable = false)
    private String email;
    private String code;
    private boolean isVerified;


    @Builder
    public EmailVerify(String email, String code) {
        this.email = email;
        this.code = code;
    }
}
