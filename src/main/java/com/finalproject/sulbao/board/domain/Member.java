package com.finalproject.sulbao.board.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Member {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String name;

    private String email;

    private String ranks;

    private String profileImg;

    public Member(String username, String password, String name, String email, String ranks) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.ranks = ranks;
    }

}
