package com.finalproject.sulbao.board.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
public class BoardCategory {

    @OneToMany(mappedBy = "boardCategory", cascade = ALL, orphanRemoval = true)
    private final List<Post> posts = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;

    public BoardCategory(String name) {
        this.name = name;
    }

}
