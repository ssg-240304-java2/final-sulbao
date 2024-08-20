package com.finalproject.sulbao.login.model.vo;

import com.finalproject.sulbao.board.domain.Post;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberImage {

    private String fileName;
    private String saveName;
    private String saveImgUrl;

}