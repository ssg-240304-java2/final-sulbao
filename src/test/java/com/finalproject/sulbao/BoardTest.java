package com.finalproject.sulbao;

import com.finalproject.sulbao.board.domain.BoardCategory;
import com.finalproject.sulbao.board.domain.Comment;
import com.finalproject.sulbao.board.domain.Post;
import com.finalproject.sulbao.board.domain.PostImage;
import com.finalproject.sulbao.board.repository.*;
import com.finalproject.sulbao.login.model.entity.Login;
import com.finalproject.sulbao.login.model.repository.LoginRepository;
import com.finalproject.sulbao.login.model.repository.MemberInfoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
@Rollback(false)
class BoardTest {

    @Autowired
    BoardCategoryRepository boardCategoryRepository;
    @Autowired
    LoginRepository loginRepository;
    @Autowired
    PostImageRepository postImageRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private MemberInfoRepository memberInfoRepository;
    @Autowired
    private LikeRepository likeRepository;

    @Test
    void load() {
        insertBoardCategory();
//        Login login = loginRepository.findById(5L).orElseThrow();
//        BoardCategory boardCategory = boardCategoryRepository.findById(1L).orElseThrow();
//        insertPost(login, boardCategory);
//        Post post = postRepository.findById(1L).orElseThrow();
//        insertComment(login, post);
    }

    private void insertComment(Login login, Post post) {
        Comment comment = Comment.createComment("The greatest glory in living lies not in never falling, but in rising every time we fall.", login, post);
        commentRepository.save(comment);
    }

    private void insertPost(Login john, BoardCategory boardCategory) {
        for (int i = 0; i < 100; i++) {
            String thumbnail = "homer.jpg";
            String content = """
                    <p class="p1" style="margin-right: 0px; margin-bottom: 0px; margin-left: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-variant-alternates: normal; font-size-adjust: none; font-kerning: auto; font-optical-sizing: auto; font-feature-settings: normal; font-variation-settings: normal; font-variant-position: normal; font-stretch: normal; line-height: normal; font-family: &quot;.SF NS&quot;; color: rgb(14, 14, 14);">전통주는 단순히 마시는 술이 아닙니다. 그 안에는 한국의 오랜 역사와 문화를 담은 이야기들이 있습니다.<span class="Apple-converted-space">&nbsp;</span></p>|<p class="p1" style="margin-right: 0px; margin-bottom: 0px; margin-left: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-variant-alternates: normal; font-size-adjust: none; font-kerning: auto; font-optical-sizing: auto; font-feature-settings: normal; font-variation-settings: normal; font-variant-position: normal; font-stretch: normal; line-height: normal; font-family: &quot;.SF NS&quot;; color: rgb(14, 14, 14);">한 잔의 전통주로 그 깊은 이야기를 느껴보세요. 여러분의 특별한 순간에 함께할 전통주를 소개합니다!</p>
                    """;

            List<String> tags = List.of("#스크롤테스트");

            List<PostImage> postImages = List.of(
                    PostImage.createPostImage("47877d26-280a-477b-aa70-081391ebf2a4.jpg"),
                    PostImage.createPostImage("8e6b6ef1-216a-428c-b83f-298001481c7d.jpg")
            );

//            Post post = Post.createPost(john, boardCategory, "전통주, 그리고 우리의 이야기", content, thumbnail, postImages, tags);
//            postRepository.save(post);
        }
    }

    private void insertBoardCategory() {
        BoardCategory zzanFeed = new BoardCategory("짠 피드");
        BoardCategory zzanPost = new BoardCategory("술 포스트");
        boardCategoryRepository.save(zzanFeed);
        boardCategoryRepository.save(zzanPost);
    }

}
