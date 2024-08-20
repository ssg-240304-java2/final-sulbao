package com.finalproject.sulbao.board.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "tbl_board_category")
public class BoardCategory {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "boardCategory", cascade = ALL, orphanRemoval = true)
    private final List<Post> posts = new ArrayList<>();

    public BoardCategory(String name) {
        this.name = name;
    }

}
