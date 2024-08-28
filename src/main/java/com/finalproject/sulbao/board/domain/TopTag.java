package com.finalproject.sulbao.board.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "tbl_top_tag")
public class TopTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long count;

    public TopTag(String name, Long count) {
        this.name = name;
        this.count = count;
    }

}
