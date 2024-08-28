package com.finalproject.sulbao.board.repository;

import com.finalproject.sulbao.board.common.BoardCategoryConstants;
import com.finalproject.sulbao.board.domain.Post;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostSpecification {

    public static Specification<Post> titleContains(String keyword) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("title"), "%" + keyword + "%");
    }

    public static Specification<Post> contentContains(String keyword) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("content"), "%" + keyword + "%");
    }

    public static Specification<Post> categoryEquals(String categoryNameKr) {
        return (root, query, criteriaBuilder) -> {
            if (categoryNameKr == null) {
                return null;
            }

            Long categoryId;
            if (categoryNameKr.equals(BoardCategoryConstants.ZZANFEED)) {
                categoryId = BoardCategoryConstants.ZZANFEED_ID;
            } else if (categoryNameKr.equals(BoardCategoryConstants.ZZANPOST)) {
                categoryId = BoardCategoryConstants.ZZANPOST_ID;
            } else {
                return null;
            }

            return criteriaBuilder.equal(root.get("boardCategory").get("id"), categoryId);
        };
    }

    public static Specification<Post> dateBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("createdAt"), startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
    }

}
