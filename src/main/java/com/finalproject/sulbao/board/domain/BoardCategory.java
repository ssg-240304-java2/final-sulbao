package com.finalproject.sulbao.board.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
public class BoardCategory {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "boardCategory", cascade = ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    public BoardCategory(String name) {
        this.name = name;
    }

}
